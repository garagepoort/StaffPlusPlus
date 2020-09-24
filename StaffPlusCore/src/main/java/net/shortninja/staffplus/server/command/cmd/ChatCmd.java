package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.chat.ChatHandler;
import net.shortninja.staffplus.server.command.AbstractCmd;
import net.shortninja.staffplus.server.command.PlayerRetrievalStrategy;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

import static net.shortninja.staffplus.server.command.PlayerRetrievalStrategy.NONE;

public class ChatCmd extends AbstractCmd {
    private final MessageCoordinator message = IocContainer.getMessage();
    private final ChatHandler chatHandler = IocContainer.getChatHandler();

    public ChatCmd(String name) {
        super(name);
    }

    @Override
    public boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer) {
        if (args.length >= 2 && permission.isOp(sender)) {
            handleChatArgument(sender, args[0], args[1], false);
        } else if (args.length == 1) {
            handleChatArgument(sender, args[0], "", true);
        } else {
            sendHelp(sender);
        }

        return true;
    }

    @Override
    protected boolean canBypass(Player player) {
        return false;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected boolean isAuthenticationRequired() {
        return true;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return NONE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

    @Override
    protected boolean isDelayable() {
        return false;
    }

    private void handleChatArgument(CommandSender sender, String argument, String option, boolean shouldCheckPermission) {
        String name = sender instanceof Player ? sender.getName() : "Console";

        switch (argument.toLowerCase()) {
            case "clear":
                if (!shouldCheckPermission || permission.has(sender, options.permissionChatClear)) {
                    chatHandler.clearChat(name);
                } else message.send(sender, messages.noPermission, messages.prefixGeneral);
                break;
            case "toggle":
                if (!shouldCheckPermission || permission.has(sender, options.permissionChatToggle)) {
                    chatHandler.setChatEnabled(name, option.isEmpty() ? !chatHandler.isChatEnabled() : Boolean.parseBoolean(option));
                } else message.send(sender, messages.noPermission, messages.prefixGeneral);
                break;
            case "slow":
                if (!shouldCheckPermission || permission.has(sender, options.permissionChatSlow)) {
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