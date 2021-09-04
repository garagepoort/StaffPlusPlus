package net.shortninja.staffplus.core.domain.staff.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.TubingGuiActions;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplusplus.reports.IReport;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class InfractionsExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final ExamineModeConfiguration examineModeConfiguration;
    private final ReportService reportService;

    public InfractionsExamineGuiProvider(Messages messages, Options options, ReportService reportService) {
        this.messages = messages;
        this.reportService = reportService;
        examineModeConfiguration = options.staffItemsConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(Player player1, SppPlayer player) {
        return infractionsItem(player);
    }

    @Override
    public String getClickAction(Player staff, SppPlayer targetPlayer, String backAction) {
        return TubingGuiActions.NOOP;
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
        List<Report> reports = reportService.getReported(player.getId(), 0, 40);

        List<String> lore = new ArrayList<>();
        IReport latestReport = reports.size() >= 1 ? reports.get(reports.size() - 1) : null;
        String latestReason = latestReport == null ? "null" : latestReport.getReason();

        for (String string : messages.infractionItem) {
            List<Warning> warnings = StaffPlus.get().getIocContainer().get(WarnService.class).getWarnings(player.getId(), false);
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
