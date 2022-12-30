package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigObjectList;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.vanish.VanishModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.custommodules.CustomModuleConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.custommodules.state.CustomModuleStateMachine;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@IocBean
public class StaffModeItemsService {

    private static final Logger logger = StaffPlusPlus.get().getLogger();

    private final List<ModeItemConfiguration> MODE_ITEMS;
    private final PlayerSettingsRepository playerSettingsRepository;
    private final List<CustomModuleConfiguration> customModuleConfigurations;
    private final CustomModuleStateMachine customModuleStateMachine;
    private final IProtocolService protocolService;
    private final ModeProvider modeProvider;

    public StaffModeItemsService(Options options, PlayerSettingsRepository playerSettingsRepository,
                                 CustomModuleStateMachine customModuleStateMachine,
                                 IProtocolService protocolService,
                                 @ConfigProperty("staffmode-custom-modules:custom-modules")
                                 @ConfigObjectList(CustomModuleConfiguration.class)
                                 List<CustomModuleConfiguration> customModuleConfigurations, ModeProvider modeProvider) {
        this.playerSettingsRepository = playerSettingsRepository;
        this.customModuleConfigurations = customModuleConfigurations;
        this.modeProvider = modeProvider;

        MODE_ITEMS = Stream.of(Arrays.asList(
                    options.staffItemsConfiguration.getCompassModeConfiguration(),
                    options.staffItemsConfiguration.getRandomTeleportModeConfiguration(),
                    options.staffItemsConfiguration.getVanishModeConfiguration(),
                    options.staffItemsConfiguration.getGuiModeConfiguration(),
                    options.staffItemsConfiguration.getCounterModeConfiguration(),
                    options.staffItemsConfiguration.getFreezeModeConfiguration(),
                    options.staffItemsConfiguration.getCpsModeConfiguration(),
                    options.staffItemsConfiguration.getExamineModeConfiguration(),
                    options.staffItemsConfiguration.getFollowModeConfiguration(),
                    options.staffItemsConfiguration.getPlayerDetailsModeConfiguration()
                ),
                customModuleConfigurations)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        this.customModuleStateMachine = customModuleStateMachine;
        this.protocolService = protocolService;
    }

    public void setModuleItem(Player player, StaffModule staffModule, int slot) {
        PlayerSettings playerSettings = playerSettingsRepository.get(player);
        GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, playerSettings.getModeName().get());
        setItem(player, modeConfiguration, playerSettings, staffModule.getKey(), slot);
    }

    public void setStaffModeItems(Player player, GeneralModeConfiguration modeConfiguration) {
        PlayerSettings session = playerSettingsRepository.get(player);
        JavaUtils.clearInventory(player);

        modeConfiguration.getItemSlots()
            .forEach((moduleName, slot) -> setItem(player, modeConfiguration, session, moduleName, slot));
    }

    /**
     * This methods assumes you are already in staff mode.
     * It will refresh the entire inventory based on the mode configuration and current mode states
     * Useful after a state change has occurred.
     * @param player
     * @param modeConfiguration
     */
    public void refreshStaffModeItems(Player player, GeneralModeConfiguration modeConfiguration) {
        PlayerSettings session = playerSettingsRepository.get(player);
        Map<String, Integer> currentLocationsOfModules = getCurrentLocationsOfModules(player);
        JavaUtils.clearInventory(player);

        modeConfiguration.getItemSlots().forEach((moduleName, slot) -> {
            int locationOfItem = getNewLocationOfModuleItem(moduleName, modeConfiguration, currentLocationsOfModules);
            setItem(player, modeConfiguration, session, moduleName, locationOfItem);
        });
    }

    public void setItem(Player player, GeneralModeConfiguration modeConfiguration, PlayerSettings session, String moduleName, int locationOfItem) {
        Optional<? extends ModeItemConfiguration> module = getModule(moduleName);
        if (!module.isPresent()) {
            logger.warning("No module found with name [" + moduleName + "]. Skipping...");
            return;
        }

        ModeItemConfiguration modeItem = module.get();
        if (!modeItem.isEnabled()) {
            return;
        }

        if (modeItem instanceof CustomModuleConfiguration) {
            CustomModuleConfiguration customModuleConfiguration = (CustomModuleConfiguration) modeItem;
            if (customModuleConfiguration.getEnabledOnState().isPresent() && !customModuleStateMachine.isActive(player, customModuleConfiguration.getEnabledOnState().get())) {
                return;
            }
        }

        if (modeItem instanceof VanishModeConfiguration) {
            ItemStack modeVanishItem = ((VanishModeConfiguration) modeItem).getModeVanishItem(session, modeConfiguration.getModeVanish());
            ItemStack item = protocolService.getVersionProtocol().addNbtString(modeVanishItem, modeItem.getIdentifier());
            player.getInventory().setItem(locationOfItem, item);
        } else {
            ItemStack item = protocolService.getVersionProtocol().addNbtString(modeItem.getItem(), modeItem.getIdentifier());
            player.getInventory().setItem(locationOfItem, item);
        }
    }

    public Optional<? extends ModeItemConfiguration> getModule(String name) {
        return MODE_ITEMS.stream()
            .filter(m -> m.getIdentifier().equals(name))
            .findFirst();
    }

    public Optional<CustomModuleConfiguration> getCustomModule(String name) {
        return customModuleConfigurations.stream()
            .filter(m -> m.getIdentifier().equals(name))
            .findFirst();
    }

    public Optional<? extends ModeItemConfiguration> getModule(ItemStack item) {
        String identifier = protocolService.getVersionProtocol().getNbtString(item);
        return getModule(identifier);
    }

    public Optional<CustomModuleConfiguration> getCustomModule(ItemStack item) {
        String identifier = protocolService.getVersionProtocol().getNbtString(item);
        return getCustomModule(identifier);
    }

    private int getNewLocationOfModuleItem(String moduleToSet, GeneralModeConfiguration modeConfiguration, Map<String, Integer> currentLocationsOfModules) {
        if (currentLocationsOfModules.containsKey(moduleToSet)) {
            return currentLocationsOfModules.get(moduleToSet);
        }

        List<String> modulesWithSameLocation = modeConfiguration.getModulesWithSameLocation(moduleToSet);
        for (String module : modulesWithSameLocation) {
            if (currentLocationsOfModules.containsKey(module)) {
                return currentLocationsOfModules.get(module);
            }
        }
        return modeConfiguration.getItemSlots().get(moduleToSet);
    }

    private Map<String, Integer> getCurrentLocationsOfModules(Player player) {
        Map<String, Integer> result = new HashMap<>();
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null) {
                String moduleName = protocolService.getVersionProtocol().getNbtString(item);
                if (StringUtils.isNotEmpty(moduleName)) {
                    result.put(moduleName, i);
                }
            }
        }
        return result;
    }
}
