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
import net.shortninja.staffplus.server.chat.*;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.player.ChatActionChatInterceptor;
import net.shortninja.staffplus.staff.ban.BanService;
import net.shortninja.staffplus.staff.ban.database.BansRepository;
import net.shortninja.staffplus.staff.ban.database.MysqlBansRepository;
import net.shortninja.staffplus.staff.ban.database.SqliteBansRepository;
import net.shortninja.staffplus.staff.broadcast.BroadcastService;
import net.shortninja.staffplus.staff.location.LocationRepository;
import net.shortninja.staffplus.staff.location.MysqlLocationRepository;
import net.shortninja.staffplus.staff.location.SqliteLocationRepository;
import net.shortninja.staffplus.staff.mode.ModeCoordinator;
import net.shortninja.staffplus.staff.protect.ProtectService;
import net.shortninja.staffplus.staff.protect.database.MysqlProtectedAreaRepository;
import net.shortninja.staffplus.staff.protect.database.ProtectedAreaRepository;
import net.shortninja.staffplus.staff.protect.database.SqliteProtectedAreaRepository;
import net.shortninja.staffplus.staff.reporting.ReportService;
import net.shortninja.staffplus.staff.reporting.database.MysqlReportRepository;
import net.shortninja.staffplus.staff.reporting.database.ReportRepository;
import net.shortninja.staffplus.staff.reporting.database.SqliteReportRepository;
import net.shortninja.staffplus.server.AlertCoordinator;
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
import net.shortninja.staffplus.staff.freeze.FreezeChatInterceptor;
import net.shortninja.staffplus.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.staff.staffchat.StaffChatChatInterceptor;
import net.shortninja.staffplus.staff.staffchat.StaffChatService;
import net.shortninja.staffplus.staff.teleport.TeleportService;
import net.shortninja.staffplus.staff.tracing.TraceChatInterceptor;
import net.shortninja.staffplus.staff.tracing.TraceService;
import net.shortninja.staffplus.staff.tracing.TraceWriterFactory;
import net.shortninja.staffplus.staff.vanish.VanishChatInterceptor;
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

    public static LocationRepository getLocationsRepository() {
        return initBean(LocationRepository.class, () -> RepositoryFactory.create("LOCATIONS"));
    }

    public static ProtectedAreaRepository getProtectedAreaRepository() {
        return initBean(ProtectedAreaRepository.class, () -> RepositoryFactory.create("PROTECTED_AREAS"));
    }

    public static DelayedActionsRepository getDelayedActionsRepository() {
        return initBean(DelayedActionsRepository.class, () -> RepositoryFactory.create("DELAYED_ACTIONS"));
    }

    public static BanService getBanService() {
        return initBean(BanService.class, () -> new BanService(getPermissionHandler(), getBansRepository(), getOptions(), getMessage(), getMessages()));
    }

    public static BansRepository getBansRepository() {
        return initBean(BansRepository.class, () -> RepositoryFactory.create("BANS"));
    }

    public static ReportService getReportService() {
        return initBean(ReportService.class, () -> new ReportService(getReportRepository(), getMessages(), getPlayerManager()));
    }

    public static ProtectService getProtectService() {
        return initBean(ProtectService.class, () -> new ProtectService(getProtectedAreaRepository(), getMessage(), getModeCoordinator(), getMessages(), getOptions()));
    }

    public static TeleportService getTeleportService() {
        return initBean(TeleportService.class, () -> new TeleportService(getOptions()));
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

    public static IStorage getStorage() {
        if (DatabaseUtil.database().getType() == DatabaseType.MYSQL) {
            return initBean(IStorage.class, MySQLStorage::new);
        }
        if (DatabaseUtil.database().getType() == DatabaseType.SQLITE) {
            return initBean(IStorage.class, SqliteStorage::new);
        }
        return initBean(IStorage.class, MemoryStorage::new);
    }

    public static SessionManager getSessionManager() {
        return initBean(SessionManager.class, () -> new SessionManager(getSessionLoader()));
    }

    public static ModeCoordinator getModeCoordinator() {
        return initBean(ModeCoordinator.class, () -> new ModeCoordinator(getMessage(), getOptions(), getMessages(), getSessionManager(), getVanishHandler()));
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

    public static BroadcastService getBroadcastService() {
        return initBean(BroadcastService.class, () -> new BroadcastService(getMessage(), getOptions()));
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
            if (getOptions().offlinePlayersModeEnabled) {
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

    public static List<ChatInterceptor> getChatInterceptors() {
        return Arrays.asList(
            new ChatActionChatInterceptor(getSessionManager()),
            new StaffChatChatInterceptor(getStaffChatService(), getPermissionHandler(), getOptions(), getSessionManager()),
            new TraceChatInterceptor(getTraceService(), getMessages(), getMessage(), getOptions()),
            new FreezeChatInterceptor(getFreezeHandler(), getOptions(), getMessages(), getMessage()),
            new VanishChatInterceptor(getVanishHandler(), getOptions(), getMessage(), getMessages()),
            new GeneralChatInterceptor(getChatHandler(), getMessage(), getMessages())
        );
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
            MysqlLocationRepository mysqlLocationRepository = new MysqlLocationRepository();
            SqliteLocationRepository sqliteLocationRepository = new SqliteLocationRepository();

            MAP.put("WARN", DatabaseType.MYSQL, new MysqlWarnRepository(getPlayerManager()));
            MAP.put("WARN", DatabaseType.SQLITE, new SqliteWarnRepository(getPlayerManager()));
            MAP.put("REPORT", DatabaseType.MYSQL, new MysqlReportRepository(getPlayerManager()));
            MAP.put("REPORT", DatabaseType.SQLITE, new SqliteReportRepository(getPlayerManager()));
            MAP.put("DELAYED_ACTIONS", DatabaseType.MYSQL, new MysqlDelayedActionsRepository());
            MAP.put("DELAYED_ACTIONS", DatabaseType.SQLITE, new SqliteDelayedActionsRepository());
            MAP.put("LOCATIONS", DatabaseType.MYSQL, mysqlLocationRepository);
            MAP.put("LOCATIONS", DatabaseType.SQLITE, sqliteLocationRepository);
            MAP.put("PROTECTED_AREAS", DatabaseType.MYSQL, new MysqlProtectedAreaRepository(mysqlLocationRepository));
            MAP.put("PROTECTED_AREAS", DatabaseType.SQLITE, new SqliteProtectedAreaRepository(sqliteLocationRepository));
            MAP.put("BANS", DatabaseType.MYSQL, new MysqlBansRepository(getPlayerManager()));
            MAP.put("BANS", DatabaseType.SQLITE, new SqliteBansRepository(getPlayerManager()));
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
