package net.shortninja.staffplus;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.shortninja.staffplus.authentication.AuthenticationProvider;
import net.shortninja.staffplus.authentication.AuthenticationService;
import net.shortninja.staffplus.authentication.authme.AuthMeAuthenticationService;
import net.shortninja.staffplus.authentication.authme.NoopAuthenticationService;
import net.shortninja.staffplus.player.OfflinePlayerProvider;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.ext.bukkit.BukkitOfflinePlayerProvider;
import net.shortninja.staffplus.player.ext.bukkit.NoopOfflinePlayerProvider;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.player.UserQueuedActionChatPreventer;
import net.shortninja.staffplus.reporting.ReportService;
import net.shortninja.staffplus.reporting.database.MysqlReportRepository;
import net.shortninja.staffplus.reporting.database.ReportRepository;
import net.shortninja.staffplus.reporting.database.SqliteReportRepository;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.server.chat.ChatHandler;
import net.shortninja.staffplus.server.chat.ChatPreventer;
import net.shortninja.staffplus.server.chat.ChatReceivePreventer;
import net.shortninja.staffplus.server.chat.GeneralChatPreventer;
import net.shortninja.staffplus.server.chat.blacklist.BlacklistService;
import net.shortninja.staffplus.server.chat.blacklist.censors.ChatCensor;
import net.shortninja.staffplus.server.chat.blacklist.censors.DomainChatCensor;
import net.shortninja.staffplus.server.chat.blacklist.censors.IllegalCharactersChatCensor;
import net.shortninja.staffplus.server.chat.blacklist.censors.IllegalWordsChatCensor;
import net.shortninja.staffplus.session.SessionLoader;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.server.data.storage.IStorage;
import net.shortninja.staffplus.server.data.storage.MemoryStorage;
import net.shortninja.staffplus.server.data.storage.MySQLStorage;
import net.shortninja.staffplus.server.data.storage.SqliteStorage;
import net.shortninja.staffplus.staff.delayedactions.DelayedActionsRepository;
import net.shortninja.staffplus.staff.delayedactions.MysqlDelayedActionsRepository;
import net.shortninja.staffplus.staff.delayedactions.SqliteDelayedActionsRepository;
import net.shortninja.staffplus.staff.freeze.FreezeChatPreventer;
import net.shortninja.staffplus.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.staff.staffchat.StaffChatService;
import net.shortninja.staffplus.staff.tracing.TraceChatPreventer;
import net.shortninja.staffplus.staff.tracing.TraceService;
import net.shortninja.staffplus.staff.tracing.TraceWriterFactory;
import net.shortninja.staffplus.staff.vanish.VanishChatPreventer;
import net.shortninja.staffplus.staff.vanish.VanishHandler;
import net.shortninja.staffplus.staff.warn.WarnService;
import net.shortninja.staffplus.staff.warn.database.MysqlWarnRepository;
import net.shortninja.staffplus.staff.warn.database.SqliteWarnRepository;
import net.shortninja.staffplus.staff.warn.database.WarnRepository;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.database.DatabaseType;
import net.shortninja.staffplus.util.database.DatabaseUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class IocContainer {

    public static final String AUTHME = "AUTHME";
    private static ReportRepository reportRepository;
    private static WarnRepository warnRepository;
    private static DelayedActionsRepository delayedActionsRepository;
    private static StaffPlus staffPlus;
    private static IStorage storage;

    private static final Map<Class, Object> beans = new HashMap<>();

    public static void init(StaffPlus staffPlus) {
        IocContainer.staffPlus = staffPlus;
    }

    public static ReportRepository getReportRepository() {
        return initBean(ReportRepository.class, () -> RepositoryFactory.create("REPORT"));
    }

    public static WarnRepository getWarnRepository() {
        return initBean(WarnRepository.class, () -> RepositoryFactory.create("WARN"));
    }

    public static DelayedActionsRepository getDelayedActionsRepository() {
        return initBean(DelayedActionsRepository.class, () -> RepositoryFactory.create("DELAYED_ACTIONS"));
    }

    public static ReportService getReportService() {
        return initBean(ReportService.class, () -> new ReportService(getReportRepository(), getMessages(), getPlayerManager()));
    }

    public static WarnService getWarnService() {
        return initBean(WarnService.class, () -> new WarnService(
            getPermissionHandler(),
            getMessage(),
            getOptions(),
            getMessages(),
            getPlayerManager(),
            getWarnRepository(),
            getDelayedActionsRepository()));
    }

    // Could use a factory but for sake of simplicity of the factory demonstration, not going to do.
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

    public static SessionManager getSessionManager() {
        return initBean(SessionManager.class, () -> new SessionManager(getSessionLoader()));
    }

    public static PlayerManager getPlayerManager() {
        return initBean(PlayerManager.class, () -> new PlayerManager(getOfflinePlayerProvider()));
    }

    public static Messages getMessages() {
        return initBean(Messages.class, Messages::new);
    }

    public static PermissionHandler getPermissionHandler() {
        return initBean(PermissionHandler.class, () -> new PermissionHandler(getOptions()));
    }

    public static MessageCoordinator getMessage() {
        return initBean(MessageCoordinator.class, () -> new MessageCoordinator(staffPlus, getPermissionHandler()));
    }

    public static Options getOptions() {
        return initBean(Options.class, Options::new);
    }

    public static StaffChatService getStaffChatService() {
        return initBean(StaffChatService.class, () -> new StaffChatService(getMessages(), getOptions()));
    }

    public static VanishHandler getVanishHandler() {
        return initBean(VanishHandler.class, () -> new VanishHandler(StaffPlus.get().versionProtocol, getPermissionHandler(),
            getMessage(), getOptions(), getMessages(), getSessionManager()));
    }

    public static ChatHandler getChatHandler() {
        return initBean(ChatHandler.class, () -> new ChatHandler(getPermissionHandler(), getMessage(), getOptions(), getMessages()));
    }

    public static FreezeHandler getFreezeHandler() {
        return initBean(FreezeHandler.class, () -> new FreezeHandler(getPermissionHandler(), getMessage(), getOptions(), getMessages(), getSessionManager()));
    }

    public static TraceService getTraceService() {
        return initBean(TraceService.class, () -> new TraceService(getTraceWriterFactory(), getOptions()));
    }

    public static TraceWriterFactory getTraceWriterFactory() {
        return initBean(TraceWriterFactory.class, () -> new TraceWriterFactory(getMessage(), getMessages(), getOptions()));
    }

    public static SessionLoader getSessionLoader() {
        return initBean(SessionLoader.class, () -> new SessionLoader(getPlayerManager()));
    }

    public static AlertCoordinator getAlertCoordinator() {
        return initBean(AlertCoordinator.class, () -> new AlertCoordinator(getPermissionHandler(), getMessage(), getOptions(), getMessages(), getSessionManager()));
    }

    public static OfflinePlayerProvider getOfflinePlayerProvider() {
        return initBean(OfflinePlayerProvider.class, () -> {
            if(getOptions().offlinePlayersModeEnabled) {
                return new BukkitOfflinePlayerProvider();
            }
            return new NoopOfflinePlayerProvider();
        });
    }

    public static BlacklistService getBlacklistService() {
        return initBean(BlacklistService.class,
            () -> new BlacklistService(getOptions(), getPermissionHandler(), getMessages(), getChatCensors()));
    }

    public static List<ChatCensor> getChatCensors() {
        return Arrays.asList(
            new IllegalWordsChatCensor(getOptions()),
            new IllegalCharactersChatCensor(getOptions()),
            new DomainChatCensor(getOptions())
        );
    }

    public static List<ChatPreventer> getChatPreventers() {
        return Arrays.asList(
            new UserQueuedActionChatPreventer(getSessionManager()),
            new TraceChatPreventer(getTraceService(), getMessages(), getMessage(), getOptions()),
            new FreezeChatPreventer(getFreezeHandler(), getOptions(), getMessages(), getMessage()),
            new VanishChatPreventer(getVanishHandler(), getOptions(), getMessage(), getMessages()),
            new GeneralChatPreventer(getChatHandler(), getMessage(), getMessages())
        );
    }

    public static List<ChatReceivePreventer> getChatReceivePreventers() {
        return Arrays.asList(new TraceChatPreventer(getTraceService(), getMessages(), getMessage(), getOptions()));
    }

    private static <T> T initBean(Class<T> clazz, Supplier<T> consumer) {
        if (!beans.containsKey(clazz)) {
            beans.put(clazz, consumer.get());
        }
        return (T) beans.get(clazz);
    }

    public static AuthenticationService getAuthenticationService() {
        return initBean(AuthenticationService.class,
            () -> {
                AuthenticationProvider authenticationProvider = getOptions().authenticationConfiguration.getAuthenticationProvider();
                if (authenticationProvider == AuthenticationProvider.AUTHME) {
                    return new AuthMeAuthenticationService();
                }
                return new NoopAuthenticationService();
            });
    }

    public static interface Repository {
    }

    private static final class RepositoryFactory {

        private static final Table<String, DatabaseType, Repository> MAP = HashBasedTable.create();

        static {
            MAP.put("WARN", DatabaseType.MYSQL, new MysqlWarnRepository(getPlayerManager()));
            MAP.put("WARN", DatabaseType.SQLITE, new SqliteWarnRepository(getPlayerManager()));
            MAP.put("REPORT", DatabaseType.MYSQL, new MysqlReportRepository(getPlayerManager()));
            MAP.put("REPORT", DatabaseType.SQLITE, new SqliteReportRepository(getPlayerManager()));
            MAP.put("DELAYED_ACTIONS", DatabaseType.MYSQL, new MysqlDelayedActionsRepository());
            MAP.put("DELAYED_ACTIONS", DatabaseType.SQLITE, new SqliteDelayedActionsRepository());
        }

        @SuppressWarnings("unchecked")
        public static <T extends Repository> T create(String type) {
            if (type == null) {
                throw new IllegalArgumentException("Type may not be null.");
            }

            final DatabaseType dbType = DatabaseUtil.database().getType();

            if (!MAP.contains(type, dbType)) {
                throw new IllegalStateException("No repository registered for type.");
            }

            return (T) MAP.get(type, dbType);
        }
    }
}
