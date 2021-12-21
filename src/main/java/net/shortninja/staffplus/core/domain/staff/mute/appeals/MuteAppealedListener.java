package net.shortninja.staffplus.core.domain.staff.mute.appeals;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.mute.database.MuteRepository;
import net.shortninja.staffplusplus.appeals.AppealApprovedEvent;
import net.shortninja.staffplusplus.appeals.AppealRejectedEvent;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.appeals.AppealedEvent;
import net.shortninja.staffplusplus.appeals.IAppeal;
import net.shortninja.staffplusplus.mute.appeals.MuteAppealApprovedEvent;
import net.shortninja.staffplusplus.mute.appeals.MuteAppealRejectedEvent;
import net.shortninja.staffplusplus.mute.appeals.MuteAppealedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
@IocListener
public class MuteAppealedListener implements Listener {

    @ConfigProperty("commands:mutes.manage.gui")
    public String commandManageMutesGui;

    private final Messages messages;
    private final MuteAppealConfiguration muteAppealConfiguration;
    private final PlayerManager playerManager;
    private final MuteRepository muteRepository;

    public MuteAppealedListener(Messages messages, MuteAppealConfiguration muteAppealConfiguration, PlayerManager playerManager, MuteRepository muteRepository) {
        this.messages = messages;
        this.muteAppealConfiguration = muteAppealConfiguration;
        this.playerManager = playerManager;
        this.muteRepository = muteRepository;
    }

    @EventHandler
    public void handleAppealedEvent(AppealedEvent appealedEvent) {
        if (appealedEvent.getAppealable().getType() != AppealableType.MUTE) {
            return;
        }

        Mute mute = (Mute) appealedEvent.getAppealable();
        mute.getAppeal().ifPresent(appeal -> {

            playerManager.getOnlinePlayer(appeal.getAppealerUuid())
                .ifPresent(appealer -> {
                    sendAppealedMessageToStaff(mute, appealer.getPlayer());
                    String message = messages.appealCreated.replace("%reason%", appeal.getReason());
                    this.messages.send(appealer.getPlayer(), message, messages.prefixGeneral);
                });

            sendEvent(new MuteAppealedEvent(mute));
        });
    }

    @EventHandler
    public void handleAppealApproved(AppealApprovedEvent appealApprovedEvent) {
        if (appealApprovedEvent.getAppeal().getType() != AppealableType.MUTE) {
            return;
        }

        IAppeal appeal = appealApprovedEvent.getAppeal();
        Mute mute = muteRepository.getMute(appeal.getAppealableId())
            .orElseThrow(() -> new BusinessException("No mute found."));

        sendMessageToPlayer(appeal.getAppealerUuid(), messages.appealApproved);
        sendMessageToPlayer(appeal.getResolverUuid(), messages.appealApprove);

        sendEvent(new MuteAppealApprovedEvent(appeal, mute));
    }

    @EventHandler
    public void handleAppealRejected(AppealRejectedEvent appealRejectedEvent) {
        if (appealRejectedEvent.getAppeal().getType() != AppealableType.MUTE) {
            return;
        }

        IAppeal appeal = appealRejectedEvent.getAppeal();
        Mute mute = muteRepository.getMute(appeal.getAppealableId())
            .orElseThrow(() -> new BusinessException("No mute found."));

        sendMessageToPlayer(appeal.getAppealerUuid(), messages.appealRejected);
        sendMessageToPlayer(appeal.getResolverUuid(), messages.appealReject);

        sendEvent(new MuteAppealRejectedEvent(appeal, mute));
    }

    private void sendMessageToPlayer(UUID appealerUuid, String appealApproved) {
        playerManager.getOnlinePlayer(appealerUuid)
            .ifPresent(sppPlayer -> this.messages.send(sppPlayer.getPlayer(), appealApproved, messages.prefixGeneral));
    }

    private void sendAppealedMessageToStaff(Mute mute, Player appealer) {
        String manageMutesCommand = commandManageMutesGui + " " + mute.getTargetName();
        JSONMessage jsonMessage = JavaUtils.buildClickableMessage(appealer.getName() + " has appealed a mute",
            "View mutes!",
            "Click to open the mutes view",
            manageMutesCommand, true);
        this.messages.sendGroupMessage(jsonMessage, muteAppealConfiguration.permissionNotifications);
    }
}
