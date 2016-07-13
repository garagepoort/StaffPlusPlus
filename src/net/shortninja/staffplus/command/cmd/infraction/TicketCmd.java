package net.shortninja.staffplus.command.cmd.infraction;

import java.util.Collection;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.attribute.Ticket;
import net.shortninja.staffplus.player.attribute.TicketHandler;
import net.shortninja.staffplus.player.attribute.TicketHandler.TicketCloseReason;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.util.lib.JavaUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class TicketCmd extends BukkitCommand
{
	private Permission permission = StaffPlus.get().permission;
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private TicketHandler ticketHandler = StaffPlus.get().ticketHandler;
	
	public TicketCmd(String name)
	{
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args)
	{
		boolean hasPermission = permission.has(sender, options.permissionTickets);
		
		if(args.length >= 3 && hasPermission)
		{
			if(manageTicket(sender, args))
			{
				return true;
			}
		}
		
		if(args.length >= 1)
		{
			String argument = args[0];
			
			if(argument.equalsIgnoreCase("list") && hasPermission)
			{
				listTickets(sender);
			}else if(sender instanceof Player)
			{
				Player player = (Player) sender;
				Ticket ticket = ticketHandler.getTicketByUuid(player.getUniqueId());
				
				if(ticket == null)
				{
					ticketHandler.addTicket(player, new Ticket(player.getUniqueId(), player.getName(), ticketHandler.getNextId(), JavaUtils.compileWords(args, 0)));
				}else if(!ticket.hasBeenClosed() && ticket.getName().equals(player.getName()))
				{
					ticketHandler.sendResponse(sender, ticket, JavaUtils.compileWords(args, 0), false);
				}else ticketHandler.addTicket(player, new Ticket(player.getUniqueId(), player.getName(), ticketHandler.getNextId(), JavaUtils.compileWords(args, 0)));
			}else message.send(sender, messages.onlyPlayers, messages.prefixTickets);
		}else if(!hasPermission)
		{
			message.send(sender, messages.invalidArguments.replace("%usage%", usageMessage), messages.prefixTickets);
		}else sendHelp(sender);
		
		return true;
	}
	
	private boolean manageTicket(CommandSender sender, String[] args)
	{
		boolean hasBeenManaged = false;
		String argument = args[0];
		Ticket ticket = getTicket(args[1]);
		
		switch(argument)
		{
			case "respond":
				hasBeenManaged = true;
				
				if(ticket == null)
				{
					message.send(sender, messages.ticketNotFound, messages.prefixTickets);
				}else if(!ticket.hasBeenClosed() || ticket.getHandlerName().equals(sender.getName()))
				{
					ticketHandler.sendResponse(sender, ticket, JavaUtils.compileWords(args, 2), true);
				}
				break;
			case "close":
				hasBeenManaged = true;
				
				if(ticket == null)
				{
					message.send(sender, messages.ticketNotFound, messages.prefixTickets);
				}else if(!ticket.hasBeenClosed())
				{
					ticketHandler.removeTicket(ticket, TicketCloseReason.STAFF);
				}else message.send(sender, messages.ticketNotFound, messages.prefixTickets);
				break;
		}
		
		return hasBeenManaged;
	}
	
	private Ticket getTicket(String option)
	{
		Ticket ticket = null;
		Player player = Bukkit.getPlayer(option);
		
		if(player != null) // Prioritizes a player's name that is consists entirely of integers over the ID.
		{
			ticket = ticketHandler.getTicketByUuid(player.getUniqueId());
		}else if(JavaUtils.isInteger(option))
		{
			ticket = ticketHandler.getTicketById(Integer.parseInt(option));
		}
		
		return ticket;
	}
	
	private void listTickets(CommandSender sender)
	{
		Collection<Ticket> tickets = ticketHandler.getOpenTickets();
		
		for(String message : messages.ticketListStart)
		{
			this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%tickets%", Integer.toString(tickets.size())), message.contains("%longline%") ? "" : messages.prefixReports);
		}
		
		for(Ticket ticket : tickets)
		{
			message.send(sender, messages.ticketsListEntry.replace("%ticket%", Integer.toString(ticket.getId())).replace("%target%", ticket.getName()).replace("%message%", ticket.getInquiry()), messages.prefixTickets);
		}
		
		for(String message : messages.ticketListEnd)
		{
			this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%tickets%", Integer.toString(tickets.size())), message.contains("%longline%") ? "" : messages.prefixReports);
		}
	}
	
	private void sendHelp(CommandSender sender)
	{
		message.send(sender, message.LONG_LINE, "");
		message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixReports);
		message.send(sender, "&b/" + getName() + " list", messages.prefixReports);
		message.send(sender, "&b/" + getName() + " respond &7[player | id] [message]", messages.prefixReports);
		message.send(sender, "&b/" + getName() + " close &7[player | id] [reason]", messages.prefixReports);
		message.send(sender, message.LONG_LINE, "");
	}
}