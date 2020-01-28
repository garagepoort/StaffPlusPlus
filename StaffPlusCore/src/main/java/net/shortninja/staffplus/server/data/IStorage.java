package net.shortninja.staffplus.server.data;

import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.attribute.Ticket;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.unordered.IWarning;
import org.bukkit.entity.Player;

import java.util.List;

public interface IStorage {

    void onEnable();

    void onDisable();

    byte[] getPassword(Player player);

    void setPassword(Player player, byte[] password);

    short getGlassColor(User user);

    void setGlassColor(User user,short glassColor);

    List<IReport> getReports(User user);

    List<IWarning> getWarnings(User user);

    void addReport(IReport report);

    void addWarning(IWarning warning);

    void removeReport(User user);

    void removeWarning(User user);

    Ticket getTicketByUUID(User user);

    Ticket getTickById(int id);

    void addTicket(Ticket ticket);

    void removeTicket(User user);

}
