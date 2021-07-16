package net.shortninja.staffplus.core.domain.staff.warn.appeals.gui.actions;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.AppealService;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class RejectAppealAction implements IAction {
    private static final String CANCEL = "cancel";
    private final Messages messages = StaffPlus.get().getIocContainer().get(Messages.class);

    private final SessionManagerImpl sessionManager = StaffPlus.get().getIocContainer().get(SessionManagerImpl.class);
    private final AppealService appealService = StaffPlus.get().getIocContainer().get(AppealService.class);
    private final Options options = StaffPlus.get().getIocContainer().get(Options.class);

    private final int appealId;

    public RejectAppealAction(int appealId) {
        this.appealId = appealId;
    }

    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        if (options.appealConfiguration.resolveReasonEnabled) {
            messages.send(player, "&1==================================================", messages.prefixWarnings);
            messages.send(player, "&6        You have chosen to reject this appeal", messages.prefixWarnings);
            messages.send(player, "&6Type your closing reason in chat to reject the appeal", messages.prefixWarnings);
            messages.send(player, "&6        Type \"cancel\" to cancel closing the appeal ", messages.prefixWarnings);
            messages.send(player, "&1==================================================", messages.prefixWarnings);
            PlayerSession playerSession = sessionManager.get(player.getUniqueId());
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled rejecting this appeal", messages.prefixWarnings);
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
