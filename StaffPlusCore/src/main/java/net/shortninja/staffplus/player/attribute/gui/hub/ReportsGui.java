package net.shortninja.staffplus.player.attribute.gui.hub;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.CommandUtil;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.player.attribute.infraction.InfractionCoordinator;
import net.shortninja.staffplus.reporting.Report;
import net.shortninja.staffplus.reporting.ReportPlayerService;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportsGui extends AbstractGui {
    private static final int SIZE = 54;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = IocContainer.getMessages();
    private UserManager userManager = IocContainer.getUserManager();
    private InfractionCoordinator infractionCoordinator = StaffPlus.get().infractionCoordinator;
    private ReportPlayerService reportPlayerService = IocContainer.getReportPlayerService();

    public ReportsGui(Player player, String title) {
        super(SIZE, title);

        IAction action = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    int reportId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                    IocContainer.getReportPlayerService().acceptReport(player, reportId);
                });
            }

            @Override
            public boolean shouldClose() {
                return true;
            }

            @Override
            public void execute(Player player, String input) {
            }
        };

        int count = 0; // Using this with an enhanced for loop because it is much faster than converting to an array.

        for (Report report : reportPlayerService.getUnresolvedReports()) {
            if ((count + 1) >= SIZE) {
                break;
            }

            setItem(count, reportItem(report), action);
            count++;
        }

        player.closeInventory();
        player.openInventory(getInventory());
        userManager.get(player.getUniqueId()).setCurrentGui(this);
    }

    private ItemStack reportItem(Report report) {
        List<String> lore = new ArrayList<String>();

        lore.add("&bStatus: " + report.getReportStatus());
        lore.add("&bTimeStamp: " + report.getTimestamp().format(DateTimeFormatter.ofPattern("dd/mm/YYYY-HH:mm")));
        if (options.reportsShowReporter) {
            lore.add("&bReporter: " + report.getReporterName());
        }

        lore.add("&bReason: " + report.getReason());

        String culprit = report.getCulpritName() == null ? "Unknown" : report.getCulpritName();
        ItemStack item = Items.editor(Items.createSkull(report.getCulpritName())).setAmount(1)
                .setName("&bCulprit: " + culprit)
                .setLore(lore)
                .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(report.getId()));
    }
}