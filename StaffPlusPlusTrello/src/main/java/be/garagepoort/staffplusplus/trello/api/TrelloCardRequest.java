package be.garagepoort.staffplusplus.trello.api;

public class TrelloCardRequest {
    private String name;
    private String idList;
    private String desc;

    public TrelloCardRequest(String name, String idList, String desc) {
        this.name = name;
        this.idList = idList;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getIdList() {
        return idList;
    }

    public String getDesc() {
        return desc;
    }
}
