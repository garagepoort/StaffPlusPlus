package net.shortninja.staffplus.core.domain.staff.ban.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.gui.IAction;

import net.shortninja.staffplus.core.domain.staff.ban.BanService;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UnbanPlayerAction implements IAction {
    private static final String CANCEL = "cancel";
    private final Messages messages = StaffPlus.get().iocContainer.get(Messages.class);

    private final SessionManagerImpl sessionManager = StaffPlus.get().iocContainer.get(SessionManagerImpl.class);
    private final BanService banService = StaffPlus.get().iocContainer.get(BanService.class);

    @Override
    public void click(Player player, ItemStack item, int slot) {
        messages.send(player, "&1=====================================================", messages.prefixGeneral);
        messages.send(player, "&6         You have chosen to unban this player", messages.prefixGeneral);
        messages.send(player, "&6Type your reason for unbanning this player in chat", messages.prefixGeneral);
        messages.send(player, "&6        Type \"cancel\" to cancel the unban ", messages.prefixGeneral);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);

        int banId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
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
