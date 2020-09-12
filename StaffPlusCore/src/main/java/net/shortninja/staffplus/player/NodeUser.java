package net.shortninja.staffplus.player;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.reporting.Report;
import net.shortninja.staffplus.reporting.ReportPlayerService;
import net.shortninja.staffplus.unordered.AlertType;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.unordered.IWarning;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//import net.shortninja.staffplus.player.attribute.SecurityHandler;

public class NodeUser {
    //    private SecurityHandler securityHandler = StaffPlus.get().securityHandler;
    //    private SecurityHandler securityHandler = StaffPlus.get().securityHandler;
    private IUser user;
    private String prefix;
    private ReportPlayerService reportPlayerService;

    public NodeUser(IUser user) {
        this.user = user;
        this.prefix = user.getUuid() + ".";
        reportPlayerService = IocContainer.getReportPlayerService();
    }

    public String prefix() {
        return prefix;
    }

    public String name() {
        return user.getName();
    }

//    public String password() {
//        return securityHandler.getPassword(user.getUuid());
//    }

    public short glassColor() {
        return user.getGlassColor();
    }

    public List<String> reports() {
        List<String> reportsList = new ArrayList<String>();

        List<Report> reports = reportPlayerService.getReports(user.getUuid());
        for (IReport report : reports) {
            reportsList.add(report.getReason() + ";" + report.getReporterName() + ";" + (report.getReporterUuid() == null ? "null" : report.getReporterUuid().toString()));
        }

        return reportsList;
    }

    public List<String> warnings() {
        List<String> warningsList = new ArrayList<String>();

        for (IWarning warning : user.getWarnings()) {
            warningsList.add(warning.getReason() + ";" + warning.getIssuerName() + ";" + (warning.getIssuerUuid() == null ? "null" : warning.getIssuerUuid().toString()) + ";" + Long.toString(warning.getTime()));
        }

        return warningsList;
    }

    public List<String> playerNotes() {
        return new ArrayList<>(user.getPlayerNotes());
    }

    public List<String> alertOptions() {
        List<String> alertsList = new ArrayList<>();

        for (AlertType alertType : AlertType.values()) {
            alertsList.add(alertType.name() + ";" + user.shouldNotify(alertType));
        }

        return alertsList;
    }

    public UUID getUUID() {
        return user.getUuid();
    }
}