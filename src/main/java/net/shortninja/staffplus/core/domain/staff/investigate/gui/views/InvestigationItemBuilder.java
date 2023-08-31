package net.shortninja.staffplus.core.domain.staff.investigate.gui.views;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@IocBean
public class InvestigationItemBuilder {

    private final Options options;

    public InvestigationItemBuilder(Options options) {
        this.options = options;
    }


    public ItemStack build(Investigation investigation) {
        List<String> lore = new ArrayList<>();

        lore.add("&bId: &6" + investigation.getId());
        if (options.serverSyncConfiguration.investigationSyncServers.isEnabled()) {
            lore.add("&bServer: &6" + investigation.getServerName());
        }
        lore.add("&bInvestigator: &6" + investigation.getInvestigatorName());
        lore.add("&bStatus: &6" + investigation.getStatus());
        lore.add("&bStart time: &6" + investigation.getCreationDate().format(DateTimeFormatter.ofPattern(options.timestampFormat)));
        lore.add("&bInvestigated: &6" + investigation.getInvestigatedName().orElse("Unknown"));


        String title = investigation.getInvestigatedName().orElse("Investigation #" + investigation.getId());

        return Items.editor(Items.createBook(title, "")).setAmount(1)
            .setName("&7Investigation")
            .setLore(lore)
            .build();
    }

}
