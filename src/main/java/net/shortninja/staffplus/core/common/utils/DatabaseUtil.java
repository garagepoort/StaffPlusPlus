package net.shortninja.staffplus.core.common.utils;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.common.SqlFilter;
import net.shortninja.staffplusplus.common.SqlFilters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseUtil {

    public static String mapFilters(SqlFilters sqlFilters, boolean prefixAnd) {
        String collect = sqlFilters.getSqlFilters().stream()
            .map(r -> {
                if (r.getValue() instanceof Collection) {
                    List<String> questionMarks = ((Collection<String>) r.getValue()).stream().map(p -> "?").collect(Collectors.toList());
                    return " AND " + r.getSqlColumn() + " in (" + String.join(", ", questionMarks) + ")";
                }
                return " AND " + r.getSqlColumn() + r.getOperator() + "? ";
            })
            .collect(Collectors.joining());

        if (prefixAnd) {
            return collect;
        }
        return collect.replaceFirst(" AND", "");
    }

    public static int insertFilterValues(SqlFilters sqlFilters, PreparedStatement ps, int index) throws SQLException {
        for (SqlFilter reportFilter : sqlFilters.getSqlFilters()) {
            if (reportFilter.getValue() instanceof Collection) {
                Collection<String> collection = (Collection<String>) reportFilter.getValue();
                for (String value : collection) {
                    ps.setObject(index, value, reportFilter.getSqlType());
                    index++;
                }
            } else {
                ps.setObject(index, reportFilter.getValue(), reportFilter.getSqlType());
                index++;
            }
        }
        return index;
    }

    public static List<String> createMigrateNameStatements(PlayerManager playerManager, String tableName, String nameColumn, String uuid_column) {
        List<String> statements = new ArrayList<>();
        List<String> playerUuids = DatabaseUtil.getUuids(tableName, uuid_column);
        playerUuids
            .forEach(playerUuid -> playerManager.getOnOrOfflinePlayer(playerUuid)
            .ifPresent(p -> statements.add(String.format("UPDATE " + tableName + " set " + nameColumn + "='%s' WHERE " + uuid_column + "='%s';", p.getUsername(), p.getId()))));
        return statements;
    }

    public static List<String> getUuids(String table, String column) {

        SqlConnectionProvider sqlConnectionProvider = StaffPlus.get().getIocContainer().get(SqlConnectionProvider.class);

        List<String> uuids = new ArrayList<>();
        try (Connection sql = sqlConnectionProvider.getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT distinct " + column + " FROM " + table)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    uuids.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return uuids;
    }
}
