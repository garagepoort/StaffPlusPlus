package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

@IocBean
@IocMultiProvider(VanishStrategy.class)
public class TotalVanishStrategy implements VanishStrategy {

    private final VanishConfiguration vanishConfiguration;
    private final PermissionHandler permission;
    private final Messages messages;
    private final OnlineSessionsManager sessionManager;
    private final PlayerManager playerManager;

    public TotalVanishStrategy(VanishConfiguration vanishConfiguration, PermissionHandler permission, Messages messages, OnlineSessionsManager sessionManager, PlayerManager playerManager) {
        this.vanishConfiguration = vanishConfiguration;
        this.permission = permission;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
    }

    @Override
    public void vanish(Player player) {
        Bukkit.getOnlinePlayers().stream()
            .filter(p -> !permission.has(p, vanishConfiguration.permissionSeeVanished))
            .forEach(p -> p.hidePlayer(player));

        String message = messages.totalVanish.replace("%status%", messages.enabled);
        if (StringUtils.isNotEmpty(message)) {
            this.messages.send(player, message, messages.prefixGeneral);
        }
    }

    @Override
    public void updateVanish(Player player) {
        if (!permission.has(player, vanishConfiguration.permissionSeeVanished)) {
            sessionManager.getAll().stream()
                .filter(s -> s.getVanishType() == VanishType.TOTAL)
                .map(s -> playerManager.getOnlinePlayer(s.getUuid()))
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .map(SppPlayer::getPlayer)
                .forEach(p -> player.hidePlayer(p.getPlayer()));
        }
    }

    @Override
    public void unvanish(Player player) {
        Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(player));

        String message = messages.totalVanish.replace("%status%", messages.disabled);
        if (StringUtils.isNotEmpty(message)) {
            this.messages.send(player, message, messages.prefixGeneral);
        }
    }

    @Override
    public VanishType getVanishType() {
        return VanishType.TOTAL;
    }

}
