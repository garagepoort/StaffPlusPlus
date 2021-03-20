package net.shortninja.staffplus.common;

import net.shortninja.staffplus.application.IocContainer;

public class Constants {

    public static final String BUNGEE_CORD_CHANNEL = "BungeeCord";
    public static final String BUNGEE_REPORT_CREATED_CHANNEL = "staffplusplus.report-created";
    public static final String BUNGEE_REPORT_DELETED_CHANNEL = "staffplusplus.report-deleted";
    public static final String BUNGEE_REPORT_ACCEPTED_CHANNEL = "staffplusplus.report-accepted";
    public static final String BUNGEE_REPORT_CLOSED_CHANNEL = "staffplusplus.report-closed";
    public static final String BUNGEE_REPORT_REOPEN_CHANNEL = "staffplusplus.report-reopen";

    public static String getServerNameFilterWithAnd(boolean enabled) {
        return !enabled ? " AND (server_name is null OR server_name='" + IocContainer.getOptions().serverName + "') " : "";
    }

    public static String getServerNameFilter(boolean enabled) {
        return !enabled ? " (server_name is null OR server_name='" + IocContainer.getOptions().serverName + "') " : "";
    }

    public static String getServerNameFilterWithWhere(boolean enabled) {
        return !enabled ? " WHERE (server_name is null OR server_name='" + IocContainer.getOptions().serverName + "') " : "";
    }
}
