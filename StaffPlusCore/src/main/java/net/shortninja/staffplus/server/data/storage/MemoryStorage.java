package net.shortninja.staffplus.server.data.storage;

import net.shortninja.staffplus.player.PlayerSession;
import net.shortninja.staffplus.player.attribute.Ticket;

import java.util.*;
import java.util.stream.Collectors;

public final class MemoryStorage implements IStorage {

    private final Map<UUID, DataHolder> data = new HashMap<>();

    @Override
    public short getGlassColor(PlayerSession playerSession) {
        return getOrPut(playerSession.getUuid()).glassColor;
    }

    @Override
    public void setGlassColor(PlayerSession playerSession, short glassColor) {
        getOrPut(playerSession.getUuid()).glassColor = glassColor;
    }

    @Override
    public Set<Ticket> getTickets() {
        final Set<Ticket> tickets = new HashSet<>();

        for (List<Ticket> cTickets : data.values().stream().map(dataHolder -> dataHolder.tickets).collect(Collectors.toSet())) {
            tickets.addAll(cTickets);
        }

        return tickets;
    }

    @Override
    public Ticket getTicketByUUID(UUID uuid) {
        return getOrPut(uuid).tickets.stream().filter(t -> t.getUuid().equals(uuid)).findAny().orElse(null);
    }

    @Override
    public Ticket getTickById(int id) {
        return getTickets().stream().filter(t -> t.getId() == id).findAny().orElse(null);
    }

    @Override
    public void addTicket(Ticket ticket) {
        getOrPut(ticket.getUuid()).tickets.add(ticket);
    }

    @Override
    public void removeTicket(Ticket ticket) {
        getOrPut(ticket.getUuid()).tickets.remove(ticket);
    }

    private DataHolder getOrPut(UUID id) {
        if (data.containsKey(id)) {
            return data.get(id);
        } else {
            final DataHolder holder = new DataHolder();
            data.putIfAbsent(id, holder);

            return holder;
        }
    }

    private static class DataHolder {
        private final List<Ticket> tickets = new ArrayList<>();
        private short glassColor;
    }
}
