package net.shortninja.staffplus.staff.kick.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.infractions.InfractionType;
import net.shortninja.staffplus.staff.infractions.gui.InfractionGuiProvider;
import net.shortninja.staffplus.staff.kick.Kick;
import net.shortninja.staffplus.common.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.common.JavaUtils.formatLines;

public class KickedPlayerItemBuilder implements InfractionGuiProvider<Kick> {

    public static ItemStack build(Kick kick) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(kick.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ofPattern(IocContainer.getOptions().timestampFormat));

        List<String> lore = new ArrayList<String>();

        lore.add("&bId: " + kick.getId());
        if(IocContainer.getOptions().serverSyncConfiguration.isKickSyncEnabled()) {
            lore.add("&bServer: " + kick.getServerName());
        }
        lore.add("&bKicked player: " + kick.getPlayerName());
        lore.add("&bIssuer: " + kick.getIssuerName());
        lore.add("&bIssued on: " + time);
        lore.add("&bReason:");
        for (String line : formatLines(kick.getReason(), 30)) {
            lore.add("  &b" + line);
        }

        ItemStack item = Items.builder()
            .setMaterial(Material.BANNER)
            .setName("&6Kick")
            .addLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(kick.getId()));
    }

    @Override
    public InfractionType getType() {
        return InfractionType.KICK;
    }

    @Override
    public ItemStack getMenuItem(Kick kick) {
        ItemStack itemStack = build(kick);
        itemStack.setType(IocContainer.getOptions().infractionsConfiguration.getKicksGuiItem());
        return itemStack;
    }
}
