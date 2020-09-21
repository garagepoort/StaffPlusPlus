package net.shortninja.staffplus.server.chat;

import org.bukkit.entity.Player;

public interface ChatPreventer {

    boolean shouldPrevent(Player player, String message);

}
