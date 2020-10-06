package be.garagepoort.staffplusplus.trello.api;

public class TrelloCardResponse {

    private String id;

    public TrelloCardResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
