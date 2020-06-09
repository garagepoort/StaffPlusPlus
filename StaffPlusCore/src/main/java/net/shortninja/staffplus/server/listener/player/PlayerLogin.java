package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public final class PlayerLogin implements Listener {


	public PlayerLogin() {
		Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent e) { }

}
