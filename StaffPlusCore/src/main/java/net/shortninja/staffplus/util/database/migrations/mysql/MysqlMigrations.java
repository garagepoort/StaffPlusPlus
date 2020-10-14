package net.shortninja.staffplus.util.database.migrations.mysql;

import net.shortninja.staffplus.util.database.migrations.Migration;
import net.shortninja.staffplus.util.database.migrations.SqlMigrations;
import net.shortninja.staffplus.util.database.migrations.common.*;
import org.bukkit.Bukkit;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MysqlMigrations implements SqlMigrations {

    private static MysqlMigrations instance;
    // Define all migrations in this list.
    private static final List<Migration> migrations = Arrays.asList(
        new V1_CreateReportsTableMigration(),
        new V2_CreateWarningsTableMigration(),
        new V3_CreateAlertOptionsTableMigration(),
        new V4_CreatePlayerDataTableMigration(),
        new V5_CreateTicketsTableMigration(),
        new V6_CreateCommandsTableMigration(),
        new V7_AlterPlayerDataTableAddPasswordMigration(),
        new V8_AlterReportTableAddStatusMigration(),
        new V9_AlterReportTableAddTimestampMigration(),
        new V10_AlterReportTableAddStaffNameMigration(),
        new V11_AlterReportTableAddStaffUuidMigration(),
        new V12_AlterReportTablePlayerUuidNullableMigration(),
        new V13_AlterWarningsTableAddScoreMigration(),
        new V14_AlterWarningsTableAddSeverityMigration(),
        new V15_CreateDelayedActionsTableMigration(),
        new V16_AlterDelayedActionsTableAddTimestampMigration(),
        new V17_AlterReportTableAddCloseReasonMigration(),
        new V18_CreateLocationsTableMigration(),
        new V19_CreateProtectedAreasTableMigration(),
        new V20_CreateBannedPlayersTableMigration(),
        new V21_CreatePlayerIpsTableMigration(),
        new V22_CreateAltDetectWhitelistTableMigration(),
        new V23_AlterWarningTableAddReadMigration(),
        new V24_AlterWarningTableAddTimestampMigration());

    private final DataSource datasource;

    private MysqlMigrations() {
        datasource = MySQLConnection.getDatasource();
    }

    public static MysqlMigrations getInstance() {
        if (instance == null) {
            instance = new MysqlMigrations();
        }
        return instance;
    }

    @Override
    public void createMigrationTable() {
        try (Connection connect = datasource.getConnection();
             Statement stmt = connect.createStatement()) {
            Bukkit.getLogger().info("Creating migration table");

            String sql = "CREATE TABLE IF NOT EXISTS migrations (\n"
                + "	id BIGINT PRIMARY KEY AUTO_INCREMENT,\n"
                + "	version integer NOT NULL\n"
                + ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failure creating migration table: " + e.getMessage());
        }

    }

    @Override
    public void runMigrations() {
        try (Connection connect = datasource.getConnection()) {
            Bukkit.getLogger().info("Starting migrations");
            connect.setAutoCommit(false);
            int maxVersion = getMaxVersion();

            List<Migration> validMigrations = migrations.stream().filter(m -> m.getVersion() > maxVersion)
                .sorted(Comparator.comparingInt(Migration::getVersion))
                .collect(Collectors.toList());

            for (Migration migration : validMigrations) {
                try (Statement stmt = connect.createStatement()) {
                    String sql = migration.getStatement();
                    stmt.execute(sql);

                    PreparedStatement migrationStatement = connect.prepareStatement("INSERT INTO migrations (version) VALUES (?);");
                    migrationStatement.setInt(1, migration.getVersion());
                    migrationStatement.execute();

                    connect.commit();
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failure executing migrations: " + e.getMessage());
        }
    }

    private int getMaxVersion() {
        try (Connection connect = datasource.getConnection();
             Statement stmt = connect.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT max(version) as max from migrations");
            int max = resultSet.next() ? resultSet.getInt("max") : 0;
            Bukkit.getLogger().info("Latest migration version = " + max);
            return max;
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failure retrieving max migration version: " + e.getMessage());
        }
        return 0;
    }
}
