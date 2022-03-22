package net.shortninja.staffplus.core.common.utils;

import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.common.SqlFilter;
import net.shortninja.staffplusplus.common.SqlFilters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
        DatabaseUtil.getUuids(tableName, uuid_column).stream()
            .filter(Objects::nonNull)
            .forEach(playerUuid -> playerManager.getOnOrOfflinePlayer(playerUuid)
                .ifPresent(p -> statements.add(String.format("UPDATE " + tableName + " set " + nameColumn + "='%s' WHERE " + uuid_column + "='%s';", p.getUsername(), p.getId()))));
        return statements;
    }

    public static List<UUID> getUuids(String table, String column) {
        QueryBuilderFactory query = StaffPlus.get().getIocContainer().get(QueryBuilderFactory.class);
        return query.create().find("SELECT distinct " + column + " FROM " + table + " WHERE " + column + " is not null", rs -> UUID.fromString(rs.getString(1)));
    }
}
