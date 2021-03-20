package net.shortninja.staffplus.application.database.migrations;

public interface Migration {

    String getStatement();

    int getVersion();
}
