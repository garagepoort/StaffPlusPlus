package net.shortninja.staffplus.core.domain.staff.reporting.queue.dto;

import java.util.UUID;

public class ReportQueueMessage {

    private final UUID playerUuid;
    private final String playerName;
    private final int reportId;

    public ReportQueueMessage(UUID playerUuid, String playerName, int reportId) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.reportId = reportId;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getReportId() {
        return reportId;
    }
}
