package net.shortninja.staffplus.domain.staff.warn.appeals.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.Items;
import net.shortninja.staffplusplus.appeals.IAppeal;
import net.shortninja.staffplusplus.warnings.AppealStatus;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.common.JavaUtils.formatLines;

public class AppealItemBuilder {

    public static ItemStack build(IAppeal appeal) {
        List<String> lore = new ArrayList<>();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(appeal.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ofPattern(IocContainer.getOptions().timestampFormat));

        lore.add("&bAppealer: " + appeal.getAppealerName());
        lore.add("&bTimeStamp: " + time);

        lore.add("&bReason:");
        for (String line : formatLines(appeal.getReason(), 30)) {
            lore.add("  &b" + line);
        }

        if (appeal.getStatus() != AppealStatus.OPEN) {
            lore.add("&bResolved by: " + appeal.getResolverName());
            if (appeal.getResolveReason().isPresent()) {
                lore.add("&bResolve reason:");
                for (String line : formatLines(appeal.getResolveReason().get(), 30)) {
                    lore.add("  &b" + line);
                }
            }
        }
        ItemStack itemStack = Items.builder()
            .setMaterial(Material.BOOK_AND_QUILL)
            .build();

        ItemStack item = Items.editor(itemStack).setAmount(1)
            .setName("Appeal &6" + appeal.getStatus().name().toLowerCase())
            .setLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(appeal.getId()));
    }

}
