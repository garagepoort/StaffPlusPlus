package net.shortninja.staffplus.listener.player;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.TicketHandler;
import net.shortninja.staffplus.player.attribute.TicketHandler.TicketCloseReason;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener
{
	private UserManager userManager = StaffPlus.get().userManager;
	private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
	private VanishHandler vanishHandler = StaffPlus.get().vanishHandler;
	private TicketHandler ticketHandler = StaffPlus.get().ticketHandler;
	
	public PlayerQuit()
	{
		Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoin(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		
		manageUser(player);
		modeCoordinator.removeMode(player);
		vanishHandler.removeVanish(player);
		ticketHandler.removeTicket(ticketHandler.getTicketByUuid(player.getUniqueId()), TicketCloseReason.QUIT);
	}
	
	private void manageUser(Player player)
	{
		User user = userManager.getUser(player.getUniqueId());
		
		user.setOnline(false);
	}
}