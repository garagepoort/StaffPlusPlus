package net.shortninja.staffplus.core.common.utils;

import net.shortninja.staffplusplus.common.SqlFilter;
import net.shortninja.staffplusplus.common.SqlFilters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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

        if(prefixAnd) {
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
}
