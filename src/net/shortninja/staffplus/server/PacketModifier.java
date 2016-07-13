package net.shortninja.staffplus.server;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler.VanishType;

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
		PacketListenerAPI.addPacketHandler(new PacketHandler(StaffPlus.get())
		{
			@Override
			public void onSend(SentPacket packet)
			{
				String packetName = packet.getPacketName();
				
				for(String string : options.animationPackets)
				{
					System.out.println(packetName);
					System.out.println(string);
					if(packetName.equalsIgnoreCase(string))
					{
						handleAnimation(packet);
						System.out.println("Packet sent");
						return;
					}
				}
				
				for(String string : options.soundNames)
				{
					System.out.println(packetName);
					System.out.println(string);
					if(packetName.equalsIgnoreCase(string))
					{
						handleSound(packet);
						System.out.println("Packet sent");
						return;
					}
				}
			}

			@Override public void onReceive(ReceivedPacket packet)
			{
				String packetName = packet.getPacketName();
				
				for(String string : options.animationPackets)
				{
					System.out.println(packetName);
					System.out.println(string);
					if(packetName.equalsIgnoreCase(string))
					{
						System.out.println("Packet received");
						return;
					}
				}
				
				for(String string : options.soundNames)
				{
					if(packetName.equalsIgnoreCase(string))
					{
						System.out.println("Packet received");
						return;
					}
				}
			}
		});
	}
	
	private void handleAnimation(SentPacket packet)
	{
		if(userManager.getUser(packet.getPlayer().getUniqueId()).getVanishType() != VanishType.TOTAL)
		{
			return;
		}
		
		packet.setCancelled(true);
	}
	
	private void handleSound(SentPacket packet)
	{
		if(userManager.getUser(packet.getPlayer().getUniqueId()).getVanishType() != VanishType.TOTAL)
		{
			return;
		}
		
		packet.setCancelled(true);
	}
}