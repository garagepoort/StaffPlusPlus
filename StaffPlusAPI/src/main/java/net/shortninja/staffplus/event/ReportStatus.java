package net.shortninja.staffplus.event;

public enum ReportStatus {
    OPEN,
    RESOLVED,
    EXPIRED,
    IN_PROGRESS,
    REJECTED;

    public boolean isClosed() {
        return this == RESOLVED || this == EXPIRED || this == REJECTED;
    }
}
