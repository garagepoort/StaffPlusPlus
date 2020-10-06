package be.garagepoort.staffplusplus.trello.reports;

public class Report {

    private int id;
    private int sppId;
    private String trelloId;

    public Report(int id, int sppId, String trelloId) {
        this.id = id;
        this.sppId = sppId;
        this.trelloId = trelloId;
    }

    public Report(int sppId, String trelloId) {
        this.sppId = sppId;
        this.trelloId = trelloId;
    }

    public int getId() {
        return id;
    }

    public int getSppId() {
        return sppId;
    }

    public String getTrelloId() {
        return trelloId;
    }
}
