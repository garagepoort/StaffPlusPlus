package net.shortninja.staffplus.core.common.bungee;

public class BungeeMessage {

    private final String serverName;
    private Long timestamp;

    public BungeeMessage(String serverName) {
        this.serverName = serverName;
        this.timestamp = System.currentTimeMillis();
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getServerName() {
        return serverName;
    }
}
