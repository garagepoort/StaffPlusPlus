package net.shortninja.staffplus.core.domain.staff.warn.appeals.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.AppealService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class AppealReasonChatAction implements IAction {
    private static final String CANCEL = "cancel";
    private final Messages messages = StaffPlus.get().getIocContainer().get(Messages.class);

    private final SessionManagerImpl sessionManager = StaffPlus.get().getIocContainer().get(SessionManagerImpl.class);
    private final AppealService appealService = StaffPlus.get().getIocContainer().get(AppealService.class);

    private final Warning warning;

    public AppealReasonChatAction(Warning warning) {
        this.warning = warning;
    }

    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        messages.send(player, "&1=====================================================", messages.prefixGeneral);
        messages.send(player, "&6         You have chosen to appeal this warning", messages.prefixGeneral);
        messages.send(player, "&6            Type your appeal reason in chat", messages.prefixGeneral);
        messages.send(player, "&6         Type \"cancel\" to cancel appealing ", messages.prefixGeneral);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);

        PlayerSession playerSession = sessionManager.get(player.getUniqueId());

        playerSession.setChatAction((player1, input) -> {
            if (input.equalsIgnoreCase(CANCEL)) {
                messages.send(player, "&CYou have cancelled your appeal", messages.prefixWarnings);
                return;
            }

            appealService.addAppeal(player, warning, input);
        });
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
