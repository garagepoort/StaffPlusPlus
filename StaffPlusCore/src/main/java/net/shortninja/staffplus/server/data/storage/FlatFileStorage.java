package net.shortninja.staffplus.server.data.storage;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.attribute.Ticket;
import net.shortninja.staffplus.player.attribute.TicketHandler;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.unordered.IWarning;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class FlatFileStorage implements IStorage {

    private final FileConfiguration dataFile = StaffPlus.get().dataFile.getConfiguration();

    @Override
    public short getGlassColor(User user) {
        return (short) dataFile.getInt(user.getUuid().toString() + "." + "glass-color", 0);
    }

    @Override
    public void setGlassColor(User user, short glassColor) {
        dataFile.set(user.getUuid().toString() + "." + "glass-color", glassColor);
        try {
            dataFile.save(StaffPlus.get().dataFile.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<IWarning> getWarnings(UUID uuid) {
        List<IWarning> warnings = new ArrayList<>();

        for (String string : dataFile.getStringList(uuid.toString() + ".warnings")) {
            String[] parts = string.split(";");
            UUID issuerUuid = UUID.fromString(parts[2]);
            String offlineName = Bukkit.getOfflinePlayer(issuerUuid).getName();
            String issuerName = offlineName == null ? parts[1] : offlineName;
            Player p = Bukkit.getPlayer(uuid);
            String pName;
            if(p==null)
                pName = "Console";
            else
                pName = p.getName();
            if(pName == null)
                pName = "Console";
            warnings.add(new Warning(uuid, pName, parts[0], issuerName, issuerUuid, Long.valueOf(parts[3])));
        }

        return warnings;
    }

    @Override
    public void addWarning(IWarning warning) {
        List<IWarning> warnings = getWarnings(warning.getUuid());
        warnings.add(warning);
        List<String> warningList = new ArrayList<String>();
        for (IWarning r : warnings) {
            warningList.add(r.getReason() + ";" + r.getIssuerName() + ";" + (r.getIssuerUuid() == null ? "null" : r.getIssuerUuid().toString()) +";" + r.getTime());
        }
        try {
            dataFile.set(warning.getUuid().toString() + ".warnings", warningList);
            dataFile.save(StaffPlus.get().dataFile.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeWarnings(UUID uuid) {
        try {
            dataFile.set(uuid.toString() + ".warnings", new ArrayList<>());
            dataFile.save(StaffPlus.get().dataFile.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Ticket getTicketByUUID(UUID uuid) {
        return TicketHandler.getTicketsMap().get(uuid);
    }

    @Override
    public Ticket getTickById(int id) {
        for (Ticket t : TicketHandler.getTicketsMap().values()) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    @Override
    public void addTicket(Ticket ticket) {
        TicketHandler.getTicketsMap().put(ticket.getUuid(), ticket);
    }

    @Override
    public void removeTicket(Ticket ticket) {
        TicketHandler.getTicketsMap().remove(ticket.getUuid());
    }

    @Override
    public Set<Ticket> getTickets(){
        Set<Ticket> tickets = new HashSet<Ticket>();

        for (Ticket ticket : TicketHandler.getTicketsMap().values()) {
            if (ticket.isOpen()) {
                tickets.add(ticket);
            }
        }

        return tickets;
    }
}
