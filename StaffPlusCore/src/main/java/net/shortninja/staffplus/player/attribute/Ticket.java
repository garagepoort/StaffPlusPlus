package net.shortninja.staffplus.player.attribute;

import java.util.UUID;

public class Ticket {
    private UUID uuid;
    private String name;
    private int id;
    private String inquiry;
    private String handlerName = "";
    private boolean hasBeenClosed = false;

    public Ticket(UUID uuid, String name, int id, String inquiry) {
        this.uuid = uuid;
        this.name = name;
        this.id = id;
        this.inquiry = inquiry;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getInquiry() {
        return inquiry;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public boolean isOpen() {
        return handlerName.isEmpty();
    }

    public boolean hasBeenClosed() {
        return hasBeenClosed;
    }

    public void setHasBeenClosed(boolean hasBeenClosed) {
        this.hasBeenClosed = hasBeenClosed;
    }
}