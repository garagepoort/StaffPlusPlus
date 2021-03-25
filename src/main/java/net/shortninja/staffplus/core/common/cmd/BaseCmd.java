package net.shortninja.staffplus.core.common.cmd;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import org.bukkit.command.Command;

import java.util.ArrayList;
import java.util.List;

public class BaseCmd {

    private Command command;
    private String match;
    private String description;
    private String usage;
    private List<String> permissions = new ArrayList<>();

    public BaseCmd(MessageCoordinator message, Messages messages, Command command) {
        this.command = command;
        this.match = StaffPlus.get().getDescription().getName();
        this.description = message.colorize(command.getDescription());
        this.usage = "/" + match + " " + command.getUsage();

        this.command.setPermissionMessage(message.colorize(messages.noPermission));
        this.command.setDescription(description);
        this.command.setUsage(usage);
    }

    public Command getCommand() {
        return command;
    }

    public String getMatch() {
        return match;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}