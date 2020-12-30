package net.shortninja.staffplus.staff.warn.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.infractions.gui.InfractionGuiProvider;
import net.shortninja.staffplus.staff.warn.Warning;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static net.shortninja.staffplus.util.lib.JavaUtils.formatLines;

public class WarningItemBuilder implements InfractionGuiProvider<Warning> {

    public static ItemStack build(Warning warning) {
        List<String> lore = new ArrayList<String>();

        lore.add("&bSeverity: " + warning.getSeverity());
        lore.add("&bTimeStamp: " + warning.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm")));
        if (IocContainer.getOptions().warningConfiguration.isShowIssuer()) {
            lore.add("&bIssuer: " + warning.getIssuerName());
        }

        lore.add("&bReason:");
        for (String line : formatLines(warning.getReason(), 30)) {
            lore.add("  &b" + line);
        }

        String reason = warning.getReason().length() > 12 ? warning.getReason().substring(0, 9) + "..." : warning.getReason();
        ItemStack item = Items.editor(Items.createSkull(warning.getName())).setAmount(1)
            .setName("&cWarning")
            .setLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(warning.getId()));
    }


    @Override
    public String getType() {
        return "WARNING";
    }

    @Override
    public ItemStack getMenuItem(Warning warning) {
        return build(warning);
    }
}
