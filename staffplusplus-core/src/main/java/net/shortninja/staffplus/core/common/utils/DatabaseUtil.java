package net.shortninja.staffplus.core.common.utils;

import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.common.ISqlFilter;
import net.shortninja.staffplusplus.common.OrSqlFilter;
import net.shortninja.staffplusplus.common.SqlFilter;
import net.shortninja.staffplusplus.common.SqlFilters;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
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
                if (r instanceof OrSqlFilter) {
                    OrSqlFilter orSqlFilter = (OrSqlFilter) r;
                    return " AND (" + mapFilterToQuery(orSqlFilter.getLeft()) + " OR " + mapFilterToQuery(orSqlFilter.getRight()) + " )";
                }
                return " AND " + mapFilterToQuery((SqlFilter) r);
            })
            .collect(Collectors.joining());

        if (prefixAnd) {
            return collect;
        }
        return collect.replaceFirst(" AND", "");
    }

    @NotNull
    private static String mapFilterToQuery(SqlFilter r) {
        if (r.getValue() == null) {
            return r.getSqlColumn() + " is null";
        }
        if (r.getValue() instanceof Collection) {
            List<String> questionMarks = ((Collection<String>) r.getValue()).stream().map(p -> "?").collect(Collectors.toList());
            return r.getSqlColumn() + " in (" + String.join(", ", questionMarks) + ")";
        }
        return r.getSqlColumn() + r.getOperator() + "? ";
    }

    public static int insertFilterValues(SqlFilters sqlFilters, PreparedStatement ps, int index) throws SQLException {
        for (ISqlFilter sqlFilter : sqlFilters.getSqlFilters()) {
            if (sqlFilter instanceof SqlFilter) {
                index = insertSqlFilter(ps, index, (SqlFilter) sqlFilter);
            }
            if (sqlFilter instanceof OrSqlFilter) {
                OrSqlFilter orSqlFilter = (OrSqlFilter) sqlFilter;
                index = insertSqlFilter(ps, index, orSqlFilter.getLeft());
                index = insertSqlFilter(ps, index, orSqlFilter.getRight());
            }
        }
        return index;
    }

    private static int insertSqlFilter(PreparedStatement ps, int currentIndex, SqlFilter sqlFilter) throws SQLException {
        int newIndex = currentIndex;
        if (sqlFilter.getValue() == null) {
            return newIndex;
        } else if (sqlFilter.getValue() instanceof Collection) {
            Collection<String> collection = (Collection<String>) sqlFilter.getValue();
            for (String value : collection) {
                ps.setObject(newIndex, value, sqlFilter.getSqlType());
                newIndex++;
            }
            return newIndex;
        } else {
            ps.setObject(newIndex, sqlFilter.getValue(), sqlFilter.getSqlType());
            return newIndex + 1;
        }
    }

    public static List<String> createMigrateNameStatements(Connection connection, PlayerManager playerManager, QueryBuilderFactory query, String tableName, String nameColumn, String uuid_column) {
        List<String> statements = new ArrayList<>();
        DatabaseUtil.getUuids(connection, query, tableName, uuid_column).stream()
            .filter(Objects::nonNull)
            .forEach(playerUuid -> playerManager.getOnOrOfflinePlayer(playerUuid)
                .ifPresent(p -> statements.add(String.format("UPDATE " + tableName + " set " + nameColumn + "='%s' WHERE " + uuid_column + "='%s';", p.getUsername(), p.getId()))));
        return statements;
    }

    public static List<UUID> getUuids(Connection connection, QueryBuilderFactory query, String table, String column) {
        return query.create(connection).find("SELECT distinct " + column + " FROM " + table + " WHERE " + column + " is not null", rs -> UUID.fromString(rs.getString(1)));
    }
}
