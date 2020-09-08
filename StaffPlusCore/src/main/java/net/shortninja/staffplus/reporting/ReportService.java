package net.shortninja.staffplus.reporting;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.event.ReportPlayerEvent;
import net.shortninja.staffplus.player.ProvidedPlayer;
import net.shortninja.staffplus.player.attribute.infraction.Report;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.unordered.IUser;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ReportService {

    private static Map<UUID, Report> unresolvedReports = new HashMap<UUID, Report>();
    private static ReportService instance;
    private FileConfiguration dataFile = StaffPlus.get().dataFile.getConfiguration();

    private ReportService() {
    }

    public static ReportService getInstance() {
        if(instance == null) {
            instance = new ReportService();
        }
        return instance;
    }

    public Collection<Report> getUnresolvedReports() {
        return unresolvedReports.values();
    }

    public void removeUnresolvedReport(UUID uuid) {
        unresolvedReports.remove(uuid);
    }

    public List<IReport> getPlayerReports(ProvidedPlayer providedPlayer) {
        List<IReport> reports = new ArrayList<>();

        for (String string : dataFile.getStringList(providedPlayer.getId() + ".reports")) {
            String[] parts = string.split(";");
            UUID reporterUuid = UUID.fromString(parts[2]);
            String offlineName = getOfflineName(reporterUuid);
            String reporterName = offlineName == null ? parts[1] : offlineName;

            reports.add(new Report(providedPlayer.getId(), providedPlayer.getUsername(), parts[0], reporterName, reporterUuid));
        }

        return reports;
    }

    public void addUnresolvedReport(Report report) {
        unresolvedReports.put(report.getUuid(), report);
        Bukkit.getPluginManager().callEvent(new ReportPlayerEvent(report));
    }

    public void addReport(IUser user, Report report) {
        addUnresolvedReport(report);
        user.addReport(report);
        StaffPlus.get().storage.addReport(report);
    }

    private String getOfflineName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    public void clearReports(IUser user) {
        StaffPlus.get().storage.removeReports(user);
        user.getReports().clear();

        if (unresolvedReports.containsKey(user.getUuid())) {
            unresolvedReports.remove(user.getUuid());
        }
    }
}
