package net.shortninja.staffplus.core.domain.staff.warn.warnings;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.config.AppealConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.ManageWarningsConfiguration;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.appeals.AppealedEvent;
import net.shortninja.staffplusplus.warnings.IWarning;
import net.shortninja.staffplusplus.warnings.WarningAppealedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
@IocListener
public class WarningAppealedListener implements Listener {

    private final ManageWarningsConfiguration manageWarningsConfiguration;
    private final Messages messages;
    private final AppealConfiguration appealConfiguration;
    private final PlayerManager playerManager;

    public WarningAppealedListener(ManageWarningsConfiguration manageWarningsConfiguration, Messages messages, AppealConfiguration appealConfiguration, PlayerManager playerManager) {
        this.manageWarningsConfiguration = manageWarningsConfiguration;
        this.messages = messages;
        this.appealConfiguration = appealConfiguration;
        this.playerManager = playerManager;
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

    private void sendAppealedMessageToStaff(IWarning warning, Player appealer) {
        String manageWarningsCommand = manageWarningsConfiguration.commandManageWarningsGui + " " + warning.getTargetName();
        JSONMessage jsonMessage = JavaUtils.buildClickableMessage(appealer.getName() + " has appealed a warning",
            "View warnings!",
            "Click to open the warnings view",
            manageWarningsCommand, true);
        this.messages.sendGroupMessage(jsonMessage, appealConfiguration.permissionNotifications);
    }
}
