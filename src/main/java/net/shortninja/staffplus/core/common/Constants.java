package net.shortninja.staffplus.core.common;

import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Constants {

    public static UUID CONSOLE_UUID = UUID.fromString("9c417515-22bc-46b8-be4d-538482992f8f");
    public static final String BUNGEE_CORD_CHANNEL = "BungeeCord";
    public static final String BUNGEE_REPORT_CREATED_CHANNEL = "staffplusplus.report-created";
    public static final String BUNGEE_REPORT_DELETED_CHANNEL = "staffplusplus.report-deleted";
    public static final String BUNGEE_REPORT_ACCEPTED_CHANNEL = "staffplusplus.report-accepted";
    public static final String BUNGEE_REPORT_CLOSED_CHANNEL = "staffplusplus.report-closed";
    public static final String BUNGEE_REPORT_REOPEN_CHANNEL = "staffplusplus.report-reopen";

    public static final String BUNGEE_PLAYER_BANNED_CHANNEL = "staffplusplus.banned-player";
    public static final String BUNGEE_PLAYER_UNBANNED_CHANNEL = "staffplusplus.unbanned-player";

    public static final String BUNGEE_IP_BANNED_CHANNEL = "staffplusplus.banned-ip";
    public static final String BUNGEE_IP_UNBANNED_CHANNEL = "staffplusplus.unbanned-ip";

    public static final String BUNGEE_STAFFCHAT_CHANNEL = "StaffPlusPlusChat";
    public static final String BUNGEE_CHATCHANNELS_MESSAGE_SEND_CHANNEL = "staffplusplus.chatchannels.message-send";
    public static final String BUNGEE_CHATCHANNELS_CLOSED_CHANNEL = "staffplusplus.chatchannels.closed";
    public static final String BUNGEE_CHATCHANNELS_CREATED_CHANNEL = "staffplusplus.chatchannels.created";
    public static final String BUNGEE_CHATCHANNELS_PLAYER_JOINED_CHANNEL = "staffplusplus.chatchannels.player-joined";
    public static final String BUNGEE_CHATCHANNELS_PLAYER_LEFT_CHANNEL = "staffplusplus.chatchannels.player-left";

    public static final String BUNGEE_INVESTIGATION_STARTED_CHANNEL = "staffplusplus.investigation-started";
    public static final String BUNGEE_INVESTIGATION_PAUSED_CHANNEL = "staffplusplus.investigation-paused";
    public static final String BUNGEE_INVESTIGATION_CONCLUDED_CHANNEL = "staffplusplus.investigation-concluded";

    public static final String BUNGEE_NAME_CHANGED_ALERT_CHANNEL = "staffplusplus.alerts.name-change";
    public static final String BUNGEE_MENTION_ALERT_CHANNEL = "staffplusplus.alerts.mention";
    public static final String BUNGEE_XRAY_ALERT_CHANNEL = "staffplusplus.alerts.xray";
    public static final String BUNGEE_BLACKLIST_ALERT_CHANNEL = "staffplusplus.alerts.blacklist";
    public static final String BUNGEE_PHRASE_DETECTION_ALERT_CHANNEL = "staffplusplus.alerts.phrase-detection";

    public static String getServerNameFilterWithAnd(ServerSyncConfig serverSyncConfig) {
        return getServerNameFilterWithAnd("", serverSyncConfig);
    }

    public static String getServerNameFilterWithAnd(String tableName, ServerSyncConfig serverSyncConfig) {
        String tableString = !tableName.isEmpty() ? tableName + "." : "";
        if (serverSyncConfig.isMatchesAll()) {
            return "";
        }
        List<String> servers = serverSyncConfig.getServers();
        String serverQueryString = servers.stream().map(s -> "'" + s + "'").collect(Collectors.joining(","));
        return " AND (" + tableString + "server_name is null OR " + tableString + "server_name IN (" + serverQueryString + ")) ";
    }

    public static String getServerNameFilterWithWhere(ServerSyncConfig serverSyncConfig) {
        if (serverSyncConfig.isMatchesAll()) {
            return "";
        }
        List<String> servers = serverSyncConfig.getServers();
        String serverQueryString = servers.stream().map(s -> "'" + s + "'").collect(Collectors.joining(","));
        return " WHERE (server_name is null OR server_name IN (" + serverQueryString + ")) ";
    }

    public static String getServerNameFilterWithWhere(String tableName, ServerSyncConfig serverSyncConfig) {
        String tableString = !tableName.isEmpty() ? tableName + "." : "";
        if (serverSyncConfig.isMatchesAll()) {
            return "";
        }
        List<String> servers = serverSyncConfig.getServers();
        String serverQueryString = servers.stream().map(s -> "'" + s + "'").collect(Collectors.joining(","));
        return " WHERE (" + tableString + "server_name is null OR " + tableString + "server_name IN (" + serverQueryString + ")) ";
    }
}
