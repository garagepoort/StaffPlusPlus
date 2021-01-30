package net.shortninja.staffplus.staff.ban.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.ban.Ban;
import net.shortninja.staffplus.staff.infractions.gui.InfractionGuiProvider;
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

public class BannedPlayerItemBuilder implements InfractionGuiProvider<Ban> {

    public static ItemStack build(Ban ban) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(ban.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        List<String> lore = new ArrayList<String>();

        lore.add("&bId: " + ban.getId());
        lore.add("&bBanned player: " + ban.getPlayerName());
        lore.add("&bIssuer: " + ban.getIssuerName());
        lore.add("&bIssued on: " + time);
        lore.add("&bReason:");
        for (String line : formatLines(ban.getReason(), 30)) {
            lore.add("  &b" + line);
        }

        if(ban.getEndTimestamp() != null) {
            lore.add("&bTime left:");
            lore.add("  &b" + ban.getHumanReadableDuration());
        }else {
            lore.add("&bPermanent ban");
        }
        ItemStack item = Items.builder()
            .setMaterial(Material.BANNER)
            .setName("&cBan")
            .addLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(ban.getId()));
    }

    @Override
    public String getType() {
        return "BAN";
    }

    @Override
    public ItemStack getMenuItem(Ban ban) {
        return build(ban);
    }
}
