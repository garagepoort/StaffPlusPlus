package net.shortninja.staffplus.core.domain.staff.kick.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.gui.LoreBuilder;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.core.domain.staff.infractions.gui.views.InfractionGuiProvider;
import net.shortninja.staffplus.core.domain.staff.kick.Kick;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@IocBean
@IocMultiProvider(InfractionGuiProvider.class)
public class KickedPlayerItemBuilder implements InfractionGuiProvider<Kick> {
    private final IProtocolService protocolService;
    private final Options options;

    public KickedPlayerItemBuilder(IProtocolService protocolService, Options options) {
        this.protocolService = protocolService;
        this.options = options;
    }

    public ItemStack build(Kick kick) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(kick.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ofPattern(options.timestampFormat));

        List<String> lore = LoreBuilder.builder("&b", "&6")
            .addItem("Id", String.valueOf(kick.getId()))
            .addItem("Server", kick.getServerName(), options.serverSyncConfiguration.kickSyncServers.isEnabled())
            .addItem("Kicked player", kick.getTargetName())
            .addItem("Issuer", kick.getIssuerName())
            .addItem("Issued on", time)
            .addIndented("Reason", kick.getReason())
            .build();

        ItemStack item = Items.builder()
            .setMaterial(Material.BANNER)
            .setName("&6Kick")
            .addLore(lore)
            .build();

        return protocolService.getVersionProtocol().addNbtString(item, String.valueOf(kick.getId()));
    }

    @Override
    public InfractionType getType() {
        return InfractionType.KICK;
    }

    @Override
    public ItemStack getMenuItem(Kick kick) {
        ItemStack itemStack = build(kick);
        itemStack.setType(options.infractionsConfiguration.getKicksGuiItem());
        return itemStack;
    }
}
