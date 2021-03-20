package net.shortninja.staffplus.application;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.authentication.AuthenticationProvider;
import net.shortninja.staffplus.authentication.AuthenticationService;
import net.shortninja.staffplus.authentication.authme.AuthMeAuthenticationService;
import net.shortninja.staffplus.authentication.authme.NoopAuthenticationService;
import net.shortninja.staffplus.domain.actions.ActionExecutioner;
import net.shortninja.staffplus.domain.actions.ActionService;
import net.shortninja.staffplus.domain.actions.database.ActionableRepository;
import net.shortninja.staffplus.domain.actions.database.MysqlActionableRepository;
import net.shortninja.staffplus.domain.actions.database.SqliteActionableRepository;
import net.shortninja.staffplus.common.bungee.BungeeClient;
import net.shortninja.staffplus.domain.confirmation.ConfirmationChatService;
import net.shortninja.staffplus.domain.confirmation.ConfirmationService;
import net.shortninja.staffplus.domain.player.ChatActionChatInterceptor;
import net.shortninja.staffplus.domain.player.providers.OfflinePlayerProvider;
import net.shortninja.staffplus.domain.player.PlayerManager;
import net.shortninja.staffplus.domain.player.providers.BukkitOfflinePlayerProvider;
import net.shortninja.staffplus.domain.player.providers.NoopOfflinePlayerProvider;
import net.shortninja.staffplus.domain.chat.ChatHandler;
import net.shortninja.staffplus.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.domain.chat.GeneralChatInterceptor;
import net.shortninja.staffplus.domain.chat.PhraseDetectionChatInterceptor;
import net.shortninja.staffplus.domain.chat.blacklist.BlacklistService;
import net.shortninja.staffplus.domain.chat.blacklist.censors.ChatCensor;
import net.shortninja.staffplus.domain.chat.blacklist.censors.DomainChatCensor;
import net.shortninja.staffplus.domain.chat.blacklist.censors.IllegalCharactersChatCensor;
import net.shortninja.staffplus.domain.chat.blacklist.censors.IllegalWordsChatCensor;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.session.SessionLoader;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplus.session.database.MysqlSessionsRepository;
import net.shortninja.staffplus.session.database.SessionsRepository;
import net.shortninja.staffplus.session.database.SqliteSessionsRepository;
import net.shortninja.staffplus.domain.staff.alerts.xray.XrayService;
import net.shortninja.staffplus.domain.staff.altaccountdetect.AltDetectionService;
import net.shortninja.staffplus.domain.staff.altaccountdetect.database.ipcheck.MysqlPlayerIpRepository;
import net.shortninja.staffplus.domain.staff.altaccountdetect.database.ipcheck.PlayerIpRepository;
import net.shortninja.staffplus.domain.staff.altaccountdetect.database.ipcheck.SqlitePlayerIpRepository;
import net.shortninja.staffplus.domain.staff.altaccountdetect.database.whitelist.AltDetectWhitelistRepository;
import net.shortninja.staffplus.domain.staff.altaccountdetect.database.whitelist.MysqlAltDetectWhitelistRepository;
import net.shortninja.staffplus.domain.staff.altaccountdetect.database.whitelist.SqliteAltDetectWhitelistRepository;
import net.shortninja.staffplus.domain.staff.ban.BanReasonResolver;
import net.shortninja.staffplus.domain.staff.ban.BanService;
import net.shortninja.staffplus.domain.staff.ban.BanTemplateResolver;
import net.shortninja.staffplus.domain.staff.ban.database.BansRepository;
import net.shortninja.staffplus.domain.staff.ban.database.MysqlBansRepository;
import net.shortninja.staffplus.domain.staff.ban.database.SqliteBansRepository;
import net.shortninja.staffplus.domain.staff.ban.gui.BannedPlayerItemBuilder;
import net.shortninja.staffplus.domain.staff.broadcast.BroadcastService;
import net.shortninja.staffplus.domain.staff.chests.EnderChestService;
import net.shortninja.staffplus.domain.delayedactions.database.DelayedActionsRepository;
import net.shortninja.staffplus.domain.delayedactions.database.MysqlDelayedActionsRepository;
import net.shortninja.staffplus.domain.delayedactions.database.SqliteDelayedActionsRepository;
import net.shortninja.staffplus.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplus.domain.staff.examine.items.*;
import net.shortninja.staffplus.domain.staff.freeze.FreezeChatInterceptor;
import net.shortninja.staffplus.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.domain.staff.infractions.InfractionProvider;
import net.shortninja.staffplus.domain.staff.infractions.InfractionsService;
import net.shortninja.staffplus.domain.staff.infractions.gui.InfractionGuiProvider;
import net.shortninja.staffplus.domain.staff.kick.KickService;
import net.shortninja.staffplus.domain.staff.kick.database.KicksRepository;
import net.shortninja.staffplus.domain.staff.kick.database.MysqlKicksRepository;
import net.shortninja.staffplus.domain.staff.kick.database.SqliteKicksRepository;
import net.shortninja.staffplus.domain.staff.kick.gui.KickedPlayerItemBuilder;
import net.shortninja.staffplus.domain.location.LocationRepository;
import net.shortninja.staffplus.domain.location.MysqlLocationRepository;
import net.shortninja.staffplus.domain.location.SqliteLocationRepository;
import net.shortninja.staffplus.domain.staff.mode.ModeDataRepository;
import net.shortninja.staffplus.domain.staff.mode.StaffModeItemsService;
import net.shortninja.staffplus.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.domain.staff.mode.handler.ConfirmationCustomModulePreprocessor;
import net.shortninja.staffplus.domain.staff.mode.handler.CustomModulePreProcessor;
import net.shortninja.staffplus.domain.staff.mode.handler.InputCustomModulePreprocessor;
import net.shortninja.staffplus.domain.staff.mute.MuteChatInterceptor;
import net.shortninja.staffplus.domain.staff.mute.MuteService;
import net.shortninja.staffplus.domain.staff.mute.database.MuteRepository;
import net.shortninja.staffplus.domain.staff.mute.database.MysqlMuteRepository;
import net.shortninja.staffplus.domain.staff.mute.database.SqliteMuteRepository;
import net.shortninja.staffplus.domain.staff.mute.gui.MutedPlayerItemBuilder;
import net.shortninja.staffplus.domain.staff.protect.ProtectService;
import net.shortninja.staffplus.domain.staff.protect.database.MysqlProtectedAreaRepository;
import net.shortninja.staffplus.domain.staff.protect.database.ProtectedAreaRepository;
import net.shortninja.staffplus.domain.staff.protect.database.SqliteProtectedAreaRepository;
import net.shortninja.staffplus.domain.staff.reporting.ManageReportService;
import net.shortninja.staffplus.domain.staff.reporting.ReportNotifier;
import net.shortninja.staffplus.domain.staff.reporting.ReportService;
import net.shortninja.staffplus.domain.staff.reporting.cmd.ReportFiltersMapper;
import net.shortninja.staffplus.domain.staff.reporting.database.MysqlReportRepository;
import net.shortninja.staffplus.domain.staff.reporting.database.ReportRepository;
import net.shortninja.staffplus.domain.staff.reporting.database.SqliteReportRepository;
import net.shortninja.staffplus.domain.staff.reporting.gui.ReportItemBuilder;
import net.shortninja.staffplus.domain.staff.staffchat.StaffChatChatInterceptor;
import net.shortninja.staffplus.domain.staff.staffchat.StaffChatService;
import net.shortninja.staffplus.domain.staff.teleport.TeleportService;
import net.shortninja.staffplus.domain.staff.tracing.TraceChatInterceptor;
import net.shortninja.staffplus.domain.staff.tracing.TraceService;
import net.shortninja.staffplus.domain.staff.tracing.TraceWriterFactory;
import net.shortninja.staffplus.domain.staff.vanish.VanishChatInterceptor;
import net.shortninja.staffplus.domain.staff.vanish.VanishServiceImpl;
import net.shortninja.staffplus.domain.staff.warn.appeals.AppealService;
import net.shortninja.staffplus.domain.staff.warn.appeals.database.AppealRepository;
import net.shortninja.staffplus.domain.staff.warn.appeals.database.MysqlAppealRepository;
import net.shortninja.staffplus.domain.staff.warn.appeals.database.SqliteAppealRepository;
import net.shortninja.staffplus.domain.staff.warn.threshold.ThresholdService;
import net.shortninja.staffplus.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.domain.staff.warn.warnings.database.MysqlWarnRepository;
import net.shortninja.staffplus.domain.staff.warn.warnings.database.SqliteWarnRepository;
import net.shortninja.staffplus.domain.staff.warn.warnings.database.WarnRepository;
import net.shortninja.staffplus.domain.staff.warn.warnings.gui.WarningItemBuilder;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import net.shortninja.staffplus.common.utils.PermissionHandler;
import net.shortninja.staffplus.application.database.DatabaseType;
import net.shortninja.staffplus.application.database.DatabaseUtil;

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
            () -> new MysqlReportRepository(getPlayerManager(), getOptions(), getLocationsRepository()),
            () -> new SqliteReportRepository(getPlayerManager(), getOptions(), getLocationsRepository()));
    }

    public static WarnRepository getWarnRepository() {
        return initRepositoryBean(WarnRepository.class,
            () -> new MysqlWarnRepository(getPlayerManager(), getAppealRepository(), getOptions()),
            () -> new SqliteWarnRepository(getPlayerManager(), getAppealRepository(), getOptions()));
    }

    public static LocationRepository getLocationsRepository() {
        return initRepositoryBean(LocationRepository.class,
            () -> new MysqlLocationRepository(getOptions()),
            () -> new SqliteLocationRepository(getOptions()));
    }

    public static ProtectedAreaRepository getProtectedAreaRepository() {
        return initRepositoryBean(ProtectedAreaRepository.class,
            () -> new MysqlProtectedAreaRepository(getLocationsRepository(), getOptions()),
            () -> new SqliteProtectedAreaRepository(getLocationsRepository(), getOptions()));
    }

    public static DelayedActionsRepository getDelayedActionsRepository() {
        return initRepositoryBean(DelayedActionsRepository.class,
            () -> new MysqlDelayedActionsRepository(getOptions()),
            () -> new SqliteDelayedActionsRepository(getOptions()));
    }

    public static BansRepository getBansRepository() {
        return initRepositoryBean(BansRepository.class,
            () -> new MysqlBansRepository(getPlayerManager(), getOptions()),
            () -> new SqliteBansRepository(getPlayerManager(), getOptions()));
    }

    public static KicksRepository getKicksRepository() {
        return initRepositoryBean(KicksRepository.class,
            () -> new MysqlKicksRepository(getPlayerManager(), getOptions()),
            () -> new SqliteKicksRepository(getPlayerManager(), getOptions()));
    }


    public static MuteRepository getMuteRepository() {
        return initRepositoryBean(MuteRepository.class,
            () -> new MysqlMuteRepository(getPlayerManager(), getOptions()),
            () -> new SqliteMuteRepository(getPlayerManager(), getOptions()));
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

    public static AppealRepository getAppealRepository() {
        return initRepositoryBean(AppealRepository.class,
            () -> new MysqlAppealRepository(getPlayerManager()),
            () -> new SqliteAppealRepository(getPlayerManager()));
    }


    public static ActionableRepository getActionableRepository() {
        return initRepositoryBean(ActionableRepository.class,
            MysqlActionableRepository::new,
            SqliteActionableRepository::new);
    }

    public static AppealService getAppealService() {
        return initBean(AppealService.class, () -> new AppealService(
            getPlayerManager(),
            getAppealRepository(),
            getWarnRepository(), getMessage(),
            getMessages(),
            getPermissionHandler(),
            getOptions()));
    }

    public static BanService getBanService() {
        return initBean(BanService.class, () -> new BanService(getPermissionHandler(), getBansRepository(), getOptions(), getMessage(), getMessages(), getBanReasonResolver(), getBanTemplateResolver()));
    }

    public static BanReasonResolver getBanReasonResolver() {
        return initBean(BanReasonResolver.class, () -> new BanReasonResolver(getOptions()));
    }

    public static BanTemplateResolver getBanTemplateResolver() {
        return initBean(BanTemplateResolver.class, () -> new BanTemplateResolver(getOptions(), getPermissionHandler(), getMessages()));
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

    public static ConfirmationService getConfirmationService() {
        return initBean(ConfirmationService.class, () -> new ConfirmationService(getConfirmationChatService()));
    }

    public static ConfirmationChatService getConfirmationChatService() {
        return initBean(ConfirmationChatService.class, ConfirmationChatService::new);
    }

    public static ReportFiltersMapper getReportFiltersMapper() {
        return initBean(ReportFiltersMapper.class, () -> new ReportFiltersMapper(getPlayerManager()));
    }

    public static ReportService getReportService() {
        return initBean(ReportService.class, () -> new ReportService(getPermissionHandler(), getMessage(), getReportRepository(), getMessages(), getPlayerManager(), getDelayedActionsRepository(), getReportNotifier()));
    }

    public static InfractionsService getInfractionsService() {
        return initBean(InfractionsService.class, () -> new InfractionsService(getInfractionProviders(), getPlayerManager()));
    }

    public static ManageReportService getManageReportService() {
        return initBean(ManageReportService.class, () -> new ManageReportService(getReportRepository(), getMessages(), getReportService(), getReportNotifier()));
    }

    public static ProtectService getProtectService() {
        return initBean(ProtectService.class, () -> new ProtectService(getProtectedAreaRepository(), getMessage(), getMessages(), getOptions(), getSessionManager()));
    }

    public static XrayService getXrayService() {
        return initBean(XrayService.class, () -> new XrayService(getOptions()));
    }

    public static TeleportService getTeleportService() {
        return initBean(TeleportService.class, () -> new TeleportService(getOptions()));
    }

    public static StaffModeItemsService getStaffModeItemsService() {
        return initBean(StaffModeItemsService.class, () -> new StaffModeItemsService(getPermissionHandler(), getOptions(), getSessionManager()));
    }


    public static ThresholdService getThresholdService() {
        return initBean(ThresholdService.class, () -> new ThresholdService(getWarnRepository(), getOptions(), getActionService()));
    }

    public static WarnService getWarnService() {
        return initBean(WarnService.class, () -> new WarnService(
            getPermissionHandler(),
            getMessage(),
            getOptions(),
            getMessages(),
            getWarnRepository(),
            getAppealRepository()));
    }

    public static SessionManagerImpl getSessionManager() {
        return initBean(SessionManagerImpl.class, () -> new SessionManagerImpl(getSessionLoader()));
    }

    public static ModeDataRepository getModeDataRepository() {
        return initBean(ModeDataRepository.class, ModeDataRepository::new);
    }

    public static ActionService getActionService() {
        return initBean(ActionService.class, () -> new ActionService(getActionableRepository(), getPlayerManager(), getActionExecutioner()));
    }

    public static ActionExecutioner getActionExecutioner() {
        return initBean(ActionExecutioner.class, () -> new ActionExecutioner(getActionableRepository(), getDelayedActionsRepository()));
    }

    public static StaffModeService getModeCoordinator() {
        return initBean(StaffModeService.class, () -> new StaffModeService(
            getMessage(),
            getOptions(),
            getMessages(),
            getSessionManager(),
            getVanishService(),
            getStaffModeItemsService(),
            getActionService(),
            getModeDataRepository(),
            getPlayerManager()));
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
        return initBean(MessageCoordinator.class, () -> new MessageCoordinator(getPermissionHandler()));
    }

    public static Options getOptions() {
        return initBean(Options.class, Options::new);
    }

    public static StaffChatService getStaffChatService() {
        return initBean(StaffChatService.class, () -> new StaffChatService(getMessages(), getOptions()));
    }

    public static VanishServiceImpl getVanishService() {
        return initBean(VanishServiceImpl.class, () -> new VanishServiceImpl(StaffPlus.get().versionProtocol, getPermissionHandler(),
            getMessage(), getOptions(), getMessages(), getSessionManager(), getSessionLoader()));
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
        return initBean(SessionLoader.class, () -> new SessionLoader(getPlayerManager(), getMuteService(), getOptions(), getSessionsRepository()));
    }

    public static ReportNotifier getReportNotifier() {
        return initBean(ReportNotifier.class, () -> new ReportNotifier(getPermissionHandler(), getBungeeClient(), getOptions(), getMessages(), getMessage(), getPlayerManager()));
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
            new PhraseDetectionChatInterceptor(getOptions()),
            new TraceChatInterceptor(getTraceService(), getMessages(), getMessage(), getOptions()),
            new FreezeChatInterceptor(getFreezeHandler(), getOptions(), getMessages(), getMessage()),
            new VanishChatInterceptor(getVanishService(), getOptions(), getMessage(), getMessages()),
            new MuteChatInterceptor(getSessionManager(), getMessage(), getMessages()),
            new GeneralChatInterceptor(getChatHandler(), getMessage(), getMessages())
        );
    }

    public static List<CustomModulePreProcessor> getCustomModulePreProcessors() {
        // The order of execution is important in this case.
        // The last executor in the list will be executed first.
        return Arrays.asList(
            new ConfirmationCustomModulePreprocessor(getMessages(), getConfirmationService(), getMessage()),
            new InputCustomModulePreprocessor(getMessage(), getMessages())
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
            new InventoryExamineGuiProvider(getOptions(), getPermissionHandler()),
            new EnderchestExamineGuiProvider(getMessages(), getOptions(), getPermissionHandler()),
            new InfractionsExamineGuiProvider(getMessages(), getOptions(), getReportService())
        );
    }
}
