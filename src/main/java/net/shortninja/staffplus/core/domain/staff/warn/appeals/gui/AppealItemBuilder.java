package net.shortninja.staffplus.core.domain.staff.warn.appeals.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.gui.LoreBuilder;
import net.shortninja.staffplusplus.appeals.IAppeal;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static net.shortninja.staffplusplus.warnings.AppealStatus.OPEN;

public class AppealItemBuilder {

    public static ItemStack build(IAppeal appeal) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(appeal.getCreationDate().toInstant(), ZoneOffset.UTC);
        String time = localDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ofPattern(StaffPlus.get().getIocContainer().get(Options.class).timestampFormat));

        List<String> lore = LoreBuilder.builder("&b", "&6")
            .addItem("Appealer", appeal.getAppealerName())
            .addItem("Timestamp", time)
            .addIndented("Reason", appeal.getReason())
            .addItem("Resolved by", appeal.getResolverName(), appeal.getStatus() != OPEN)
            .addIndented("Resolve reason", () -> appeal.getResolveReason().get(), appeal.getResolveReason().isPresent())
            .build();

        ItemStack itemStack = Items.builder()
            .setMaterial(Material.WRITABLE_BOOK)
            .build();

        ItemStack item = Items.editor(itemStack).setAmount(1)
            .setName("Appeal &6" + appeal.getStatus().name().toLowerCase())
            .setLore(lore)
            .build();

        return StaffPlus.get().getIocContainer().get(IProtocolService.class).getVersionProtocol().addNbtString(item, String.valueOf(appeal.getId()));
    }

}
