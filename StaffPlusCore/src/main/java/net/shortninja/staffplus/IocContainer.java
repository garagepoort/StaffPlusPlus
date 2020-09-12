package net.shortninja.staffplus;

import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.reporting.ReportPlayerService;
import net.shortninja.staffplus.reporting.database.MysqlReportRepository;
import net.shortninja.staffplus.reporting.database.ReportRepository;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.server.data.storage.IStorage;
import net.shortninja.staffplus.server.data.storage.MemoryStorage;
import net.shortninja.staffplus.server.data.storage.MySQLStorage;
import net.shortninja.staffplus.util.database.DatabaseType;
import net.shortninja.staffplus.util.database.DatabaseUtil;

public class IocContainer {

    private static ReportPlayerService reportPlayerService;
    private static ReportRepository reportRepository;
    private static UserManager userManager;
    private static StaffPlus staffPlus;
    private static Messages messages;
    private static Options options;
    private static IStorage storage;

    public static void init(StaffPlus staffPlus) {
        IocContainer.staffPlus = staffPlus;
    }

    public static ReportRepository getReportRepository() {
        if (reportRepository == null) {
            reportRepository = new MysqlReportRepository();
        }
        return reportRepository;
    }

    public static ReportPlayerService getReportPlayerService() {
        if (reportPlayerService == null) {
            reportPlayerService = new ReportPlayerService(getReportRepository(), getUserManager(), getMessages());
        }
        return reportPlayerService;
    }

    public static IStorage getStorage() {
        if (storage == null) {
            if (DatabaseUtil.database().getType() == DatabaseType.MYSQL) {
                storage = new MySQLStorage();
            } else {
                storage = new MemoryStorage();
            }
        }
        return storage;
    }

    public static UserManager getUserManager() {
        if (userManager == null) {
            userManager = new UserManager(staffPlus);
        }
        return userManager;
    }

    public static Messages getMessages() {
        if (messages == null) {
            messages = new Messages();
        }
        return messages;
    }
}
