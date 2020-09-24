package net.shortninja.staffplus.server.command.cmd.mode;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.CommandUtil;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.unordered.IPlayerSession;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

import static net.shortninja.staffplus.server.command.PlayerRetrievalStrategy.ONLINE;

public class NotesCmd extends AbstractCmd {
    public static final String GET = "get";
    public static final String CLEAR = "clear";
    private final MessageCoordinator message = IocContainer.getMessage();
    private final SessionManager sessionManager = IocContainer.getSessionManager();

    public NotesCmd(String name) {
        super(name, IocContainer.getOptions().permissionExamine);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
        return CommandUtil.executeCommand(sender, true, () -> {
            if (args.length == 2) {
                String argument = args[0];
                boolean hasPermission = permission.has(sender, options.permissionExamine);

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
        });
    }

    @Override
    protected boolean canBypass(Player player) {
        return false;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 2;
    }

    @Override
    protected boolean isAuthenticationRequired() {
        return true;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return ONLINE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args.length == 2) {
            boolean hasPermission = permission.has(sender, options.permissionExamine);
            if ((args[0].equalsIgnoreCase(GET) || args[0].equalsIgnoreCase(CLEAR)) && hasPermission) {
                return Optional.of(args[1]);
            }
        }
        return Optional.of(args[0]);
    }

    @Override
    protected boolean isDelayable() {
        return false;
    }

    private void listNotes(CommandSender sender, Player player) {
        IPlayerSession user = sessionManager.get(player.getUniqueId());

        List<String> notes = user.getPlayerNotes();

        for (String message : messages.noteListStart) {
            this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", player.getName()).replace("%notes%", Integer.toString(notes.size())), message.contains("%longline%") ? "" : messages.prefixGeneral);
        }

        for (int i = 0; i < notes.size(); i++) {
            String note = notes.get(i);

            message.send(sender, messages.noteListEntry.replace("%count%", Integer.toString(i + 1)).replace("%note%", note), messages.prefixGeneral);
        }

        for (String message : messages.noteListEnd) {
            this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", player.getName()).replace("%notes%", Integer.toString(notes.size())), message.contains("%longline%") ? "" : messages.prefixGeneral);
        }
    }

    private void clearNotes(CommandSender sender, Player player) {
        IPlayerSession user = sessionManager.get(player.getUniqueId());

        user.getPlayerNotes().clear();
        message.send(sender, messages.noteCleared.replace("%target%", player.getName()), messages.prefixGeneral);
    }

    private void addNote(CommandSender sender, Player player, String note) {
        sessionManager.get(player.getUniqueId()).addPlayerNote(note);
        message.send(sender, messages.noteAdded.replace("%target%", player.getName()), messages.prefixGeneral);
    }

    private void sendHelp(CommandSender sender) {
        message.send(sender, "&7" + message.LONG_LINE, "");
        message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
        message.send(sender, "&b/" + getName() + " get &7[player]", messages.prefixGeneral);
        message.send(sender, "&b/" + getName() + " clear &7[player]", messages.prefixGeneral);
        message.send(sender, "&7" + message.LONG_LINE, "");
    }
}