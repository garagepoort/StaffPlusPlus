package net.shortninja.staffplus.server.data.Storage;

import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.attribute.Ticket;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.unordered.IWarning;
import org.bukkit.entity.Player;

import java.util.List;
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

    void removeReport(User user);

    void removeWarning(UUID uuid);

    Ticket getTicketByUUID(UUID uuid);

    Ticket getTickById(int id);

    void addTicket(Ticket ticket);

    void removeTicket(Ticket ticket);

}
