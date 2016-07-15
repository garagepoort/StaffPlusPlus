package net.shortninja.staffplus.server;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler.VanishType;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.inventivetalent.packetlistener.PacketListenerAPI;
import org.inventivetalent.packetlistener.handler.PacketHandler;
import org.inventivetalent.packetlistener.handler.ReceivedPacket;
import org.inventivetalent.packetlistener.handler.SentPacket;

public class PacketModifier
{
	private Options options = StaffPlus.get().options;
	private UserManager userManager = StaffPlus.get().userManager;
	
	public PacketModifier()
	{
		initializeHandler();
	}
	
	private void initializeHandler()
	{
		PacketListenerAPI.addPacketHandler(new PacketHandler(StaffPlus.get())
		{
			@Override
			public void onSend(SentPacket packet)
			{
				String packetName = packet.getPacketName();
				
				if(packetName.equalsIgnoreCase("PacketPlayOutNamedSoundEffect"))
				{
					String soundName = (String) packet.getPacketValueSilent(0);
					
					for(String string : options.soundNames)
					{
						if(string.equalsIgnoreCase(soundName))
						{
							handleClientSound(packet);
						}
					}
				}else
				{
					for(String string : options.animationPackets)
					{
						if(packetName.equalsIgnoreCase(string))
						{
							handleClientAnimation(packet);
							break;
						}
					}
				}
			}

			@Override public void onReceive(ReceivedPacket packet)
			{
				if(packet.isCancelled())
				{
					String packetName = packet.getPacketName();
					
					if(packetName.equalsIgnoreCase("PacketPlayOutNamedSoundEffect"))
					{
						String soundName = (String) packet.getPacketValueSilent(0);
						
						for(String string : options.soundNames)
						{
							if(string.equalsIgnoreCase(soundName))
							{
								handleServerSide(packet);
							}
						}
					}else
					{
						for(String string : options.animationPackets)
						{
							if(packetName.equalsIgnoreCase(string))
							{
								handleServerSide(packet);
								break;
							}
						}
					}
				}
			}
		});
	}
	
	private void handleClientSound(SentPacket packet)
	{
		Player player = packet.getPlayer();
		
		if(player == null)
		{
			return;
		}
		
		if(!isVanished(player))
		{
			return;
		}
		
		packet.setCancelled(true);
	}
	
	private void handleClientAnimation(SentPacket packet)
	{
		Player player = packet.getPlayer();
		
		if(player == null)
		{
			return;
		}
		
		if(userManager.getUser(packet.getPlayer().getUniqueId()).getVanishType() != VanishType.TOTAL)
		{
			return;
		}
		
		packet.setCancelled(true);
	}
	

	private void handleServerSide(ReceivedPacket packet)
	{
		Player player = packet.getPlayer();
		
		if(player == null)
		{
			return;
		}
		
		Location location = new Location(player.getWorld(), (int)packet.getPacketValue(2) / 8, (int) packet.getPacketValue(3) / 8, (int)packet.getPacketValue(4) / 8);
		Chest chest = getChest(location);
		
		if(chest != null)
		{
			for(HumanEntity entity : chest.getInventory().getViewers())
			{
				if(entity instanceof Player)
				{
					Player p = (Player) entity;
					
					if(isVanished(p))
					{
						packet.setCancelled(true);
						break;
					}
				}
			}
		}else
		{
			for(Player p : Bukkit.getOnlinePlayers())
			{
				Location l = player.getLocation();
				
				if(l.getWorld() != location.getWorld())
				{
					continue;
				}
				
				if(l.distanceSquared(location) <= 256 && isVanished(p))
				{
					packet.setCancelled(true);
					break;
				}
			}
		}
	}
	
	private boolean isVanished(Player player)
	{
		return userManager.getUser(player.getUniqueId()).getVanishType() == VanishType.TOTAL;
	}
	
	private Chest getChest(Location location)
	{
		Chest chest = null;
		
		if(location.getBlock() != null)
		{
			BlockState state = location.getBlock().getState();
			
			if(state instanceof Chest)
			{
				chest = (Chest) state;
			}
		}
		
		return chest;
	}
}