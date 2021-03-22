package net.shortninja.staffplus.core.domain.staff.mute.gui;

import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.core.domain.staff.infractions.gui.InfractionGuiProvider;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.core.common.JavaUtils.formatLines;

@IocBean
@IocMultiProvider(InfractionGuiProvider.class)
public class MutedPlayerItemBuilder implements InfractionGuiProvider<Mute> {

    private final IProtocol versionProtocol;
    private final Options options;

    public MutedPlayerItemBuilder(IProtocol versionProtocol, Options options) {
        this.versionProtocol = versionProtocol;
        this.options = options;
    }

    public ItemStack build(Mute mute) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(mute.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ofPattern(options.timestampFormat));

        List<String> lore = new ArrayList<>();

        lore.add("&bId: " + mute.getId());
        if(options.serverSyncConfiguration.isMuteSyncEnabled()) {
            lore.add("&bServer: " + mute.getServerName());
        }
        lore.add("&bMuted player: " + mute.getTargetName());
        lore.add("&bIssuer: " + mute.getIssuerName());
        lore.add("&bIssued on: " + time);
        lore.add("&bReason:");
        for (String line : formatLines(mute.getReason(), 30)) {
            lore.add("  &b" + line);
        }

        if(mute.getEndTimestamp() != null) {
            lore.add("&bTime left:");
            lore.add("  &b" + mute.getHumanReadableDuration());
        }else {
            lore.add("&bPermanent mute");
        }
        ItemStack item = Items.builder()
            .setMaterial(Material.PLAYER_HEAD)
            .setName("&3Mute")
            .addLore(lore)
            .build();

        return versionProtocol.addNbtString(item, String.valueOf(mute.getId()));
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
