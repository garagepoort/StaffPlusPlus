package net.shortninja.staffplus.listener.player;

import java.util.UUID;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.Load;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler;
import net.shortninja.staffplus.util.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener
{
	private Permission permission = StaffPlus.get().permission;
	private Options options = StaffPlus.get().options;
	private UserManager userManager = StaffPlus.get().userManager;
	private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
	private VanishHandler vanishHandler = StaffPlus.get().vanishHandler;
	
	public PlayerJoin()
	{
		Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		manageUser(player);
		vanishHandler.updateVanish();
		
		if(permission.has(player, options.permissionMode) && options.modeEnableOnLogin)
		{
			modeCoordinator.addMode(player);
		}
	}
	
	private void manageUser(Player player)
	{
		UUID uuid = player.getUniqueId();
		
		if(userManager.hasUser(uuid))
		{
			userManager.getUser(uuid).setOnline(true);
		}else new Load(player);
	}
}