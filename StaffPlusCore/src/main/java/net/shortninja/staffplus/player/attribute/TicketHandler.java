package net.shortninja.staffplus.player.attribute;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class TicketHandler {
    private static Map<UUID, Ticket> tickets = new HashMap<UUID, Ticket>();
    private static int nextTicketId = 1;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = IocContainer.getMessages();

    public Collection<Ticket> getTickets() {
        return tickets.values();
    }

    public Set<Ticket> getOpenTickets() {
        Set<Ticket> tickets = new HashSet<Ticket>();

        for (Ticket ticket : this.tickets.values()) {
            if (ticket.isOpen()) {
                tickets.add(ticket);
            }
        }

        return tickets;
    }


    public Ticket getTicketByUuid(UUID uuid) {
        return IocContainer.getStorage().getTicketByUUID(uuid);
    }

    public Ticket getTicketById(int id) {
        return IocContainer.getStorage().getTickById(id);
    }

    public int getNextId() {
        return nextTicketId;
    }

    public void addTicket(Player player, Ticket ticket) {
        String message = messages.ticket.replace("%ticket%", Integer.toString(ticket.getId())).replace("%player%", ticket.getName()).replace("%message%", ticket.getInquiry());

        this.message.send(player, message, messages.prefixTickets);
        this.message.sendGroupMessage(message, options.permissionTickets, messages.prefixTickets);
        IocContainer.getStorage().addTicket(ticket);
        nextTicketId++;
    }

    public void removeTicket(Ticket ticket, String reason, TicketCloseReason ticketCloseReason) {
        if (ticket == null) {
            return;
        }

        String message = messages.ticketRemoved.replace("%ticket%", Integer.toString(ticket.getId())).replace("%reason%", ticketCloseReason == TicketCloseReason.STAFF ? reason : ticketCloseReason.getMessage());

        this.message.sendGroupMessage(message, options.permissionTickets, messages.prefixTickets);
        this.message.send(Bukkit.getPlayer(ticket.getName()), message, messages.prefixTickets);
        ticket.setHasBeenClosed(true);
        IocContainer.getStorage().removeTicket(ticket);

    }

    public void sendResponse(CommandSender sender, Ticket ticket, String response, boolean isStaffResponse) {
        String message = isStaffResponse ? messages.ticketResponseStaff.replace("%ticket%", Integer.toString(ticket.getId())).replace("%player%", sender.getName()).replace("%message%", response) : messages.ticket.replace("%ticket%", Integer.toString(ticket.getId())).replace("%player%", sender.getName()).replace("%message%", response);

        this.message.send(sender, message, messages.prefixTickets);

        if (!isStaffResponse) {
            if (ticket.isOpen() || options.ticketsGlobal) {
                this.message.sendGroupMessage(message, options.permissionTickets, messages.prefixTickets);
            } else this.message.send(Bukkit.getPlayer(ticket.getHandlerName()), message, messages.prefixTickets);
        } else this.message.send(Bukkit.getPlayer(ticket.getName()), message, messages.prefixTickets);

        if (!options.ticketsKeepOpen && isStaffResponse) {
            ticket.setHandlerName(sender.getName());
        }
    }

    public enum TicketCloseReason {
        STAFF("Closed by staff"), QUIT("Player logged off");

        String message;

        private TicketCloseReason(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static Map<UUID, Ticket> getTicketsMap() {
        return tickets;
    }
}