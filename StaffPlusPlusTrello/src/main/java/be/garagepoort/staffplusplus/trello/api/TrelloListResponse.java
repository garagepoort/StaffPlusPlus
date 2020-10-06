package be.garagepoort.staffplusplus.trello.api;

public class TrelloListResponse {
    private String id;
    private String name;
    private boolean closed;

    public TrelloListResponse(String id, String name, boolean closed) {
        this.id = id;
        this.name = name;
        this.closed = closed;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isClosed() {
        return closed;
    }
}
