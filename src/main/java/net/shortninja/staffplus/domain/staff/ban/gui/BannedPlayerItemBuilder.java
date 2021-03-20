package net.shortninja.staffplus.domain.staff.ban.gui;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.staff.ban.Ban;
import net.shortninja.staffplus.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.domain.staff.infractions.gui.InfractionGuiProvider;
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

public class BannedPlayerItemBuilder implements InfractionGuiProvider<Ban> {

    private Options options = IocContainer.getOptions();

    public static ItemStack build(Ban ban) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(ban.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ofPattern(IocContainer.getOptions().timestampFormat));

        List<String> lore = new ArrayList<String>();

        lore.add("&bId: " + ban.getId());
        if(IocContainer.getOptions().serverSyncConfiguration.isBanSyncEnabled()) {
            lore.add("&bServer: " + ban.getServerName());
        }
        lore.add("&bBanned player: " + ban.getTargetName());
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
            .setMaterial(Material.PLAYER_HEAD)
            .setName("&cBan")
            .addLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(ban.getId()));
    }

    @Override
    public InfractionType getType() {
        return InfractionType.BAN;
    }

    @Override
    public ItemStack getMenuItem(Ban ban) {
        ItemStack itemStack = build(ban);
        itemStack.setType(options.infractionsConfiguration.getBansGuiItem());
        return itemStack;
    }
}
