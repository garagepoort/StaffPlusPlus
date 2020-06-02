package net.shortninja.staffplus.player.attribute;

import net.shortninja.staffplus.StaffPlus;
<<<<<<< HEAD
import net.shortninja.staffplus.server.data.MySQLConnection;
=======
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

<<<<<<< HEAD
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
=======
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
import java.util.*;

public class TicketHandler {
    private static Map<UUID, Ticket> tickets = new HashMap<UUID, Ticket>();
    private static int nextTicketId = 1;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;

    public Collection<Ticket> getTickets() {
        return tickets.values();
    }

    public Set<Ticket> getOpenTickets() {
<<<<<<< HEAD
        Set<Ticket> tickets = new HashSet<Ticket>();

        for (Ticket ticket : this.tickets.values()) {
            if (ticket.isOpen()) {
                tickets.add(ticket);
            }
        }

        return tickets;
    }

    public Ticket getTicketByUuid(UUID uuid) {
        if (options.storageType.equalsIgnoreCase("flatfile"))
            return tickets.get(uuid);
        else if (options.storageType.equalsIgnoreCase("mysql")) {
            try (Connection sql = MySQLConnection.getConnection();
                 PreparedStatement ps = sql.prepareStatement("SELECT ID FROM sp_warnings WHERE PLAYER_UUID=?")) {
                ps.setString(1, uuid.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    tickets.put(uuid, new Ticket(uuid, Bukkit.getPlayer(uuid).getDisplayName(), rs.getInt("ID"), rs.getString("Inquiry")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tickets.get(uuid);
    }

    public Ticket getTicketById(int id) {
        Ticket ticket = null;
        if (options.storageType.equalsIgnoreCase("flatfile")) {
            for (Ticket t : tickets.values()) {
                if (t.getId() == id) {
                    ticket = t;
                    break;
                }
            }
        } else if (options.storageType.equalsIgnoreCase("mysql")) {
            try (Connection sql = MySQLConnection.getConnection();
                 PreparedStatement ps = sql.prepareStatement("SELECT ID FROM sp_warnings WHERE ID=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    ticket = new Ticket(UUID.fromString(rs.getString("UUID")),
                            Bukkit.getPlayer(UUID.fromString("UUID")).getDisplayName(), id, rs.getString("Inquiry"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ticket;
=======
        return StaffPlus.get().storage.getTickets();
    }

    public Ticket getTicketByUuid(UUID uuid) {
        return StaffPlus.get().storage.getTicketByUUID(uuid);
    }

    public Ticket getTicketById(int id) {
        return StaffPlus.get().storage.getTickById(id);
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
    }

    public int getNextId() {
        return nextTicketId;
    }

    public void addTicket(Player player, Ticket ticket) {
        String message = messages.ticket.replace("%ticket%", Integer.toString(ticket.getId())).replace("%player%", ticket.getName()).replace("%message%", ticket.getInquiry());

        this.message.send(player, message, messages.prefixTickets);
        this.message.sendGroupMessage(message, options.permissionTickets, messages.prefixTickets);
<<<<<<< HEAD
        if (options.storageType.equalsIgnoreCase("flatfile"))
            tickets.put(ticket.getUuid(), ticket);
        else if (options.storageType.equalsIgnoreCase("mysql")) {
            try (Connection sql = MySQLConnection.getConnection();
                 PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_tickets(WARNER_UUID,ID,Inquiry) " +
                         "VALUES(?, ?, ?);")) {
                insert.setString(1, ticket.getUuid().toString());
                insert.setInt(2, ticket.getId());
                insert.setString(3, ticket.getInquiry());
                insert.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
=======
        StaffPlus.get().storage.addTicket(ticket);
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
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
<<<<<<< HEAD
        if (options.storageType.equalsIgnoreCase("flatfile"))
            tickets.remove(ticket.getUuid());
        else if (options.storageType.equalsIgnoreCase("mysql")) {
            try (Connection sql = MySQLConnection.getConnection();
                 PreparedStatement delete = sql.prepareCall("DELETE FROM TABLE sp_tickets WHERE WARNER_UUID =?;")) {
                delete.setString(1, ticket.getUuid().toString());
                delete.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
=======
        StaffPlus.get().storage.removeTicket(ticket);

>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
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
<<<<<<< HEAD
=======

    public static Map<UUID, Ticket> getTicketsMap() {
        return tickets;
    }
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
}