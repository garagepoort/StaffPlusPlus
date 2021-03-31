package net.shortninja.staffplus.core.domain.staff.mode.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;

import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.ONLINE;

@IocBean
@IocMultiProvider(SppCommand.class)
public class NotesCmd extends AbstractCmd {
    private static final String GET = "get";
    private static final String CLEAR = "clear";

    private final SessionManagerImpl sessionManager;
    private final PermissionHandler permissionHandler;

    public NotesCmd(PermissionHandler permissionHandler, Messages messages, Options options, SessionManagerImpl sessionManager, CommandService commandService) {
        super(options.commandNotes, messages, options, commandService);
        this.sessionManager = sessionManager;
        this.permissionHandler = permissionHandler;
        setPermission(options.examineConfiguration.getPermissionExamine());
        setDescription("Adds or manages a player's notes");
        setUsage("[player] [note]");
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
        if (args.length == 2) {
            String argument = args[0];
            boolean hasPermission = permissionHandler.has(sender, options.examineConfiguration.getPermissionExamine());

            if (argument.equalsIgnoreCase(GET) && hasPermission) {
                listNotes(sender, targetPlayer.getPlayer());
            } else if (argument.equalsIgnoreCase(CLEAR) && hasPermission) {
                clearNotes(sender, targetPlayer.getPlayer());
            } else {
                addNote(sender, targetPlayer.getPlayer(), JavaUtils.compileWords(args, 1));
            }
        } else if (args.length >= 3) {
            addNote(sender, targetPlayer.getPlayer(), JavaUtils.compileWords(args, 1));
        } else {
            sendHelp(sender);
        }

        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 2;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return ONLINE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args.length == 2) {
            boolean hasPermission = permissionHandler.has(sender, options.examineConfiguration.getPermissionExamine());
            if ((args[0].equalsIgnoreCase(GET) || args[0].equalsIgnoreCase(CLEAR)) && hasPermission) {
                return Optional.of(args[1]);
            }
        }
        return Optional.of(args[0]);
    }

    private void listNotes(CommandSender sender, Player player) {
        PlayerSession user = sessionManager.get(player.getUniqueId());

        List<String> notes = user.getPlayerNotes();

        for (String message : messages.noteListStart) {
            this.messages.send(sender, message.replace("%longline%", this.messages.LONG_LINE).replace("%target%", player.getName()).replace("%notes%", Integer.toString(notes.size())), message.contains("%longline%") ? "" : messages.prefixGeneral);
        }

        for (int i = 0; i < notes.size(); i++) {
            String note = notes.get(i);

            messages.send(sender, messages.noteListEntry.replace("%count%", Integer.toString(i + 1)).replace("%note%", note), messages.prefixGeneral);
        }

        for (String message : messages.noteListEnd) {
            this.messages.send(sender, message.replace("%longline%", this.messages.LONG_LINE).replace("%target%", player.getName()).replace("%notes%", Integer.toString(notes.size())), message.contains("%longline%") ? "" : messages.prefixGeneral);
        }
    }

    private void clearNotes(CommandSender sender, Player player) {
        PlayerSession user = sessionManager.get(player.getUniqueId());

        user.getPlayerNotes().clear();
        messages.send(sender, messages.noteCleared.replace("%target%", player.getName()), messages.prefixGeneral);
    }

    private void addNote(CommandSender sender, Player player, String note) {
        sessionManager.get(player.getUniqueId()).addPlayerNote(note);
        messages.send(sender, messages.noteAdded.replace("%target%", player.getName()), messages.prefixGeneral);
    }

    private void sendHelp(CommandSender sender) {
        messages.send(sender, "&7" + messages.LONG_LINE, "");
        messages.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
        messages.send(sender, "&b/" + getName() + " get &7[player]", messages.prefixGeneral);
        messages.send(sender, "&b/" + getName() + " clear &7[player]", messages.prefixGeneral);
        messages.send(sender, "&7" + messages.LONG_LINE, "");
    }
}