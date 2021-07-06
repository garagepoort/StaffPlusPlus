package net.shortninja.staffplus.core.domain.staff.ban.playerbans.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class UnbanPlayerAction implements IAction {
    private static final String CANCEL = "cancel";
    private final Messages messages = StaffPlus.get().getIocContainer().get(Messages.class);

    private final SessionManagerImpl sessionManager = StaffPlus.get().getIocContainer().get(SessionManagerImpl.class);
    private final BanService banService = StaffPlus.get().getIocContainer().get(BanService.class);

    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        messages.send(player, "&1=====================================================", messages.prefixGeneral);
        messages.send(player, "&6         You have chosen to unban this player", messages.prefixGeneral);
        messages.send(player, "&6Type your reason for unbanning this player in chat", messages.prefixGeneral);
        messages.send(player, "&6        Type \"cancel\" to cancel the unban ", messages.prefixGeneral);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);

        int banId = Integer.parseInt(StaffPlus.get().getIocContainer().get(IProtocolService.class).getVersionProtocol().getNbtString(item));
        PlayerSession playerSession = sessionManager.get(player.getUniqueId());
        playerSession.setChatAction((player1, message) -> {
            if (message.equalsIgnoreCase(CANCEL)) {
                messages.send(player, "&CYou have cancelled unbanning this player", messages.prefixReports);
                return;
            }
            banService.unban(player, banId, message);
        });
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
