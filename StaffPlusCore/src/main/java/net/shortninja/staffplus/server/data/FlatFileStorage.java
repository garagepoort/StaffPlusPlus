package net.shortninja.staffplus.server.data;

import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.attribute.Ticket;
import net.shortninja.staffplus.unordered.AlertType;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.unordered.IWarning;

import java.util.List;
import java.util.Map;

public class FlatFileStorage implements IStorage {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public String getPassword(User user) {
        return null;
    }

    @Override
    public void setPassword(User user, String password) {

    }

    @Override
    public short getGlassColor(User user) {return 0;}

    @Override
    public void setGlassColor(User user, short color) {

    }

    @Override
    public List<IReport> getReports(User user) {
        return null;
    }

    @Override
    public List<IWarning> getWarnings(User user) {
        return null;
    }

    @Override
    public void addReport(IReport report) {

    }

    @Override
    public void addWarning(IWarning warning) {

    }

    @Override
    public void removeReport(User user) {

    }

    @Override
    public void removeWarning(User user) {

    }

    @Override
    public Ticket getTicketByUUID(User user) {
        return null;
    }

    @Override
    public Ticket getTickById(int id) {
        return null;
    }

    @Override
    public void addTicket(Ticket ticket) {

    }

    @Override
    public void removeTicket(User user) {

    }
}
