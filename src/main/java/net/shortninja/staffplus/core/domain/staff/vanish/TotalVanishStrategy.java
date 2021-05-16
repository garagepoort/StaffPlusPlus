package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@IocBean
@IocMultiProvider(VanishStrategy.class)
public class TotalVanishStrategy implements VanishStrategy {

    private final Options options;
    private final PermissionHandler permission;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;

    public TotalVanishStrategy(Options options, PermissionHandler permission, Messages messages, SessionManagerImpl sessionManager) {
        this.options = options;
        this.permission = permission;
        this.messages = messages;
        this.sessionManager = sessionManager;
    }

    @Override
    public void vanish(Player player) {
        Bukkit.getOnlinePlayers().stream()
            .filter(p -> !permission.has(p, options.permissionSeeVanished))
            .forEach(p -> p.hidePlayer(player));

        String message = messages.totalVanish.replace("%status%", messages.enabled);
        if (StringUtils.isNotEmpty(message)) {
            this.messages.send(player, message, messages.prefixGeneral);
        }
    }

    @Override
    public void updateVanish(Player player) {
        if(!permission.has(player, options.permissionSeeVanished)) {
            sessionManager.getAll().stream()
                .filter(session -> session.getPlayer().isPresent() && session.getVanishType() == VanishType.TOTAL)
                .forEach(p -> player.hidePlayer(p.getPlayer().get()));
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
