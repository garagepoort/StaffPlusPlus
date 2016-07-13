package net.shortninja.staffplus.player.attribute;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.util.Message;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TicketHandler
{
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private static Map<UUID, Ticket> tickets = new HashMap<UUID, Ticket>();
	private static int nextTicketId = 1;
	
	public Collection<Ticket> getTickets()
	{
		return tickets.values();
	}
	
	public Set<Ticket> getOpenTickets()
	{
		Set<Ticket> tickets = new HashSet<Ticket>();
		
		for(Ticket ticket : this.tickets.values())
		{
			if(ticket.isOpen())
			{
				tickets.add(ticket);
			}
		}
		
		return tickets;
	}
	
	public Ticket getTicketByUuid(UUID uuid)
	{
		return tickets.get(uuid);
	}
	
	public Ticket getTicketById(int id)
	{
		Ticket ticket = null;
		
		for(Ticket t : tickets.values())
		{
			if(t.getId() == id)
			{
				ticket = t;
				break;
			}
		}
		
		return ticket;
	}
	
	public int getNextId()
	{
		return nextTicketId;
	}
	
	public void addTicket(Player player, Ticket ticket)
	{
		String message = messages.ticket.replace("%ticket%", Integer.toString(ticket.getId())).replace("%player%", ticket.getName()).replace("%message%", ticket.getInquiry());
		
		this.message.send(player, message, messages.prefixTickets);
		this.message.sendGroupMessage(message, options.permissionTickets, messages.prefixTickets);
		tickets.put(ticket.getUuid(), ticket);
		nextTicketId++;
	}
	
	public void removeTicket(Ticket ticket, TicketCloseReason ticketCloseReason)
	{
		if(ticket == null)
		{
			return;
		}
		
		String message = messages.ticketRemoved.replace("%ticket%", Integer.toString(ticket.getId())).replace("%reason%", ticketCloseReason.getMessage());
		
		this.message.sendGroupMessage(message, options.permissionTickets, messages.prefixTickets);
		this.message.send(Bukkit.getPlayer(ticket.getName()), message, messages.prefixTickets);
		ticket.setHasBeenClosed(true);
		tickets.remove(ticket.getUuid());
	}
	
	public void sendResponse(CommandSender sender, Ticket ticket, String response, boolean isStaffResponse)
	{
		String message = isStaffResponse ? messages.ticketResponseStaff.replace("%ticket%", Integer.toString(ticket.getId())).replace("%player%", ticket.getName()).replace("%message%", response) : messages.ticket.replace("%ticket%", Integer.toString(ticket.getId())).replace("%player%", sender.getName()).replace("%message%", response);
		
		this.message.send(sender, message, messages.prefixTickets);
		
		if(!isStaffResponse)
		{
			if(ticket.isOpen() || options.ticketsGlobal)
			{
				this.message.sendGroupMessage(message, options.permissionTickets, messages.prefixTickets);
			}else this.message.send(Bukkit.getPlayer(ticket.getHandlerName()), message, messages.prefixTickets);
		}else this.message.send(Bukkit.getPlayer(ticket.getHandlerName()), message, messages.prefixTickets);
		
		if(!options.ticketsKeepOpen && isStaffResponse)
		{
			ticket.setHandlerName(sender.getName());
		}
	}
	
	public enum TicketCloseReason
	{
		STAFF("Closed by staff"), QUIT("Player logged off");
		
		String message;
		
		private TicketCloseReason(String message)
		{
			this.message = message;
		}
		
		public String getMessage()
		{
			return message;
		}
	}
}