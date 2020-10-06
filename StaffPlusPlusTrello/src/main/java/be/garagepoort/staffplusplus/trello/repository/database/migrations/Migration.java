package be.garagepoort.staffplusplus.trello.repository.database.migrations;

public interface Migration {

    String getStatement();

    int getVersion();
}
