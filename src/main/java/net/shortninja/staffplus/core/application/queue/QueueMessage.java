package net.shortninja.staffplus.core.application.queue;

public class QueueMessage {

    private final int id;
    private final QueueStatus status;
    private final String type;
    private final String data;
    private final String statusMessage;
    private final long timestamp;

    public QueueMessage(int id, QueueStatus status, String type, String data, String statusMessage, long timestamp) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.data = data;
        this.statusMessage = statusMessage;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public QueueStatus getStatus() {
        return status;
    }

    public String getData() {
        return data;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }
}
