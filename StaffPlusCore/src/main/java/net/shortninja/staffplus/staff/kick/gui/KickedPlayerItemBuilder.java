package net.shortninja.staffplus.staff.kick.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.infractions.gui.InfractionGuiProvider;
import net.shortninja.staffplus.staff.kick.Kick;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.util.lib.JavaUtils.formatLines;

public class KickedPlayerItemBuilder implements InfractionGuiProvider<Kick> {

    public static ItemStack build(Kick kick) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(kick.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        List<String> lore = new ArrayList<String>();

        lore.add("&bId: " + kick.getId());
        lore.add("&bKicked player: " + kick.getPlayerName());
        lore.add("&bIssuer: " + kick.getIssuerName());
        lore.add("&bIssued on: " + time);
        lore.add("&bReason:");
        for (String line : formatLines(kick.getReason(), 30)) {
            lore.add("  &b" + line);
        }

        ItemStack item = Items.builder()
            .setMaterial(Material.PLAYER_HEAD)
            .setName(kick.getPlayerName())
            .addLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(kick.getId()));
    }

    @Override
    public String getType() {
        return "KICK";
    }

    @Override
    public ItemStack getMenuItem(Kick kick) {
        return build(kick);
    }
}
