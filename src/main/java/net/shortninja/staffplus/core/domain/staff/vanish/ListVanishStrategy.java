package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SessionManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

@IocBean
@IocMultiProvider(VanishStrategy.class)
public class ListVanishStrategy implements VanishStrategy {

    private final VanishConfiguration vanishConfiguration;
    private final IProtocolService protocolService;
    private final PermissionHandler permission;
    private final SessionManager sessionManager;
    private final PlayerManager playerManager;

    public ListVanishStrategy(VanishConfiguration vanishConfiguration,
                              IProtocolService protocolService,
                              PermissionHandler permission,
                              SessionManager sessionManager,
                              PlayerManager playerManager) {
        this.vanishConfiguration = vanishConfiguration;
        this.protocolService = protocolService;
        this.permission = permission;
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
    }

    @Override
    public void vanish(Player player) {
        if (vanishConfiguration.vanishTabList) {
            protocolService.getVersionProtocol().listVanish(player, true);
        }
    }

    @Override
    public void unvanish(Player player) {
        protocolService.getVersionProtocol().listVanish(player, false);
    }

    @Override
    public void updateVanish(Player player) {
        if (!permission.has(player, vanishConfiguration.permissionSeeVanished)) {
            sessionManager.getAll().stream()
                .filter(s -> s.getVanishType() == VanishType.LIST)
                .map(s -> playerManager.getOnlinePlayer(s.getUuid()))
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .map(SppPlayer::getPlayer)
                .forEach(p -> protocolService.getVersionProtocol().listVanish(p.getPlayer(), true));
        }
    }

    @Override
    public VanishType getVanishType() {
        return VanishType.LIST;
    }
}
