package net.shortninja.staffplus.core.domain.staff.warn.warnings;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.config.WarningAppealConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.ManageWarningsConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.database.WarnRepository;
import net.shortninja.staffplusplus.appeals.AppealApprovedEvent;
import net.shortninja.staffplusplus.appeals.AppealRejectedEvent;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.appeals.AppealedEvent;
import net.shortninja.staffplusplus.appeals.IAppeal;
import net.shortninja.staffplusplus.warnings.WarningAppealApprovedEvent;
import net.shortninja.staffplusplus.warnings.WarningAppealRejectedEvent;
import net.shortninja.staffplusplus.warnings.WarningAppealedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
@IocListener
public class WarningAppealedListener implements Listener {

    private final ManageWarningsConfiguration manageWarningsConfiguration;
    private final Messages messages;
    private final WarningAppealConfiguration warningAppealConfiguration;
    private final PlayerManager playerManager;
    private final WarnRepository warnRepository;

    public WarningAppealedListener(ManageWarningsConfiguration manageWarningsConfiguration, Messages messages, WarningAppealConfiguration warningAppealConfiguration, PlayerManager playerManager, WarnRepository warnRepository) {
        this.manageWarningsConfiguration = manageWarningsConfiguration;
        this.messages = messages;
        this.warningAppealConfiguration = warningAppealConfiguration;
        this.playerManager = playerManager;
        this.warnRepository = warnRepository;
    }

    @EventHandler
    public void handleAppealedEvent(AppealedEvent appealedEvent) {
        if (appealedEvent.getAppealable().getType() != AppealableType.WARNING) {
            return;
        }

        Warning warning = (Warning) appealedEvent.getAppealable();
        warning.getAppeal().ifPresent(appeal -> {

            playerManager.getOnlinePlayer(appeal.getAppealerUuid())
                .ifPresent(appealer -> {
                    sendAppealedMessageToStaff(warning, appealer.getPlayer());
                    String message = messages.appealCreated.replace("%reason%", appeal.getReason());
                    this.messages.send(appealer.getPlayer(), message, messages.prefixWarnings);
                });

            sendEvent(new WarningAppealedEvent(warning));
        });
    }

    @EventHandler
    public void handleAppealApproved(AppealApprovedEvent appealApprovedEvent) {
        if (appealApprovedEvent.getAppeal().getType() != AppealableType.WARNING) {
            return;
        }

        IAppeal appeal = appealApprovedEvent.getAppeal();
        Warning warning = warnRepository.findWarning(appeal.getAppealableId())
            .orElseThrow(() -> new BusinessException("No warning found."));

        sendMessageToPlayer(appeal.getAppealerUuid(), messages.appealApproved);
        sendMessageToPlayer(appeal.getResolverUuid(), messages.appealApprove);

        sendEvent(new WarningAppealApprovedEvent(appeal, warning));
    }

    @EventHandler
    public void handleAppealRejected(AppealRejectedEvent appealRejectedEvent) {
        if (appealRejectedEvent.getAppeal().getType() != AppealableType.WARNING) {
            return;
        }

        IAppeal appeal = appealRejectedEvent.getAppeal();
        Warning warning = warnRepository.findWarning(appeal.getAppealableId())
            .orElseThrow(() -> new BusinessException("No warning found."));

        sendMessageToPlayer(appeal.getAppealerUuid(), messages.appealRejected);
        sendMessageToPlayer(appeal.getResolverUuid(), messages.appealReject);

        sendEvent(new WarningAppealRejectedEvent(appeal, warning));
    }

    private void sendMessageToPlayer(UUID appealerUuid, String appealApproved) {
        playerManager.getOnlinePlayer(appealerUuid)
            .ifPresent(sppPlayer -> this.messages.send(sppPlayer.getPlayer(), appealApproved, messages.prefixWarnings));
    }

    private void sendAppealedMessageToStaff(Warning warning, Player appealer) {
        String manageWarningsCommand = manageWarningsConfiguration.commandManageWarningsGui + " " + warning.getTargetName();
        JSONMessage jsonMessage = JavaUtils.buildClickableMessage(appealer.getName() + " has appealed a warning",
            "View warnings!",
            "Click to open the warnings view",
            manageWarningsCommand, true);
        this.messages.sendGroupMessage(jsonMessage, warningAppealConfiguration.permissionNotifications);
    }
}
