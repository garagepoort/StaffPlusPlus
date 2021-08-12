package net.shortninja.staffplus.core.domain.staff.notes;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.Optional;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.ONLINE;

@Command(
    command = "commands:notes",
    permissions = "permissions:examine",
    description = "Adds or manages a player's notes",
    usage = "[player] [note]",
    playerRetrievalStrategy = ONLINE
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class NotesCmd extends AbstractCmd {
    private static final String GET = "get";
    private static final String CLEAR = "clear";

    private final NoteService noteService;
    private final BukkitUtils bukkitUtils;

    public NotesCmd(PermissionHandler permissionHandler,
                    Messages messages,
                    CommandService commandService,
                    NoteService noteService, BukkitUtils bukkitUtils) {
        super(messages, permissionHandler, commandService);
        this.noteService = noteService;
        this.bukkitUtils = bukkitUtils;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {
        bukkitUtils.runTaskAsync(sender, () -> {
            if (args.length == 2) {
                String argument = args[0];

                if (argument.equalsIgnoreCase(GET)) {
                    noteService.listNotes(sender, targetPlayer.getPlayer());
                } else if (argument.equalsIgnoreCase(CLEAR)) {
                    noteService.clearNotes(sender, targetPlayer.getPlayer());
                } else {
                    noteService.addPlayerNote(sender, targetPlayer.getPlayer(), JavaUtils.compileWords(args, 1));
                }
            } else if (args.length >= 3) {
                noteService.addPlayerNote(sender, targetPlayer.getPlayer(), JavaUtils.compileWords(args, 1));
            } else {
                sendHelp(sender);
            }
        });
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 2;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args.length == 2) {
            if ((args[0].equalsIgnoreCase(GET) || args[0].equalsIgnoreCase(CLEAR))) {
                return Optional.of(args[1]);
            }
        }
        return Optional.of(args[0]);
    }

    private void sendHelp(CommandSender sender) {
        messages.send(sender, "&7" + messages.LONG_LINE, "");
        messages.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
        messages.send(sender, "&b/" + getName() + " get &7[player]", messages.prefixGeneral);
        messages.send(sender, "&b/" + getName() + " clear &7[player]", messages.prefixGeneral);
        messages.send(sender, "&7" + messages.LONG_LINE, "");
    }
}