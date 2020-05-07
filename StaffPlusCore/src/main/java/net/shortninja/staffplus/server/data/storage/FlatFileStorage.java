package net.shortninja.staffplus.server.data.storage;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.attribute.Ticket;
import net.shortninja.staffplus.player.attribute.TicketHandler;
import net.shortninja.staffplus.player.attribute.infraction.Report;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.server.data.file.DataFile;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.unordered.IWarning;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FlatFileStorage implements IStorage {

    private final FileConfiguration dataFile = StaffPlus.get().dataFile.getConfiguration();

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public byte[] getPassword(Player player) {
        DataFile dataFile = new DataFile("passwords.yml");
        dataFile.load();
        return dataFile.getString(player.getUniqueId().toString()).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void setPassword(Player player, byte[] password) {
        DataFile dataFile = new DataFile("passwords.yml");
        dataFile.load();
        dataFile.getConfiguration().set(player.getUniqueId().toString(), password);
        dataFile.save();
    }

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
    public List<IReport> getReports(UUID uuid) {
        List<IReport> reports = new ArrayList<>();

        for (String string : dataFile.getStringList(uuid.toString() + ".reports")) {
            if(string==null)
                return reports;
            String[] parts = string.split(";");
            UUID reporterUuid = UUID.fromString(parts[2]);
            String offlineName = Bukkit.getOfflinePlayer(reporterUuid).getName();
            String reporterName = offlineName == null ? parts[1] : offlineName;
            try {
                reports.add(new Report(uuid, Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName(), parts[0], reporterName, reporterUuid));
            }catch (NullPointerException e ){}
            }

        return reports;
    }


    @Override
    public List<IWarning> getWarnings(UUID uuid) {
        List<IWarning> warnings = new ArrayList<>();

        for (String string : dataFile.getStringList(uuid.toString() + ".warnings")) {
            String[] parts = string.split(";");
            UUID issuerUuid = UUID.fromString(parts[2]);
            String offlineName = Bukkit.getOfflinePlayer(issuerUuid).getName();
            String issuerName = offlineName == null ? parts[1] : offlineName;

            warnings.add(new Warning(uuid, Bukkit.getPlayer(uuid).getName(), parts[0], issuerName, issuerUuid, Long.valueOf(parts[3])));
        }

        return warnings;
    }

    @Override
    public void addReport(IReport report) {
        List<IReport> reports = getReports(report.getUuid());
        reports.add(report);
        List<String> reportsList = new ArrayList<String>();
        for (IReport r : reports) {
            reportsList.add(r.getReason() + ";" + r.getReporterName() + ";" + (r.getReporterUuid() == null ? "null" : r.getReporterUuid().toString()));
        }
        dataFile.set(report.getUuid().toString() + ".reports", reportsList);
        reports.clear();
        reportsList.clear();
        try {
            dataFile.save(StaffPlus.get().dataFile.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addWarning(IWarning warning) {
        List<IWarning> warnings = getWarnings(warning.getUuid());
        warnings.add(warning);
        List<String> warningList = new ArrayList<String>();
        for (IWarning r : warnings) {
            warningList.add(r.getReason() + ";" + r.getIssuerName() + ";" + (r.getIssuerUuid() == null ? "null" : r.getIssuerUuid().toString()));
        }
        dataFile.set(warning.getUuid().toString() + ".warnings", warningList);
        warnings.clear();
        warningList.clear();
        try {
            dataFile.save(StaffPlus.get().dataFile.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeReport(User user) {
        dataFile.set(user.getUuid().toString() + ".reports", new ArrayList<>());
        try {
            dataFile.save(StaffPlus.get().dataFile.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeWarning(UUID uuid) {
        dataFile.set(uuid.toString() + ".warnings", new ArrayList<>());
        try {
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
