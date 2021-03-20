package net.shortninja.staffplus.domain.staff.warn.appeals.gui.actions;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplus.domain.staff.warn.appeals.AppealService;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RejectAppealAction implements IAction {
    private static final String CANCEL = "cancel";
    private final Messages messages = IocContainer.getMessages();
    private final MessageCoordinator messageCoordinator = IocContainer.getMessage();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();
    private final AppealService appealService = IocContainer.getAppealService();
    private final Options options = IocContainer.getOptions();

    private final int appealId;

    public RejectAppealAction(int appealId) {
        this.appealId = appealId;
    }

    @Override
    public void click(Player player, ItemStack item, int slot) {
        if (options.appealConfiguration.isResolveReasonEnabled()) {
            messageCoordinator.send(player, "&1==================================================", messages.prefixWarnings);
            messageCoordinator.send(player, "&6        You have chosen to reject this appeal", messages.prefixWarnings);
            messageCoordinator.send(player, "&6Type your closing reason in chat to reject the appeal", messages.prefixWarnings);
            messageCoordinator.send(player, "&6        Type \"cancel\" to cancel closing the appeal ", messages.prefixWarnings);
            messageCoordinator.send(player, "&1==================================================", messages.prefixWarnings);
            PlayerSession playerSession = sessionManager.get(player.getUniqueId());
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messageCoordinator.send(player, "&CYou have cancelled rejecting this appeal", messages.prefixWarnings);
                    return;
                }
                appealService.rejectAppeal(player, appealId, message);
            });
        } else {
            appealService.rejectAppeal(player, appealId);
        }
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
