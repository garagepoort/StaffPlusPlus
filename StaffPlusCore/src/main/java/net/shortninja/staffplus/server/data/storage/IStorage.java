package net.shortninja.staffplus.server.data.storage;

import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.attribute.Ticket;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.unordered.IWarning;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IStorage {

    void onEnable();

    void onDisable();

    byte[] getPassword(Player player);

    void setPassword(Player player, byte[] password);

    short getGlassColor(User user);

    void setGlassColor(User user, short glassColor);

    List<IReport> getReports(UUID uuid);

    List<IWarning> getWarnings(UUID uuid);

    void addReport(IReport report);

    void addWarning(IWarning warning);

    void removeReports(IUser user);

    void removeWarnings(UUID uuid);

    Set<Ticket> getTickets();

    Ticket getTicketByUUID(UUID uuid);

    Ticket getTickById(int id);

    void addTicket(Ticket ticket);

    void removeTicket(Ticket ticket);

}
