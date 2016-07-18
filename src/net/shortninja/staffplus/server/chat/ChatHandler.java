package net.shortninja.staffplus.server.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.Permission;

import org.bukkit.entity.Player;

public class ChatHandler
{
	private Permission permission = StaffPlus.get().permission;
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private boolean isChatEnabled = true;
	private long chatSlowLength = 0;
	private long chatSlowStart = 0;
	private static Map<UUID, Long> userChatTimes = new HashMap<UUID, Long>();
	
	public boolean isChatEnabled()
	{
		return isChatEnabled;
	}
	
	public boolean isChatEnabled(Player player)
	{
		return isChatEnabled || permission.has(player, options.permissionChatToggle);
	}
	
	public boolean canChat(Player player)
	{
		boolean canChat = true;
		
		if(chatSlowLength > 0 && !permission.has(player, options.permissionChatSlow))
		{
			UUID uuid = player.getUniqueId();
			long now = System.currentTimeMillis();
			long lastChat = userChatTimes.containsKey(uuid) ? userChatTimes.get(uuid) : 0;
			
			if((now - chatSlowStart) >= chatSlowLength)
			{
				chatSlowLength = 0;
				chatSlowStart = 0;
				userChatTimes.clear();
			}else if(((now - lastChat) / 1000) <= options.chatSlow)
			{
				canChat = false;
			}else userChatTimes.put(uuid, now);
		}
		
		return canChat;
	}
	
	public boolean hasHandle(String message)
	{
		return message.startsWith(options.staffChatHandle) && !options.staffChatHandle.isEmpty();
	}
	
	public void setChatEnabled(String name, boolean isChatEnabled)
	{
		String status = isChatEnabled ? "enabled" : "disabled";
		
		message.sendGlobalMessage(messages.chatToggled.replace("%status%", status).replace("%player%", name), messages.prefixGeneral);
		this.isChatEnabled = isChatEnabled;
	}
	
	public void setChatSlow(String name, int time)
	{
		chatSlowLength = time * 1000;
		chatSlowStart = System.currentTimeMillis();
		message.sendGlobalMessage(messages.chatSlowed.replace("%seconds%", Integer.toString(time)).replace("%player%", name), messages.prefixGeneral);
	}
	
	public void sendStaffChatMessage(String name, String message)
	{
		this.message.sendGroupMessage(messages.staffChat.replace("%player%", name).replace("%message%", message), options.permissionStaffChat, messages.prefixStaffChat);
	}
	
	public void clearChat(String name)
	{
		for(int i = 0; i < options.chatLines; i++)
		{
			message.sendGlobalMessage(messages.chatClearLine, "");
		}
		
		message.sendGlobalMessage(messages.chatCleared.replace("%player%", name), messages.prefixGeneral);
	}
}