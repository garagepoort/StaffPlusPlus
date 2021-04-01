package net.shortninja.staffplus.core.domain.staff.investigate.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.staff.infractions.gui.InfractionGuiProvider;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@IocBean
@IocMultiProvider(InfractionGuiProvider.class)
public class InvestigationItemBuilder {

    private final Options options;
    private final IProtocolService protocolService;

    public InvestigationItemBuilder(Options options, IProtocolService protocolService) {
        this.options = options;
        this.protocolService = protocolService;
    }


    public ItemStack build(Investigation investigation) {
        List<String> lore = new ArrayList<>();

        lore.add("&bId: " + investigation.getId());
        if (options.serverSyncConfiguration.isInvestigationSyncEnabled()) {
            lore.add("&bServer: " + investigation.getServerName());
        }
        lore.add("&bStatus: " + investigation.getStatus());
        lore.add("&bStart time: " + investigation.getCreationDate().format(DateTimeFormatter.ofPattern(options.timestampFormat)));


        ItemStack item = Items.editor(Items.createSkull(investigation.getInvestigatedName())).setAmount(1)
            .setName("&6Investigation")
            .setLore(lore)
            .build();

        return protocolService.getVersionProtocol().addNbtString(item, String.valueOf(investigation.getId()));
    }

}
