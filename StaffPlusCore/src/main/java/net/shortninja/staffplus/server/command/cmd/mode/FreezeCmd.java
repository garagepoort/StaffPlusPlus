package net.shortninja.staffplus.server.command.cmd.mode;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.handler.freeze.FreezeHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.freeze.FreezeRequest;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FreezeCmd extends BukkitCommand {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private FreezeHandler freezeHandler = StaffPlus.get().freezeHandler;

    public FreezeCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (args.length < 1) {
            message.send(sender, messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
            return true;
        }

        List<String> options = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
        if (options.size() > 2) {
            message.send(sender, messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            message.send(sender, messages.playerOffline, messages.prefixGeneral);
            return true;
        }

        if (options.isEmpty()) {
            // No options given, simple freeze
            freezeHandler.execute(new FreezeRequest(sender, targetPlayer, !freezeHandler.isFrozen(targetPlayer.getUniqueId())));
            return true;
        }

        FreezeRequest freezeRequest = buildFreezeRequest(sender, options, targetPlayer);
        if (freezeRequest != null) {
            freezeHandler.execute(freezeRequest);
        }
        return true;
    }

    private FreezeRequest buildFreezeRequest(CommandSender sender, List<String> options, Player targetPlayer) {
        Optional<String> teleportLocation = options.stream().filter(o -> o.startsWith("-T")).findFirst();
        Optional<String> enabled = options.stream().filter(o -> o.equals("enabled")).findFirst();
        Optional<String> disabled = options.stream().filter(o -> o.startsWith("disabled")).findFirst();

        if ((enabled.isPresent() || disabled.isPresent()) && !permission.isOp(sender)) {
            message.send(sender, messages.noPermission, messages.prefixGeneral);
            return null;
        }

        boolean freeze = enabled.isPresent() || (!disabled.isPresent() && !freezeHandler.isFrozen(targetPlayer.getUniqueId()));

        if (teleportLocation.isPresent()) {
            teleportLocation = Optional.of(teleportLocation.get().substring(2));
        }
        return new FreezeRequest(
                sender,
                targetPlayer,
                freeze,
                teleportLocation.orElse(null)
        );
    }
}