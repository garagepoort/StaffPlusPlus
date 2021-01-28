package net.shortninja.staffplus;

import net.shortninja.staffplus.authentication.AuthenticationProvider;
import net.shortninja.staffplus.authentication.AuthenticationService;
import net.shortninja.staffplus.authentication.authme.AuthMeAuthenticationService;
import net.shortninja.staffplus.authentication.authme.NoopAuthenticationService;
import net.shortninja.staffplus.common.bungee.BungeeClient;
import net.shortninja.staffplus.player.ChatActionChatInterceptor;
import net.shortninja.staffplus.player.OfflinePlayerProvider;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.ext.bukkit.BukkitOfflinePlayerProvider;
import net.shortninja.staffplus.player.ext.bukkit.NoopOfflinePlayerProvider;
import net.shortninja.staffplus.server.chat.ChatHandler;
import net.shortninja.staffplus.server.chat.ChatInterceptor;
import net.shortninja.staffplus.server.chat.GeneralChatInterceptor;
import net.shortninja.staffplus.server.chat.blacklist.BlacklistService;
import net.shortninja.staffplus.server.chat.blacklist.censors.ChatCensor;
import net.shortninja.staffplus.server.chat.blacklist.censors.DomainChatCensor;
import net.shortninja.staffplus.server.chat.blacklist.censors.IllegalCharactersChatCensor;
import net.shortninja.staffplus.server.chat.blacklist.censors.IllegalWordsChatCensor;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.SessionLoader;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.session.database.MysqlSessionsRepository;
import net.shortninja.staffplus.session.database.SessionsRepository;
import net.shortninja.staffplus.session.database.SqliteSessionsRepository;
import net.shortninja.staffplus.staff.alerts.AlertCoordinator;
import net.shortninja.staffplus.staff.alerts.xray.XrayService;
import net.shortninja.staffplus.staff.altaccountdetect.AltDetectionService;
import net.shortninja.staffplus.staff.altaccountdetect.database.ipcheck.MysqlPlayerIpRepository;
import net.shortninja.staffplus.staff.altaccountdetect.database.ipcheck.PlayerIpRepository;
import net.shortninja.staffplus.staff.altaccountdetect.database.ipcheck.SqlitePlayerIpRepository;
import net.shortninja.staffplus.staff.altaccountdetect.database.whitelist.AltDetectWhitelistRepository;
import net.shortninja.staffplus.staff.altaccountdetect.database.whitelist.MysqlAltDetectWhitelistRepository;
import net.shortninja.staffplus.staff.altaccountdetect.database.whitelist.SqliteAltDetectWhitelistRepository;
import net.shortninja.staffplus.staff.ban.BanService;
import net.shortninja.staffplus.staff.ban.database.BansRepository;
import net.shortninja.staffplus.staff.ban.database.MysqlBansRepository;
import net.shortninja.staffplus.staff.ban.database.SqliteBansRepository;
import net.shortninja.staffplus.staff.ban.gui.BannedPlayerItemBuilder;
import net.shortninja.staffplus.staff.broadcast.BroadcastService;
import net.shortninja.staffplus.staff.chests.EnderChestService;
import net.shortninja.staffplus.staff.delayedactions.DelayedActionsRepository;
import net.shortninja.staffplus.staff.delayedactions.MysqlDelayedActionsRepository;
import net.shortninja.staffplus.staff.delayedactions.SqliteDelayedActionsRepository;
import net.shortninja.staffplus.staff.examine.ExamineGuiItemProvider;
import net.shortninja.staffplus.staff.examine.items.*;
import net.shortninja.staffplus.staff.freeze.FreezeChatInterceptor;
import net.shortninja.staffplus.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.staff.infractions.InfractionProvider;
import net.shortninja.staffplus.staff.infractions.InfractionsService;
import net.shortninja.staffplus.staff.infractions.gui.InfractionGuiProvider;
import net.shortninja.staffplus.staff.kick.KickService;
import net.shortninja.staffplus.staff.kick.database.KicksRepository;
import net.shortninja.staffplus.staff.kick.database.MysqlKicksRepository;
import net.shortninja.staffplus.staff.kick.database.SqliteKicksRepository;
import net.shortninja.staffplus.staff.kick.gui.KickedPlayerItemBuilder;
import net.shortninja.staffplus.staff.location.LocationRepository;
import net.shortninja.staffplus.staff.location.MysqlLocationRepository;
import net.shortninja.staffplus.staff.location.SqliteLocationRepository;
import net.shortninja.staffplus.staff.mode.ModeDataRepository;
import net.shortninja.staffplus.staff.mode.StaffModeItemsService;
import net.shortninja.staffplus.staff.mode.StaffModeService;
import net.shortninja.staffplus.staff.mute.MuteChatInterceptor;
import net.shortninja.staffplus.staff.mute.MuteService;
import net.shortninja.staffplus.staff.mute.database.MuteRepository;
import net.shortninja.staffplus.staff.mute.database.MysqlMuteRepository;
import net.shortninja.staffplus.staff.mute.database.SqliteMuteRepository;
import net.shortninja.staffplus.staff.mute.gui.MutedPlayerItemBuilder;
import net.shortninja.staffplus.staff.protect.ProtectService;
import net.shortninja.staffplus.staff.protect.database.MysqlProtectedAreaRepository;
import net.shortninja.staffplus.staff.protect.database.ProtectedAreaRepository;
import net.shortninja.staffplus.staff.protect.database.SqliteProtectedAreaRepository;
import net.shortninja.staffplus.staff.reporting.ManageReportService;
import net.shortninja.staffplus.staff.reporting.ReportService;
import net.shortninja.staffplus.staff.reporting.database.MysqlReportRepository;
import net.shortninja.staffplus.staff.reporting.database.ReportRepository;
import net.shortninja.staffplus.staff.reporting.database.SqliteReportRepository;
import net.shortninja.staffplus.staff.reporting.gui.ReportItemBuilder;
import net.shortninja.staffplus.staff.staffchat.StaffChatChatInterceptor;
import net.shortninja.staffplus.staff.staffchat.StaffChatService;
import net.shortninja.staffplus.staff.teleport.TeleportService;
import net.shortninja.staffplus.staff.tracing.TraceChatInterceptor;
import net.shortninja.staffplus.staff.tracing.TraceService;
import net.shortninja.staffplus.staff.tracing.TraceWriterFactory;
import net.shortninja.staffplus.staff.vanish.VanishChatInterceptor;
import net.shortninja.staffplus.staff.vanish.VanishService;
import net.shortninja.staffplus.staff.warn.WarnService;
import net.shortninja.staffplus.staff.warn.database.MysqlWarnRepository;
import net.shortninja.staffplus.staff.warn.database.SqliteWarnRepository;
import net.shortninja.staffplus.staff.warn.database.WarnRepository;
import net.shortninja.staffplus.staff.warn.gui.WarningItemBuilder;
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

    private static final Map<Class, Object> beans = new HashMap<>();

    public static void init(StaffPlus staffPlus) {
        IocContainer.staffPlus = staffPlus;
    }

    public static ReportRepository getReportRepository() {
        return initRepositoryBean(ReportRepository.class,
            () -> new MysqlReportRepository(getPlayerManager()),
            () -> new SqliteReportRepository(getPlayerManager()));
    }

    public static WarnRepository getWarnRepository() {
        return initRepositoryBean(WarnRepository.class,
            () -> new MysqlWarnRepository(getPlayerManager()),
            () -> new SqliteWarnRepository(getPlayerManager()));
    }

    public static LocationRepository getLocationsRepository() {
        return initRepositoryBean(LocationRepository.class,
            MysqlLocationRepository::new,
            SqliteLocationRepository::new);
    }

    public static ProtectedAreaRepository getProtectedAreaRepository() {
        return initRepositoryBean(ProtectedAreaRepository.class,
            () -> new MysqlProtectedAreaRepository(getLocationsRepository()),
            () -> new SqliteProtectedAreaRepository(getLocationsRepository()));
    }

    public static DelayedActionsRepository getDelayedActionsRepository() {
        return initRepositoryBean(DelayedActionsRepository.class,
            MysqlDelayedActionsRepository::new,
            SqliteDelayedActionsRepository::new);
    }

    public static BansRepository getBansRepository() {
        return initRepositoryBean(BansRepository.class,
            () -> new MysqlBansRepository(getPlayerManager()),
            () -> new SqliteBansRepository(getPlayerManager()));
    }

    public static KicksRepository getKicksRepository() {
        return initRepositoryBean(KicksRepository.class,
            () -> new MysqlKicksRepository(getPlayerManager()),
            () -> new SqliteKicksRepository(getPlayerManager()));
    }


    public static MuteRepository getMuteRepository() {
        return initRepositoryBean(MuteRepository.class,
            () -> new MysqlMuteRepository(getPlayerManager()),
            () -> new SqliteMuteRepository(getPlayerManager()));
    }

    public static PlayerIpRepository getPlayerIpRepository() {
        return initRepositoryBean(PlayerIpRepository.class,
            MysqlPlayerIpRepository::new,
            SqlitePlayerIpRepository::new);
    }

    public static AltDetectWhitelistRepository getAltDetectWhitelistRepository() {
        return initRepositoryBean(AltDetectWhitelistRepository.class,
            MysqlAltDetectWhitelistRepository::new,
            SqliteAltDetectWhitelistRepository::new);
    }

    public static SessionsRepository getSessionsRepository() {
        return initRepositoryBean(SessionsRepository.class,
            MysqlSessionsRepository::new,
            SqliteSessionsRepository::new);
    }

    public static BanService getBanService() {
        return initBean(BanService.class, () -> new BanService(getPermissionHandler(), getBansRepository(), getOptions(), getMessage(), getMessages()));
    }
    public static BungeeClient getBungeeClient() {
        return initBean(BungeeClient.class, BungeeClient::new);
    }

    public static KickService getKickService() {
        return initBean(KickService.class, () -> new KickService(getPermissionHandler(), getKicksRepository(), getOptions(), getMessage(), getMessages()));
    }

    public static MuteService getMuteService() {
        return initBean(MuteService.class, () -> new MuteService(getPermissionHandler(), getMuteRepository(), getOptions(), getMessage(), getMessages()));
    }

    public static ReportService getReportService() {
        return initBean(ReportService.class, () -> new ReportService(getReportRepository(), getMessages(), getPlayerManager()));
    }

    public static InfractionsService getInfractionsService() {
        return initBean(InfractionsService.class, () -> new InfractionsService(getInfractionProviders()));
    }

    public static ManageReportService getManageReportService() {
        return initBean(ManageReportService.class, () -> new ManageReportService(getReportRepository(), getMessages(), getPlayerManager(), getReportService()));
    }

    public static ProtectService getProtectService() {
        return initBean(ProtectService.class, () -> new ProtectService(getProtectedAreaRepository(), getMessage(), getMessages(), getOptions(), getSessionManager()));
    }

    public static XrayService getXrayService() {
        return initBean(XrayService.class, () -> new XrayService(getOptions(), getAlertCoordinator()));
    }

    public static TeleportService getTeleportService() {
        return initBean(TeleportService.class, () -> new TeleportService(getOptions()));
    }

    public static StaffModeItemsService getStaffModeItemsService() {
        return initBean(StaffModeItemsService.class, () -> new StaffModeItemsService(getPermissionHandler(), getOptions(), getSessionManager()));
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

    public static SessionManager getSessionManager() {
        return initBean(SessionManager.class, () -> new SessionManager(getSessionLoader()));
    }

    public static ModeDataRepository getModeDataRepository() {
        return initBean(ModeDataRepository.class, ModeDataRepository::new);
    }

    public static StaffModeService getModeCoordinator() {
        return initBean(StaffModeService.class, () -> new StaffModeService(
            getMessage(),
            getOptions(),
            getMessages(),
            getSessionManager(),
            getVanishHandler(),
            getStaffModeItemsService(),
            getModeDataRepository()));
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

    public static VanishService getVanishHandler() {
        return initBean(VanishService.class, () -> new VanishService(StaffPlus.get().versionProtocol, getPermissionHandler(),
            getMessage(), getOptions(), getMessages(), getSessionManager()));
    }

    public static ChatHandler getChatHandler() {
        return initBean(ChatHandler.class, () -> new ChatHandler(getPermissionHandler(), getMessage(), getOptions(), getMessages()));
    }

    public static FreezeHandler getFreezeHandler() {
        return initBean(FreezeHandler.class, () -> new FreezeHandler(getPermissionHandler(), getMessage(), getOptions(), getMessages(), getSessionManager()));
    }

    public static BroadcastService getBroadcastService() {
        return initBean(BroadcastService.class, () -> new BroadcastService(getMessage(), getOptions(), getBungeeClient()));
    }

    public static TraceService getTraceService() {
        return initBean(TraceService.class, () -> new TraceService(getTraceWriterFactory(), getOptions()));
    }

    public static TraceWriterFactory getTraceWriterFactory() {
        return initBean(TraceWriterFactory.class, () -> new TraceWriterFactory(getMessage(), getMessages(), getOptions()));
    }

    public static SessionLoader getSessionLoader() {
        return initBean(SessionLoader.class, () -> new SessionLoader(getPlayerManager(), getMuteService(),getOptions(), getSessionsRepository()));
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

    public static EnderChestService getEnderchestService() {
        return initBean(EnderChestService.class,
            () -> new EnderChestService(getPermissionHandler(), getOptions()));
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
            new MuteChatInterceptor(getSessionManager(), getMessage(), getMessages()),
            new GeneralChatInterceptor(getChatHandler(), getMessage(), getMessages())
        );
    }

    public static List<InfractionProvider> getInfractionProviders() {
        return Arrays.asList(getBanService(), getMuteService(), getWarnService(), getReportService(), getKickService());
    }


    public static List<InfractionGuiProvider> getInfractionGuiProviders() {
        return Arrays.asList(new BannedPlayerItemBuilder(), new MutedPlayerItemBuilder(), new WarningItemBuilder(), new ReportItemBuilder(), new KickedPlayerItemBuilder());
    }

    private static <T> T initBean(Class<T> clazz, Supplier<T> consumer) {
        if (!beans.containsKey(clazz)) {
            beans.put(clazz, consumer.get());
        }
        return (T) beans.get(clazz);
    }

    private static <T> T initRepositoryBean(Class<T> clazz, Supplier<T> mysqlRepoSupplier, Supplier<T> sqliteRepoSupplier) {
        if (!beans.containsKey(clazz)) {
            beans.put(clazz, DatabaseUtil.database().getType() == DatabaseType.MYSQL ? mysqlRepoSupplier.get() : sqliteRepoSupplier.get());
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

    public static AltDetectionService getAltDetectionService() {
        return initBean(AltDetectionService.class, () -> new AltDetectionService(getPlayerManager(), getPlayerIpRepository(), getAltDetectWhitelistRepository(), getPermissionHandler(), getOptions()));
    }

    public static List<ExamineGuiItemProvider> getExamineGuiItemProviders() {
        return Arrays.asList(
            new LocationExamineGuiProvider(getMessages(), getOptions()),
            new NotesExamineGuiProvider(getMessages(), getMessage(), getOptions(), getSessionManager()),
            new FreezeExamineGuiProvider(getMessages(), getOptions(), getFreezeHandler()),
            new WarnExamineGuiProvider(getMessages(), getMessage(), getOptions(), getSessionManager(), getPlayerManager()),
            new FoodExamineGuiProvider(getMessages(), getOptions()),
            new GamemodeExamineGuiProvider(getMessages(), getOptions()),
            new IpExamineGuiProvider(getMessages(), getOptions()),
            new InventoryExamineGuiProvider(getMessages(), getOptions(), getPermissionHandler()),
            new EnderchestExamineGuiProvider(getMessages(), getOptions(), getPermissionHandler())
        );
    }
}
