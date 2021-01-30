package net.shortninja.staffplus.common;

import net.shortninja.staffplus.IocContainer;

public class Constants {

    public static final String BUNGEE_CORD_CHANNEL = "BungeeCord";

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
