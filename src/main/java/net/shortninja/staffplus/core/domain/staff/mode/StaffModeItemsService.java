package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.vanish.VanishModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.custommodules.CustomModuleConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.custommodules.state.CustomModuleStateMachine;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@IocBean
public class StaffModeItemsService {

    private static final Logger logger = StaffPlusPlus.get().getLogger();
    private final List<ModeItemConfiguration> MODE_ITEMS;

    private final PlayerSettingsRepository playerSettingsRepository;
    private final CustomModuleStateMachine customModuleStateMachine;

    public StaffModeItemsService(Options options, PlayerSettingsRepository playerSettingsRepository, CustomModuleStateMachine customModuleStateMachine) {
        this.playerSettingsRepository = playerSettingsRepository;

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
            ), options.customModuleConfigurations)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        this.customModuleStateMachine = customModuleStateMachine;
    }

    public void setStaffModeItems(Player player, GeneralModeConfiguration modeConfiguration) {
        PlayerSettings session = playerSettingsRepository.get(player);
        JavaUtils.clearInventory(player);

        modeConfiguration.getItemSlots().forEach((moduleName, slot) -> {
            Optional<? extends ModeItemConfiguration> module = getModule(moduleName);
            if (!module.isPresent()) {
                logger.warning("No module found with name [" + moduleName + "]. Skipping...");
            } else {
                setModeItem(player, session, module.get(), slot, modeConfiguration);
            }
        });
    }

    public void setModeItem(Player player, PlayerSettings session, ModeItemConfiguration modeItem, int slot, GeneralModeConfiguration modeConfiguration) {
        if (!modeItem.isEnabled()) {
            return;
        }

        if(modeItem instanceof CustomModuleConfiguration) {
            CustomModuleConfiguration customModuleConfiguration = (CustomModuleConfiguration) modeItem;
            if(customModuleConfiguration.getEnabledOnState().isPresent() && !customModuleStateMachine.isActive(player, customModuleConfiguration.getEnabledOnState().get())) {
                return;
            }
        }

        if (modeItem instanceof VanishModeConfiguration) {
            player.getInventory().setItem(slot, ((VanishModeConfiguration) modeItem).getModeVanishItem(session, modeConfiguration.getModeVanish()));
        } else {
            player.getInventory().setItem(slot, modeItem.getItem());
        }
    }

    private Optional<? extends ModeItemConfiguration> getModule(String name) {
        return MODE_ITEMS.stream()
            .filter(m -> m.getIdentifier().equals(name))
            .findFirst();
    }
}
