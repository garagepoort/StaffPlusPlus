package net.shortninja.staffplus.server.command;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.command.Command;

import java.util.ArrayList;
import java.util.List;

public class BaseCmd {
    private MessageCoordinator message = IocContainer.getMessage();
    private Messages messages = IocContainer.getMessages();
    private Command command;
    private boolean isEnabled;
    private String match;
    private String description;
    private String usage;
    private List<String> permissions = new ArrayList<String>();

    public BaseCmd(Command command, boolean isEnabled, String permission, String description, String usage) {
        this.command = command;
        this.isEnabled = isEnabled;
        this.match = StaffPlus.get().getDescription().getName();
        this.description = message.colorize(description);
        this.usage = "/" + match + " " + usage;

        this.command.setPermissionMessage(message.colorize(messages.noPermission));
        this.command.setDescription(description);
        this.command.setUsage(usage);

        if (!permission.isEmpty()) {
            this.command.setPermission(permission);
            permissions.add(permission);
        }
    }

    public BaseCmd(Command command, boolean isEnabled, List<String> permission, String description, String usage) {
        this.command = command;
        this.isEnabled = isEnabled;
        this.match = StaffPlus.get().getDescription().getName();
        this.description = description;
        this.usage = "/" + match + " " + usage;

        this.command.setPermissionMessage(message.colorize(messages.noPermission));
        this.command.setDescription(description);
        this.command.setUsage(usage);
        permissions.addAll(permission);
    }

    public BaseCmd(Command command, boolean isEnabled, String description, String usage) {
        this(command, isEnabled, "", description, usage);
    }

    public boolean isEnabled() {
        return !match.isEmpty() && isEnabled;
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