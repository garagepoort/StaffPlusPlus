package net.shortninja.staffplus.core.domain.staff.reporting.queue.dto;

import net.shortninja.staffplus.core.domain.staff.reporting.CloseReportRequest;

import java.util.UUID;

public class ReportClosedQueueMessage {

    private final UUID playerUuid;
    private final String playerName;
    private final CloseReportRequest closeReportRequest;

    public ReportClosedQueueMessage(UUID playerUuid, String playerName, CloseReportRequest closeReportRequest) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.closeReportRequest = closeReportRequest;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public CloseReportRequest getCloseReportRequest() {
        return closeReportRequest;
    }
}
