package net.shortninja.staffplus.core.domain.staff.freeze;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeRequest {

    private final CommandSender commandSender;
    private final Player player;
    private final boolean enableFreeze;

    public FreezeRequest(CommandSender commandSender, Player player, boolean enableFreeze) {
        this.commandSender = commandSender;
        this.player = player;
        this.enableFreeze = enableFreeze;
    }

    public CommandSender getCommandSender() {
        return commandSender;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isEnableFreeze() {
        return enableFreeze;
    }

}
