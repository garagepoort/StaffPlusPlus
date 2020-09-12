package net.shortninja.staffplus.server.data.storage;

import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.attribute.Ticket;
import net.shortninja.staffplus.unordered.IWarning;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IStorage {

    short getGlassColor(User user);

    void setGlassColor(User user, short glassColor);

    List<IWarning> getWarnings(UUID uuid);

    void addWarning(IWarning warning);

    void removeWarnings(UUID uuid);

    Set<Ticket> getTickets();

    Ticket getTicketByUUID(UUID uuid);

    Ticket getTickById(int id);

    void addTicket(Ticket ticket);

    void removeTicket(Ticket ticket);

}
