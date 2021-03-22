package net.shortninja.staffplus.core.application.database.migrations;

import javax.sql.DataSource;
import java.sql.Connection;

public interface SqlConnectionProvider {

    Connection getConnection();

    DataSource getDatasource();
}
