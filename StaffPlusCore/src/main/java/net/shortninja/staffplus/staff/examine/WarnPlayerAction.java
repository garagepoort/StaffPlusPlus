package net.shortninja.staffplus.staff.examine;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.warn.WarnService;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class WarnPlayerAction implements IAction {
    private static final String CANCEL = "cancel";
    private final Messages messages = IocContainer.getMessages();
    private final MessageCoordinator messageCoordinator = IocContainer.getMessage();
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final WarnService warnService = IocContainer.getWarnService();
    private final PlayerManager playerManager = IocContainer.getPlayerManager();

    private final Player targetPlayer;

    public WarnPlayerAction(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }


    @Override
    public void click(Player player, ItemStack item, int slot) {
        messageCoordinator.send(player, "&1=====================================================", messages.prefixGeneral);
        messageCoordinator.send(player, "&6         You have chosen to warn this player", messages.prefixGeneral);
        messageCoordinator.send(player, "&6Type your reason for warning this player in chat", messages.prefixGeneral);
        messageCoordinator.send(player, "&6        Type \"cancel\" to cancel the warning ", messages.prefixGeneral);
        messageCoordinator.send(player, "&1=====================================================", messages.prefixGeneral);

        String severityLevel = StaffPlus.get().versionProtocol.getNbtString(item);
        PlayerSession playerSession = sessionManager.get(player.getUniqueId());

        playerSession.setChatAction((player1, input) -> {
            if (input.equalsIgnoreCase(CANCEL)) {
                messageCoordinator.send(player, "&CYou have cancelled warning this player", messages.prefixWarnings);
                return;
            }

            Optional<SppPlayer> onOrOfflinePlayer = playerManager.getOnOrOfflinePlayer(targetPlayer.getUniqueId());
            if (!onOrOfflinePlayer.isPresent()) {
                messageCoordinator.send(player1, messages.playerOffline, messages.prefixGeneral);
            } else {
                warnService.sendWarning(player1, onOrOfflinePlayer.get(), input, severityLevel);
                messageCoordinator.send(player1, messages.inputAccepted, messages.prefixGeneral);
            }
        });
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
