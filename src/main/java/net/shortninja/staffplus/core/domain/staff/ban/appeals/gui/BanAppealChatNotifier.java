package net.shortninja.staffplus.core.domain.staff.ban.appeals.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.appeals.BanAppealConfiguration;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.database.BansRepository;
import net.shortninja.staffplusplus.appeals.AppealApprovedEvent;
import net.shortninja.staffplusplus.appeals.AppealRejectedEvent;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.appeals.AppealedEvent;
import net.shortninja.staffplusplus.appeals.IAppeal;
import net.shortninja.staffplusplus.ban.appeals.BanAppealApprovedEvent;
import net.shortninja.staffplusplus.ban.appeals.BanAppealRejectedEvent;
import net.shortninja.staffplusplus.ban.appeals.BanAppealedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
@IocListener
public class BanAppealChatNotifier implements Listener {

    @ConfigProperty("commands:bans.manage.gui")
    public String commandManageBansGui;

    private final Messages messages;
    private final BanAppealConfiguration banAppealConfiguration;
    private final PlayerManager playerManager;
    private final BansRepository bansRepository;

    public BanAppealChatNotifier(Messages messages, BanAppealConfiguration banAppealConfiguration, PlayerManager playerManager, BansRepository bansRepository) {
        this.messages = messages;
        this.banAppealConfiguration = banAppealConfiguration;
        this.playerManager = playerManager;
        this.bansRepository = bansRepository;
    }

    @EventHandler
    public void handleAppealedEvent(AppealedEvent appealedEvent) {
        if (appealedEvent.getAppealable().getType() != AppealableType.BAN) {
            return;
        }

        Ban ban = (Ban) appealedEvent.getAppealable();
        ban.getAppeal().ifPresent(appeal -> {

            playerManager.getOnlinePlayer(appeal.getAppealerUuid())
                .ifPresent(appealer -> {
                    sendAppealedMessageToStaff(ban, appealer.getPlayer());
                    String message = messages.appealCreated.replace("%reason%", appeal.getReason());
                    this.messages.send(appealer.getPlayer(), message, messages.prefixBans);
                });

            sendEvent(new BanAppealedEvent(ban));
        });
    }

    @EventHandler
    public void handleAppealApproved(AppealApprovedEvent appealApprovedEvent) {
        if (appealApprovedEvent.getAppeal().getType() != AppealableType.BAN) {
            return;
        }

        IAppeal appeal = appealApprovedEvent.getAppeal();
        Ban ban = bansRepository.getBan(appeal.getAppealableId())
            .orElseThrow(() -> new BusinessException("No ban found."));

        sendMessageToPlayer(appeal.getAppealerUuid(), messages.appealApproved);
        sendMessageToPlayer(appeal.getResolverUuid(), messages.appealApprove);

        sendEvent(new BanAppealApprovedEvent(appeal, ban));
    }

    @EventHandler
    public void handleAppealRejected(AppealRejectedEvent appealRejectedEvent) {
        if (appealRejectedEvent.getAppeal().getType() != AppealableType.BAN) {
            return;
        }

        IAppeal appeal = appealRejectedEvent.getAppeal();
        Ban ban = bansRepository.getBan(appeal.getAppealableId())
            .orElseThrow(() -> new BusinessException("No ban found."));

        sendMessageToPlayer(appeal.getAppealerUuid(), messages.appealRejected);
        sendMessageToPlayer(appeal.getResolverUuid(), messages.appealReject);

        sendEvent(new BanAppealRejectedEvent(appeal, ban));
    }

    private void sendMessageToPlayer(UUID appealerUuid, String appealApproved) {
        playerManager.getOnlinePlayer(appealerUuid)
            .ifPresent(sppPlayer -> this.messages.send(sppPlayer.getPlayer(), appealApproved, messages.prefixBans));
    }

    private void sendAppealedMessageToStaff(Ban ban, Player appealer) {
        String manageBansCommand = commandManageBansGui + " " + ban.getTargetName();
        JSONMessage jsonMessage = JavaUtils.buildClickableMessage(appealer.getName() + " has appealed a ban",
            "View bans!",
            "Click to open the bans view",
            manageBansCommand, true);
        this.messages.sendGroupMessage(jsonMessage, banAppealConfiguration.permissionNotifications);
    }
}
