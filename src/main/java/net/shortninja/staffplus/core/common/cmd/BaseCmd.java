package net.shortninja.staffplus.core.common.cmd;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import org.bukkit.command.Command;

import java.util.ArrayList;
import java.util.List;

public class BaseCmd {
    private MessageCoordinator message = StaffPlus.get().iocContainer.get(MessageCoordinator.class);
    private Messages messages = StaffPlus.get().iocContainer.get(Messages.class);
    private Command command;
    private boolean isEnabled;
    private String match;
    private String description;
    private String usage;
    private List<String> permissions = new ArrayList<>();

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