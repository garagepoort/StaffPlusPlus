package net.shortninja.staffplus.server.data;

import net.shortninja.staffplus.player.attribute.Ticket;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.unordered.IWarning;

import java.util.List;
import java.util.UUID;

public interface IStorage {

    void onEnable();

    void onDisable();

     String getPassword(UUID uuid);

     void setPassword(UUID uuid, String password);

     short getGlassColor(UUID uuid);

    void setGlassColor(UUID uuid,short color);

    List<IReport> getReports(UUID uuid);

    List<IWarning> getWarnings(UUID uuid);

    void addReport(IReport report);

    void addWarning(IWarning warning);

    void removeReport(UUID uuid);

    void removeWarning(UUID uuid);

    Ticket getTicketByUUID(UUID uuid);

    Ticket getTickById(int id);

    void addTicket(Ticket ticket);

    void removeTicket(UUID uuid);

}
