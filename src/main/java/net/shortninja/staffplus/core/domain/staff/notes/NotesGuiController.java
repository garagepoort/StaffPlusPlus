package net.shortninja.staffplus.core.domain.staff.notes;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class NotesGuiController {

    private final SessionManagerImpl sessionManager;
    private final PlayerManager playerManager;
    private final Messages messages;

    public NotesGuiController(SessionManagerImpl sessionManager, PlayerManager playerManager, Messages messages) {
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
        this.messages = messages;
    }

    @GuiAction("manage-notes/create")
    public void createNote(Player staff, @GuiParam("targetPlayerName") String targetPlayerName) {
        SppPlayer targetPlayer = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
        PlayerSession playerSession = sessionManager.get(staff.getUniqueId());

        messages.send(staff, messages.typeInput, messages.prefixGeneral);

        playerSession.setChatAction((player12, input) -> {
            sessionManager.get(targetPlayer.getId()).addPlayerNote("&7" + input);
            messages.send(player12, messages.inputAccepted, messages.prefixGeneral);
        });
    }
}
