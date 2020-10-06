package be.garagepoort.staffplusplus.trello.repository.database.migrations.sqlite;

import be.garagepoort.staffplusplus.trello.repository.database.migrations.Migration;

public class V1_CreateReportsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS spp_trello_reports (  ID integer PRIMARY KEY,  spp_id integer NOT NULL,  trello_id VARCHAR(128) NOT NULL)";
    }

    @Override
    public int getVersion() {
        return 1;
    }
}
