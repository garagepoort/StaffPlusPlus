package net.shortninja.staffplus.core.vanish.listeners;

import be.garagepoort.mcioc.IocContainer;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.load.InjectTubingPlugin;
import be.garagepoort.mcioc.load.OnLoad;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.vanish.VanishService;
import net.shortninja.staffplus.core.vanish.gui.VanishPlayersBukkitService;
import net.shortninja.staffplusplus.session.IPlayerSession;
import net.shortninja.staffplusplus.session.SessionManager;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.LazyMetadataValue;

@IocBukkitListener(conditionalOnProperty = "vanish-module.enabled=true")
@IocMultiProvider(OnLoad.class)
public class VanishJoinListener implements Listener, OnLoad {

    private final VanishPlayersBukkitService vanishPlayersBukkitService;
    private final PlayerSettingsRepository playerSettingsRepository;
    private final VanishService vanishService;
    private final TubingBukkitPlugin staffPlusPlus;
    private final SessionManager sessionManager;
    private final PlayerManager playerManager;

    public VanishJoinListener(VanishPlayersBukkitService vanishPlayersBukkitService,
                              PlayerSettingsRepository playerSettingsRepository,
                              VanishService vanishService,
                              @InjectTubingPlugin TubingBukkitPlugin staffPlusPlus,
                              SessionManager sessionManager,
                              PlayerManager playerManager) {
        this.vanishPlayersBukkitService = vanishPlayersBukkitService;
        this.playerSettingsRepository = playerSettingsRepository;
        this.vanishService = vanishService;
        this.staffPlusPlus = staffPlusPlus;
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void hideJoinMessage(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // vanish immediately to prevent blinking when joining
        vanishPlayersBukkitService.updateVanish(player);
        PlayerSettings playerSettings = playerSettingsRepository.get(player);
        if (playerSettings.isVanished()) {
            vanishService.addVanish(player, playerSettings.getVanishType(), true);
            event.setJoinMessage("");
        }
    }

    @EventHandler
    public void setMetaData(StaffPlusPlusJoinedEvent event) {
        setVanishMetaData(event.getPlayer());
    }

    @Override
    public void load(IocContainer iocContainer) {
        playerManager.getOnlinePlayers().forEach(this::setVanishMetaData);
    }

    private void setVanishMetaData(Player player) {
        player.setMetadata("vanished",
            new LazyMetadataValue(staffPlusPlus, LazyMetadataValue.CacheStrategy.NEVER_CACHE, () -> {
                IPlayerSession session = sessionManager.get(player);
                return session.getVanishType() == VanishType.TOTAL || session.getVanishType() == VanishType.LIST;
            }));
    }
}
