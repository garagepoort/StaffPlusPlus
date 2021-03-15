package net.shortninja.staffplus.staff.ban.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplus.staff.ban.BanService;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UnbanPlayerAction implements IAction {
    private static final String CANCEL = "cancel";
    private final Messages messages = IocContainer.getMessages();
    private final MessageCoordinator messageCoordinator = IocContainer.getMessage();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();
    private final BanService banService = IocContainer.getBanService();

    @Override
    public void click(Player player, ItemStack item, int slot) {
        messageCoordinator.send(player, "&1=====================================================", messages.prefixGeneral);
        messageCoordinator.send(player, "&6         You have chosen to unban this player", messages.prefixGeneral);
        messageCoordinator.send(player, "&6Type your reason for unbanning this player in chat", messages.prefixGeneral);
        messageCoordinator.send(player, "&6        Type \"cancel\" to cancel the unban ", messages.prefixGeneral);
        messageCoordinator.send(player, "&1=====================================================", messages.prefixGeneral);

        int banId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
        PlayerSession playerSession = sessionManager.get(player.getUniqueId());
        playerSession.setChatAction((player1, message) -> {
            if (message.equalsIgnoreCase(CANCEL)) {
                messageCoordinator.send(player, "&CYou have cancelled unbanning this player", messages.prefixReports);
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
