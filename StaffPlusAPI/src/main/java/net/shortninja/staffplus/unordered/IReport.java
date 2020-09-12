package net.shortninja.staffplus.unordered;

import net.shortninja.staffplus.event.ReportStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IReport {

    String getReason();

    String getReporterName();

    UUID getReporterUuid();

    void setReporterName(String newName);

    UUID getUuid();

    UUID getCulpritUuid();

    ReportStatus getReportStatus();

    LocalDateTime getTimestamp();
}
