package net.shortninja.staffplus.core.domain.chat;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.NONE;

public class ChatCmd extends AbstractCmd {
    private final MessageCoordinator message = StaffPlus.get().iocContainer.get(MessageCoordinator.class);
    private final ChatHandler chatHandler = StaffPlus.get().iocContainer.get(ChatHandler.class);

    public ChatCmd(String name) {
        super(name);
    }

    @Override
    public boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
        if (args.length >= 2 && permissionHandler.isOp(sender)) {
            handleChatArgument(sender, args[0], args[1], false);
        } else if (args.length == 1) {
            handleChatArgument(sender, args[0], "", true);
        } else {
            sendHelp(sender);
        }

        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return NONE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

    private void handleChatArgument(CommandSender sender, String argument, String option, boolean shouldCheckPermission) {
        String name = sender instanceof Player ? sender.getName() : "Console";

        switch (argument.toLowerCase()) {
            case "clear":
                if (!shouldCheckPermission || permissionHandler.has(sender, options.permissionChatClear)) {
                    chatHandler.clearChat(name);
                } else message.send(sender, messages.noPermission, messages.prefixGeneral);
                break;
            case "toggle":
                if (!shouldCheckPermission || permissionHandler.has(sender, options.permissionChatToggle)) {
                    chatHandler.setChatEnabled(name, option.isEmpty() ? !chatHandler.isChatEnabled() : Boolean.parseBoolean(option));
                } else message.send(sender, messages.noPermission, messages.prefixGeneral);
                break;
            case "slow":
                if (!shouldCheckPermission || permissionHandler.has(sender, options.permissionChatSlow)) {
                    if (JavaUtils.isInteger(option)) {
                        chatHandler.setChatSlow(name, Integer.parseInt(option));
                    } else
                        message.send(sender, messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
                } else message.send(sender, messages.noPermission, messages.prefixGeneral);
                break;
            default:
                message.send(sender, messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
                break;
        }
    }

    private void sendHelp(CommandSender sender) {
        message.send(sender, "&7" + message.LONG_LINE, "");
        message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
        message.send(sender, "&7" + message.LONG_LINE, "");
    }
}