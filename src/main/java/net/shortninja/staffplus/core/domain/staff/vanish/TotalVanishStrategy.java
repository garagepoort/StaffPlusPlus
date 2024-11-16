package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;

import java.util.stream.Stream;

@IocBean
@IocMultiProvider(VanishStrategy.class)
public class TotalVanishStrategy implements VanishStrategy {

    private final VanishConfiguration vanishConfiguration;
    private final PermissionHandler permission;
    private final OnlineSessionsManager sessionManager;
    private final PlayerManager playerManager;
    private final IProtocolService protocolService;

    public TotalVanishStrategy(VanishConfiguration vanishConfiguration,
                               PermissionHandler permission,
                               OnlineSessionsManager sessionManager,
                               PlayerManager playerManager,
                               IProtocolService protocolService) {
        this.vanishConfiguration = vanishConfiguration;
        this.permission = permission;
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
        this.protocolService = protocolService;
    }

    @Override
    public void vanish(SppPlayer player) {
        Bukkit.getOnlinePlayers().stream()
            .filter(p -> !permission.has(p, vanishConfiguration.permissionSeeVanished))
            .forEach(p -> p.hidePlayer(player.getPlayer()));
        listVanish(player, true);
        
        // Cancel existing targets of mobs
        int mobActivationRange = Bukkit.getServer().spigot().getConfig().getInt("world-settings.default.entity-activation-range.monsters");
        player.getPlayer().getWorld().getNearbyEntities(player.getPlayer().getLocation(), mobActivationRange, mobActivationRange, mobActivationRange).forEach(entity -> {
            if (!(entity instanceof Mob)) return;
            Mob mob = (Mob) entity;
            if (mob.getTarget() != player.getPlayer()) return;
            mob.setTarget(null);
       });
    }

    @Override
    public void updateVanish(SppPlayer player) {
        if (!permission.has(player.getPlayer(), vanishConfiguration.permissionSeeVanished)) {
            sessionManager.getAll().stream()
                .filter(s -> s.getVanishType() == VanishType.TOTAL)
                .map(s -> playerManager.getOnlinePlayer(s.getUuid()))
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .map(SppPlayer::getPlayer)
                .forEach(p -> {
                    player.getPlayer().hidePlayer(p.getPlayer());
                    listVanish(player, true);
                });
        }
    }

    private void listVanish(SppPlayer player, boolean enabled) {
        if (vanishConfiguration.vanishTabList) {
            protocolService.getVersionProtocol().listVanish(player.getPlayer(), enabled);
        }
    }

    @Override
    public void unvanish(SppPlayer player) {
        listVanish(player, false);
        Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(player.getPlayer()));
    }

    @Override
    public VanishType getVanishType() {
        return VanishType.TOTAL;
    }
}
