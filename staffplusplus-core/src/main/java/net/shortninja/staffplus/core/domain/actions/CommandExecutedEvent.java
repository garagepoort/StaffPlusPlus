package net.shortninja.staffplus.core.domain.actions;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CommandExecutedEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final CommandSender executor;
    private final String command;

    public CommandExecutedEvent(CommandSender executor, String command) {
        this.executor = executor;
        this.command = command;
    }

    public CommandSender getExecutor() {
        return executor;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
