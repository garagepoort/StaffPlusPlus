package net.shortninja.staffplus.core.domain.staff.mode.item;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.mode.ModeProvider;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeItemsService;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.vanish.VanishModeConfiguration;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@IocBean(conditionalOnProperty = "vanish-module.enabled=true")
@IocListener
public class VanishItemUpdater implements Listener {

    private final PlayerSettingsRepository playerSettingsRepository;
    private final IProtocolService protocolService;
    private final VanishModeConfiguration vanishModeConfiguration;
    private final ModeProvider modeProvider;
    private final StaffModeItemsService staffModeItemsService;

    public VanishItemUpdater(PlayerSettingsRepository playerSettingsRepository,
                             IProtocolService protocolService,
                             Options options,
                             ModeProvider modeProvider,
                             StaffModeItemsService staffModeItemsService) {
        this.playerSettingsRepository = playerSettingsRepository;
        this.protocolService = protocolService;
        this.vanishModeConfiguration = options.staffItemsConfiguration.getVanishModeConfiguration();
        this.modeProvider = modeProvider;
        this.staffModeItemsService = staffModeItemsService;
    }

    @EventHandler
    public void vanishOn(VanishOnEvent event) {
        resetVanishItem(event.getPlayer());
    }

    @EventHandler
    public void vanishOff(VanishOffEvent event) {
        resetVanishItem(event.getPlayer());
    }

    private void resetVanishItem(Player player) {
        PlayerSettings playerSettings = playerSettingsRepository.get(player);
        if (playerSettings.isInStaffMode()) {
            GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, playerSettings.getModeName().get());
            getVanishItemSlot(player).ifPresent(slot -> {
                staffModeItemsService.setModeItem(player, playerSettings, vanishModeConfiguration, slot, modeConfiguration);
            });
        }
    }

    private Optional<Integer> getVanishItemSlot(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
            ItemStack item = contents[i];
            String nbtString = protocolService.getVersionProtocol().getNbtString(item);
            if (vanishModeConfiguration.getIdentifier().equalsIgnoreCase(nbtString)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }
}
