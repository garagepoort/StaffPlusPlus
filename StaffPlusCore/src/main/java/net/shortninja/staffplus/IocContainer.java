package net.shortninja.staffplus;

import net.shortninja.staffplus.player.UserQueuedActionChatPreventer;
import net.shortninja.staffplus.staff.delayedactions.DelayedActionsRepository;
import net.shortninja.staffplus.staff.delayedactions.MysqlDelayedActionsRepository;
import net.shortninja.staffplus.staff.delayedactions.SqliteDelayedActionsRepository;
import net.shortninja.staffplus.staff.freeze.FreezeChatPreventer;
import net.shortninja.staffplus.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.reporting.ReportService;
import net.shortninja.staffplus.reporting.database.MysqlReportRepository;
import net.shortninja.staffplus.reporting.database.ReportRepository;
import net.shortninja.staffplus.reporting.database.SqliteReportRepository;
import net.shortninja.staffplus.server.chat.ChatHandler;
import net.shortninja.staffplus.server.chat.ChatPreventer;
import net.shortninja.staffplus.server.chat.GeneralChatPreventer;
import net.shortninja.staffplus.server.chat.blacklist.BlacklistService;
import net.shortninja.staffplus.server.chat.blacklist.censors.DomainChatCensor;
import net.shortninja.staffplus.server.chat.blacklist.censors.IllegalCharactersChatCensor;
import net.shortninja.staffplus.server.chat.blacklist.censors.IllegalWordsChatCensor;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.server.data.storage.IStorage;
import net.shortninja.staffplus.server.data.storage.MemoryStorage;
import net.shortninja.staffplus.server.data.storage.MySQLStorage;
import net.shortninja.staffplus.server.data.storage.SqliteStorage;
import net.shortninja.staffplus.staff.staffchat.StaffChatService;
import net.shortninja.staffplus.staff.vanish.VanishChatPreventer;
import net.shortninja.staffplus.staff.vanish.VanishHandler;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.database.DatabaseType;
import net.shortninja.staffplus.util.database.DatabaseUtil;
import net.shortninja.staffplus.staff.warn.WarnService;
import net.shortninja.staffplus.staff.warn.database.MysqlWarnRepository;
import net.shortninja.staffplus.staff.warn.database.SqliteWarnRepository;
import net.shortninja.staffplus.staff.warn.database.WarnRepository;

import java.util.Arrays;
import java.util.List;

public class IocContainer {

    private static ReportService reportService;
    private static ReportRepository reportRepository;
    private static WarnRepository warnRepository;
    private static DelayedActionsRepository delayedActionsRepository;
    private static UserManager userManager;
    private static StaffPlus staffPlus;
    private static Messages messages;
    private static PermissionHandler permissionHandler;
    private static Options options;
    private static IStorage storage;
    private static WarnService warnService;
    private static MessageCoordinator message;
    private static StaffChatService staffChatService;

    public static void init(StaffPlus staffPlus) {
        IocContainer.staffPlus = staffPlus;
    }

    public static ReportRepository getReportRepository() {
        if (reportRepository == null) {
            if (DatabaseUtil.database().getType() == DatabaseType.MYSQL) {
                reportRepository = new MysqlReportRepository(getUserManager());
            } else if (DatabaseUtil.database().getType() == DatabaseType.SQLITE) {
                reportRepository = new SqliteReportRepository(getUserManager());
            } else {
                throw new RuntimeException("Unsupported database type");
            }
        }
        return reportRepository;
    }

    public static WarnRepository getWarnRepository() {
        if (warnRepository == null) {
            if (DatabaseUtil.database().getType() == DatabaseType.MYSQL) {
                warnRepository = new MysqlWarnRepository(getUserManager());
            } else if (DatabaseUtil.database().getType() == DatabaseType.SQLITE) {
                warnRepository = new SqliteWarnRepository(getUserManager());
            } else {
                throw new RuntimeException("Unsupported database type");
            }
        }
        return warnRepository;
    }

    public static DelayedActionsRepository getDelayedActionsRepository() {
        if (delayedActionsRepository == null) {
            if (DatabaseUtil.database().getType() == DatabaseType.MYSQL) {
                delayedActionsRepository = new MysqlDelayedActionsRepository();
            } else if (DatabaseUtil.database().getType() == DatabaseType.SQLITE) {
                delayedActionsRepository = new SqliteDelayedActionsRepository();
            } else {
                throw new RuntimeException("Unsupported database type");
            }
        }
        return delayedActionsRepository;
    }

    public static ReportService getReportService() {
        if (reportService == null) {
            reportService = new ReportService(getReportRepository(), getUserManager(), getMessages());
        }
        return reportService;
    }

    public static WarnService getWarnService() {
        if (warnService == null) {
            warnService = new WarnService(
                getPermissionHandler(),
                getMessage(),
                getOptions(),
                getMessages(),
                getUserManager(),
                getWarnRepository(),
                getDelayedActionsRepository());
        }
        return warnService;
    }

    public static IStorage getStorage() {
        if (storage == null) {
            if (DatabaseUtil.database().getType() == DatabaseType.MYSQL) {
                storage = new MySQLStorage();
            } else if (DatabaseUtil.database().getType() == DatabaseType.SQLITE) {
                storage = new SqliteStorage();
            } else {
                storage = new MemoryStorage();
            }
        }
        return storage;
    }

    public static UserManager getUserManager() {
        if (userManager == null) {
            userManager = new UserManager(staffPlus, getOptions());
        }
        return userManager;
    }

    public static Messages getMessages() {
        if (messages == null) {
            messages = new Messages();
        }
        return messages;
    }

    public static PermissionHandler getPermissionHandler() {
        if (permissionHandler == null) {
            permissionHandler = new PermissionHandler(getOptions());
        }
        return permissionHandler;
    }

    public static MessageCoordinator getMessage() {
        if (message == null) {
            message = new MessageCoordinator(staffPlus, getPermissionHandler());
        }
        return message;
    }

    public static Options getOptions() {
        if (options == null) {
            options = new Options();
        }
        return options;
    }

    public static StaffChatService getStaffChatService() {
        if (staffChatService == null) {
            staffChatService = new StaffChatService(getMessages(), getOptions());
        }
        return staffChatService;
    }

    public static BlacklistService getBlacklistService() {
        return new BlacklistService(getOptions(), getPermissionHandler(), getMessages(), Arrays.asList(
            new IllegalWordsChatCensor(getOptions()),
            new IllegalCharactersChatCensor(getOptions()),
            new DomainChatCensor(getOptions())
        ));
    }

    public static List<ChatPreventer> getChatPreventers() {
        return Arrays.asList(
            new UserQueuedActionChatPreventer(getUserManager()),
            new FreezeChatPreventer(getFreezeHandler(), getOptions(), getMessages(), getMessage()),
            new VanishChatPreventer(getVanishHandler(), getOptions(), getMessage(), getMessages()),
            new GeneralChatPreventer(getChatHandler(), getMessage(), getMessages())
        );
    }

    public static VanishHandler getVanishHandler() {
        return new VanishHandler(StaffPlus.get().versionProtocol, getPermissionHandler(),
            getMessage(), getOptions(), getMessages(), getUserManager());
    }

    public static ChatHandler getChatHandler() {
        return new ChatHandler(getPermissionHandler(), getMessage(), getOptions(), getMessages());
    }

    public static FreezeHandler getFreezeHandler() {
        return new FreezeHandler(getPermissionHandler(), getMessage(), getOptions(), getMessages(), getUserManager());
    }
}
