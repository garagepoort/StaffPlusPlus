package net.shortninja.staffplus.unordered;

import net.shortninja.staffplus.event.ReportStatus;
import org.bukkit.Location;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public interface IReport {

    String getReason();

    String getStaffName();

    String getReporterName();

    UUID getReporterUuid();

    void setReporterName(String newName);

    UUID getUuid();

    UUID getCulpritUuid();

    String getCulpritName();

    ReportStatus getReportStatus();

    ZonedDateTime getTimestamp();

    String getCloseReason();

    UUID getStaffUuid();

    int getId();

    String getServerName();

    Optional<Location> getLocation();
}
