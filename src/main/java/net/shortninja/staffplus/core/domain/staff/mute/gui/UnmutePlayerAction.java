package net.shortninja.staffplus.core.domain.staff.mute.gui;

import net.shortninja.staffplus.core.StaffPlus;
import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UnmutePlayerAction implements IAction {
    private static final String CANCEL = "cancel";
    private final Messages messages = IocContainer.get(Messages.class);
    private final MessageCoordinator messageCoordinator = IocContainer.get(MessageCoordinator.class);
    private final SessionManagerImpl sessionManager = IocContainer.get(SessionManagerImpl.class);
    private final MuteService muteService = IocContainer.get(MuteService.class);

    @Override
    public void click(Player player, ItemStack item, int slot) {
        messageCoordinator.send(player, "&1=====================================================", messages.prefixGeneral);
        messageCoordinator.send(player, "&6         You have chosen to unmute this player", messages.prefixGeneral);
        messageCoordinator.send(player, "&6Type your reason for unmuting this player in chat", messages.prefixGeneral);
        messageCoordinator.send(player, "&6        Type \"cancel\" to cancel the unmute ", messages.prefixGeneral);
        messageCoordinator.send(player, "&1=====================================================", messages.prefixGeneral);

        int muteId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
        PlayerSession playerSession = sessionManager.get(player.getUniqueId());
        playerSession.setChatAction((player1, message) -> {
            if (message.equalsIgnoreCase(CANCEL)) {
                messageCoordinator.send(player, "&CYou have cancelled unmuting this player", messages.prefixReports);
                return;
            }
            muteService.unmute(player, muteId, message);
            sessionManager.get(player.getUniqueId()).setMuted(false);
        });
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
