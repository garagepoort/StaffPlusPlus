package net.shortninja.staffplus.core.domain.staff.kick;

import net.shortninja.staffplus.core.StaffPlus;
import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.infractions.Infraction;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionInfo;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionProvider;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.core.domain.staff.kick.database.KicksRepository;
import net.shortninja.staffplusplus.kick.KickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
@IocMultiProvider(InfractionProvider.class)
public class KickService implements InfractionProvider {

    private final PermissionHandler permission;
    private final KicksRepository kicksRepository;
    private final Options options;
    private MessageCoordinator messageCoordinator;
    private Messages messages;

    public KickService(PermissionHandler permission, KicksRepository kicksRepository, Options options, MessageCoordinator messageCoordinator, Messages messages) {
        this.messageCoordinator = messageCoordinator;
        this.permission = permission;
        this.kicksRepository = kicksRepository;
        this.options = options;
        this.messages = messages;
    }

    public void kick(CommandSender issuer, SppPlayer playerToKick, String reason) {
        if (playerToKick.isOnline() && permission.has(playerToKick.getPlayer(), options.kickConfiguration.getPermissionKickByPass())) {
            throw new BusinessException("&CThis player bypasses being kicked");
        }
        if (!playerToKick.isOnline()) {
            throw new BusinessException("&CCannot kick offline player");
        }

        String issuerName = issuer instanceof Player ? issuer.getName() : "Console";
        UUID issuerUuid = issuer instanceof Player ? ((Player) issuer).getUniqueId() : StaffPlus.get().consoleUUID;

        Kick kick = new Kick(reason, issuerName, issuerUuid, playerToKick.getUsername(), playerToKick.getId());
        kick.setId(kicksRepository.addKick(kick));

        notifyPlayers(playerToKick, issuerName, reason);
        kickPlayer(playerToKick, issuerName, reason);
        sendEvent(new KickEvent(kick));
    }

    private void kickPlayer(SppPlayer playerToKick, String issuerName, String reason) {
        String message = messages.kickMessage
            .replace("%target%", playerToKick.getUsername())
            .replace("%issuer%", issuerName)
            .replace("%reason%", reason);
        playerToKick.getPlayer().kickPlayer(this.messageCoordinator.colorize(message));
    }

    private void notifyPlayers(SppPlayer playerToKick, String issuerName, String reason) {
        String message = messages.kickedNotify
            .replace("%target%", playerToKick.getUsername())
            .replace("%issuer%", issuerName)
            .replace("%reason%", reason);
        this.messageCoordinator.sendGlobalMessage(message, messages.prefixGeneral);
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
