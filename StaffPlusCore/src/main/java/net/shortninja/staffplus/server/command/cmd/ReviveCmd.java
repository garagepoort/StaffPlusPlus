package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.player.attribute.mode.handler.ReviveHandler;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.server.command.arguments.ArgumentProcessor;
import net.shortninja.staffplus.server.command.arguments.ArgumentType;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.common.CommandUtil.executeCommand;
import static net.shortninja.staffplus.server.command.arguments.ArgumentType.STRIP;
import static net.shortninja.staffplus.server.command.arguments.ArgumentType.TELEPORT;
import static net.shortninja.staffplus.util.lib.JavaUtils.getTargetPlayer;
import static org.bukkit.Bukkit.getPlayer;

public class ReviveCmd extends BukkitCommand {
    private static final List<ArgumentType> VALID_ARGUMENTS = Arrays.asList(TELEPORT, STRIP);

    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private ReviveHandler reviveHandler = StaffPlus.get().reviveHandler;
    private ArgumentProcessor argumentProcessor = ArgumentProcessor.getInstance();

    public ReviveCmd(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        return executeCommand(sender, () -> {

            if (!permission.has(sender, options.permissionRevive)) {
                throw new BusinessException(messages.noPermission, messages.prefixGeneral);
            }

            List<String> nonArguments = Arrays.stream(args).filter(a -> !a.startsWith("-")).collect(Collectors.toList());
            if (nonArguments.size() == 0 && !(sender instanceof Player)) {
                throw new BusinessException(messages.invalidArguments, messages.prefixGeneral);
            }

            Player targetPlayer = nonArguments.size() == 1 ? getPlayer(nonArguments.get(0)) : getTargetPlayer((Player) sender);
            if (targetPlayer == null) {
                throw new BusinessException(messages.playerOffline, messages.prefixGeneral);
            }

            if (reviveHandler.hasSavedInventory(targetPlayer.getUniqueId())) {
                reviveHandler.restoreInventory(targetPlayer);
                argumentProcessor.parseArguments(sender, targetPlayer.getName(), Arrays.asList(args), VALID_ARGUMENTS);
                message.send(sender, messages.revivedStaff.replace("%target%", targetPlayer.getName()), messages.prefixGeneral);
            } else {
                message.send(sender, messages.noFound, messages.prefixGeneral);
            }

            return true;
        });
    }
}