package net.shortninja.staffplus.core.domain.staff.playernotes.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.GuiActionService;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.playernotes.PlayerNote;
import net.shortninja.staffplus.core.domain.staff.playernotes.PlayerNoteService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.BOTH;

@Command(
    command = "commands:notes",
    permissions = "permissions:player-notes.view",
    description = "View a player's notes",
    usage = "[player]",
    playerRetrievalStrategy = BOTH
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class NotesCmd extends AbstractCmd {

    private static final String LIST_PARAM = "-l";
    private final PlayerNoteService playerNoteService;
    private final BukkitUtils bukkitUtils;
    private final GuiActionService guiActionService;
    private final PlayerManager playerManager;

    public NotesCmd(PermissionHandler permissionHandler,
                    Messages messages,
                    CommandService commandService,
                    PlayerNoteService playerNoteService,
                    BukkitUtils bukkitUtils, GuiActionService guiActionService, PlayerManager playerManager) {
        super(messages, permissionHandler, commandService);
        this.playerNoteService = playerNoteService;
        this.bukkitUtils = bukkitUtils;
        this.guiActionService = guiActionService;
        this.playerManager = playerManager;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {
        if (!(sender instanceof Player) || optionalParameters.containsKey(LIST_PARAM)) {
            bukkitUtils.runTaskAsync(sender, () -> listNotes(sender, playerNoteService.getAllPlayerNotes(sender, targetPlayer.getId(), 0, 100), targetPlayer));
        } else {
            guiActionService.executeAction((Player) sender, "player-notes/view/overview?targetPlayerName=" + targetPlayer.getUsername());
        }
        return true;
    }

    @Override
    protected List<String> getOptionalParameters() {
        return Collections.singletonList(LIST_PARAM);
    }

    private void listNotes(CommandSender sender, List<PlayerNote> notes, SppPlayer player) {

        for (String message : messages.noteListStart) {
            this.messages.send(sender, message.replace("%longline%", this.messages.LONG_LINE).replace("%target%", player.getUsername()).replace("%notes%", Integer.toString(notes.size())), message.contains("%longline%") ? "" : messages.prefixGeneral);
        }

        for (int i = 0, notesSize = notes.size(); i < notesSize; i++) {
            if (notes.get(i).isPrivateNote()) {
                messages.send(sender, messages.noteListEntryPrivate.replace("%count%", Integer.toString(i + 1)).replace("%note%", notes.get(i).getNote()), messages.prefixGeneral);
            } else {
                messages.send(sender, messages.noteListEntry.replace("%count%", Integer.toString(i + 1)).replace("%note%", notes.get(i).getNote()), messages.prefixGeneral);
            }
        }

        for (String message : messages.noteListEnd) {
            this.messages.send(sender, message.replace("%longline%", this.messages.LONG_LINE).replace("%target%", player.getUsername()).replace("%notes%", Integer.toString(notes.size())), message.contains("%longline%") ? "" : messages.prefixGeneral);
        }
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.of(args[0]);
    }

    @Override
    protected List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {
        String currentArg = args.length > 0 ? args[args.length - 1] : "";

        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}