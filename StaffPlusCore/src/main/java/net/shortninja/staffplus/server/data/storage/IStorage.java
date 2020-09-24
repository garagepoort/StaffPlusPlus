package net.shortninja.staffplus.server.data.storage;

import net.shortninja.staffplus.player.PlayerSession;
import net.shortninja.staffplus.player.attribute.Ticket;

import java.util.Set;
import java.util.UUID;

public interface IStorage {

    short getGlassColor(PlayerSession playerSession);

    void setGlassColor(PlayerSession playerSession, short glassColor);

    Set<Ticket> getTickets();

    Ticket getTicketByUUID(UUID uuid);

    Ticket getTickById(int id);

    void addTicket(Ticket ticket);

    void removeTicket(Ticket ticket);

}
