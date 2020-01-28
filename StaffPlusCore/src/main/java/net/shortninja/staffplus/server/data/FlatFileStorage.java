package net.shortninja.staffplus.server.data;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.attribute.Ticket;
import net.shortninja.staffplus.player.attribute.infraction.Report;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.server.data.file.DataFile;
import net.shortninja.staffplus.unordered.AlertType;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.unordered.IWarning;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        return (short) dataFile.getInt(user.getUuid().toString()+"." + "glass-color",0);
    }

    @Override
    public void setGlassColor(User user, short glassColor) {
        dataFile.set(user.getUuid().toString()+"." + "glass-color",glassColor);
    }

    @Override
    public List<IReport> getReports(User user) {
        List<IReport> reports = new ArrayList<>();

        for (String string : dataFile.getStringList(user.getUuid().toString() + ".reports")) {
            String[] parts = string.split(";");
            UUID reporterUuid = UUID.fromString(parts[2]);
            String offlineName = Bukkit.getOfflinePlayer(reporterUuid).getName();
            String reporterName = offlineName == null ? parts[1] : offlineName;

            reports.add(new Report(user.getUuid(), user.getName(), parts[0], reporterName, reporterUuid));
        }

        return reports;
    }

    @Override
    public List<IWarning> getWarnings(User user) {
        List<IWarning> warnings = new ArrayList<>();

        for (String string : dataFile.getStringList(user.getUuid().toString() + ".warnings")) {
            String[] parts = string.split(";");
            UUID issuerUuid = UUID.fromString(parts[2]);
            String offlineName = Bukkit.getOfflinePlayer(issuerUuid).getName();
            String issuerName = offlineName == null ? parts[1] : offlineName;

            warnings.add(new Warning(user.getUuid(), user.getName(), parts[0], issuerName, issuerUuid, Long.valueOf(parts[3])));
        }

        return warnings;
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
