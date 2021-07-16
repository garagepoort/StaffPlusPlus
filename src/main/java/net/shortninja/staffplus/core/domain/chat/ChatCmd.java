package net.shortninja.staffplus.core.domain.chat;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

@Command(
    command = "commands:chat",
    permissions = {"permissions:chat-clear","permissions:chat-toggle","permissions:chat-slow"},
    description = "Executes the given chat management action.",
    usage = "[clear | toggle | slow] {enable | disable | time}"
)
@IocBean(conditionalOnProperty = "chat-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class ChatCmd extends AbstractCmd {

    private final ChatHandler chatHandler;
    private final PermissionHandler permissionHandler;

    @ConfigProperty("permissions:chat-clear")
    private String permissionChatClear;
    @ConfigProperty("permissions:chat-toggle")
    private String permissionChatToggle;
    @ConfigProperty("permissions:chat-slow")
    private String permissionChatSlow;

    public ChatCmd(PermissionHandler permissionHandler, Messages messages, ChatHandler chatHandler, CommandService commandService) {
        super(messages, permissionHandler, commandService);
        this.chatHandler = chatHandler;
        this.permissionHandler = permissionHandler;
    }

    @Override
    public boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {
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
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

    private void handleChatArgument(CommandSender sender, String argument, String option, boolean shouldCheckPermission) {
        String name = sender instanceof Player ? sender.getName() : "Console";

        switch (argument.toLowerCase()) {
            case "clear":
                if (!shouldCheckPermission || permissionHandler.has(sender, permissionChatClear)) {
                    chatHandler.clearChat(name);
                } else messages.send(sender, messages.noPermission, messages.prefixGeneral);
                break;
            case "toggle":
                if (!shouldCheckPermission || permissionHandler.has(sender, permissionChatToggle)) {
                    chatHandler.setChatEnabled(name, option.isEmpty() ? !chatHandler.isChatEnabled() : Boolean.parseBoolean(option));
                } else messages.send(sender, messages.noPermission, messages.prefixGeneral);
                break;
            case "slow":
                if (!shouldCheckPermission || permissionHandler.has(sender, permissionChatSlow)) {
                    if (JavaUtils.isInteger(option)) {
                        chatHandler.setChatSlow(name, Integer.parseInt(option));
                    } else
                        messages.send(sender, messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
                } else messages.send(sender, messages.noPermission, messages.prefixGeneral);
                break;
            default:
                messages.send(sender, messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
                break;
        }
    }

    private void sendHelp(CommandSender sender) {
        messages.send(sender, "&7" + messages.LONG_LINE, "");
        messages.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
        messages.send(sender, "&7" + messages.LONG_LINE, "");
    }
}