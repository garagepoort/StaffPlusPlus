package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.StaffPlus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public final class PlayerLogin implements Listener {

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		StaffPlus.get().versionProtocol.inject(e.getPlayer());
	}
}
