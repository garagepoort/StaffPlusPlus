package net.shortninja.staffplus.domain.staff.examine.items;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.staff.examine.gui.ExamineGui;
import net.shortninja.staffplus.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplus.domain.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplus.domain.staff.reporting.Report;
import net.shortninja.staffplus.domain.staff.reporting.ReportService;
import net.shortninja.staffplus.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.common.Items;
import net.shortninja.staffplusplus.reports.IReport;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InfractionsExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final ExamineModeConfiguration examineModeConfiguration;
    private final Options options;
    private final ReportService reportService;

    public InfractionsExamineGuiProvider(Messages messages, Options options, ReportService reportService) {
        this.messages = messages;
        this.options = options;
        this.reportService = reportService;
        examineModeConfiguration = this.options.modeConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return infractionsItem(player);
    }

    @Override
    public IAction getClickAction(ExamineGui examineGui, Player staff, SppPlayer targetPlayer) {
        return null;
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineModeConfiguration.getModeExamineInfractions() >= 0;
    }

    @Override
    public int getSlot() {
        return examineModeConfiguration.getModeExamineInfractions() - 1;
    }

    private ItemStack infractionsItem(SppPlayer player) {
        List<Report> reports = reportService.getReports(player.getId(), 0, 40);

        List<String> lore = new ArrayList<String>();
        IReport latestReport = reports.size() >= 1 ? reports.get(reports.size() - 1) : null;
        String latestReason = latestReport == null ? "null" : latestReport.getReason();

        for (String string : messages.infractionItem) {
            List<Warning> warnings = IocContainer.getWarnService().getWarnings(player.getId(), false);
            lore.add(string.replace("%warnings%", Integer.toString(warnings.size())).replace("%reports%", Integer.toString(reports.size())).replace("%reason%", latestReason));
        }

        ItemStack item = Items.builder()
            .setMaterial(Material.BOOK).setAmount(1)
            .setName("&bInfractions")
            .setLore(lore)
            .build();

        return item;
    }

}
