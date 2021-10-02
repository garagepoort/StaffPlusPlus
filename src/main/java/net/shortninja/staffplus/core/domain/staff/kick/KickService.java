package net.shortninja.staffplus.core.domain.staff.kick;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.infractions.Infraction;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionInfo;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionProvider;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.core.domain.staff.kick.database.KicksRepository;
import net.shortninja.staffplusplus.kick.KickEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
@IocMultiProvider(InfractionProvider.class)
public class KickService implements InfractionProvider {

    private final PermissionHandler permission;
    private final KicksRepository kicksRepository;
    private final Options options;

    public KickService(PermissionHandler permission, KicksRepository kicksRepository, Options options) {
        this.permission = permission;
        this.kicksRepository = kicksRepository;
        this.options = options;
    }

    public void kick(CommandSender issuer, SppPlayer playerToKick, String reason) {
        if (playerToKick.isOnline() && permission.has(playerToKick.getPlayer(), options.kickConfiguration.getPermissionKickByPass())) {
            throw new BusinessException("&CThis player bypasses being kicked");
        }
        if (!playerToKick.isOnline()) {
            throw new BusinessException("&CCannot kick offline player");
        }

        String issuerName = issuer instanceof Player ? issuer.getName() : "Console";
        UUID issuerUuid = issuer instanceof Player ? ((Player) issuer).getUniqueId() : CONSOLE_UUID;

        Kick kick = new Kick(reason, issuerName, issuerUuid, playerToKick.getUsername(), playerToKick.getId());
        kick.setId(kicksRepository.addKick(kick));

        sendEvent(new KickEvent(kick));
    }

    @Override
    public List<? extends Infraction> getInfractions(Player executor, UUID playerUUID) {
        if (!options.infractionsConfiguration.isShowKicks()) {
            return Collections.emptyList();
        }
        return kicksRepository.getKicksForPlayer(playerUUID);
    }

    @Override
    public Optional<InfractionInfo> getInfractionsInfo() {
        if (!options.infractionsConfiguration.isShowKicks()) {
            return Optional.empty();
        }
        return Optional.of(new InfractionInfo(InfractionType.KICK, kicksRepository.getCountByPlayer()));
    }

    @Override
    public InfractionType getType() {
        return InfractionType.KICK;
    }
}
