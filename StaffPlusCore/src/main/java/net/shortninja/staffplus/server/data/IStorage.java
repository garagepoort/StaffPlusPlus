package net.shortninja.staffplus.server.data;

import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.attribute.Ticket;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.unordered.IWarning;

import java.util.List;

public interface IStorage {

    void onEnable();

    void onDisable();

    String getPassword(User user);

    void setPassword(User user, String password);

    short getGlassColor(User user);

    void setGlassColor(User user,short color);

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
