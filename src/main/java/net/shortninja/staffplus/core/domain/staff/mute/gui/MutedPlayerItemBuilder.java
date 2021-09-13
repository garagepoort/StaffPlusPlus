package net.shortninja.staffplus.core.domain.staff.mute.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.gui.LoreBuilder;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.core.domain.staff.infractions.gui.views.InfractionGuiProvider;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@IocBean
@IocMultiProvider(InfractionGuiProvider.class)
public class MutedPlayerItemBuilder implements InfractionGuiProvider<Mute> {

    private final IProtocolService protocolService;
    private final Options options;

    public MutedPlayerItemBuilder(IProtocolService protocolService, Options options) {
        this.protocolService = protocolService;
        this.options = options;
    }

    public ItemStack build(Mute mute) {
        String time = getTimeString(mute.getCreationDate());

        List<String> lore = LoreBuilder.builder("&b", "&6")
            .addItem("Id", String.valueOf(mute.getId()))
            .addItem("Server", mute.getServerName(), options.serverSyncConfiguration.muteSyncEnabled)
            .addItem("Muted player", mute.getTargetName())
            .addItem("Issuer", mute.getIssuerName())
            .addItem("Issued on", time)
            .addItem("Ended on", () -> getTimeString(mute.getEndDate()), mute.hasEnded())
            .addIndented("Reason", mute.getReason())
            .addDuration("Time Left", mute.getHumanReadableDuration(), mute.getEndTimestamp() != null)
            .addItem("Permanent mute", mute.getEndTimestamp() == null)
            .build();

        ItemStack item = Items.builder()
            .setMaterial(Material.PLAYER_HEAD)
            .setName("&3Mute")
            .addLore(lore)
            .build();

        return protocolService.getVersionProtocol().addNbtString(item, String.valueOf(mute.getId()));
    }
    @NotNull
    private String getTimeString(ZonedDateTime date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
        return localDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ofPattern(options.timestampFormat));
    }
    @Override
    public InfractionType getType() {
        return InfractionType.MUTE;
    }

    @Override
    public ItemStack getMenuItem(Mute mute) {
        ItemStack itemStack = build(mute);
        itemStack.setType(options.infractionsConfiguration.getMutesGuiItem());
        return itemStack;
    }
}