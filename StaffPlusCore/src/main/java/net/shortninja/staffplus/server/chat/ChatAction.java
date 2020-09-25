package net.shortninja.staffplus.server.chat;

import org.bukkit.entity.Player;

public interface ChatAction {

	void execute(Player player, String message);

}