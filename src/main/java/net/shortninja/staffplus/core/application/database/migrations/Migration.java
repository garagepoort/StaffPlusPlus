package net.shortninja.staffplus.core.application.database.migrations;

public interface Migration {

    String getStatement();

    int getVersion();
}
