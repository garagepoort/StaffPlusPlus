package net.shortninja.staffplus.util.database.migrations;

public interface Migration {

    String getStatement();

    int getVersion();
}
