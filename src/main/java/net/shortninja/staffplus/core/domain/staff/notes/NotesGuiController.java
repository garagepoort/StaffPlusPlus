package net.shortninja.staffplus.core.domain.staff.notes;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class NotesGuiController {

    private final OnlineSessionsManager sessionManager;
    private final PlayerSettingsRepository playerSettingsRepository;
    private final PlayerManager playerManager;
    private final Messages messages;
    private final BukkitUtils bukkitUtils;

    public NotesGuiController(OnlineSessionsManager sessionManager, PlayerSettingsRepository playerSettingsRepository, PlayerManager playerManager, Messages messages, BukkitUtils bukkitUtils) {
        this.sessionManager = sessionManager;
        this.playerSettingsRepository = playerSettingsRepository;
        this.playerManager = playerManager;
        this.messages = messages;
        this.bukkitUtils = bukkitUtils;
    }

    @GuiAction("manage-notes/create")
    public void createNote(Player staff, @GuiParam("targetPlayerName") String targetPlayerName) {
        SppPlayer targetPlayer = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
        OnlinePlayerSession playerSession = sessionManager.get(staff);

        messages.send(staff, messages.typeInput, messages.prefixGeneral);

        playerSession.setChatAction((player, input) ->
            bukkitUtils.runTaskAsync(player, () -> {
                playerSettingsRepository.get(targetPlayer.getOfflinePlayer()).addPlayerNote("&7" + input);
                messages.send(player, messages.inputAccepted, messages.prefixGeneral);
            }));
    }
}
