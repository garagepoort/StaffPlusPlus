package net.shortninja.staffplus.staff.warn.warnings.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.infractions.InfractionType;
import net.shortninja.staffplus.staff.infractions.gui.InfractionGuiProvider;
import net.shortninja.staffplus.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.staff.warn.appeals.config.AppealConfiguration;
import net.shortninja.staffplus.staff.warn.warnings.Warning;
import net.shortninja.staffplus.unordered.AppealStatus;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.shortninja.staffplus.util.lib.JavaUtils.formatLines;

public class WarningItemBuilder implements InfractionGuiProvider<Warning> {

    private static AppealConfiguration appealConfiguration = IocContainer.getOptions().appealConfiguration;

    public static ItemStack build(Warning warning) {
        List<String> lore = new ArrayList<String>();

        lore.add("&bId: " + warning.getId());
        if(IocContainer.getOptions().serverSyncConfiguration.isWarningSyncEnabled()) {
            lore.add("&bServer: " + warning.getServerName());
        }
        lore.add("&bSeverity: " + warning.getSeverity());
        lore.add("&bTimeStamp: " + warning.getTimestamp().format(DateTimeFormatter.ofPattern(IocContainer.getOptions().timestampFormat)));
        if (IocContainer.getOptions().warningConfiguration.isShowIssuer()) {
            lore.add("&bIssuer: " + warning.getIssuerName());
        }

        lore.add("&bReason:");
        for (String line : formatLines(warning.getReason(), 30)) {
            lore.add("  &b" + line);
        }

        Optional<Appeal> appeal = warning.getAppeal();
        if(appealConfiguration.isEnabled() && appeal.isPresent() && appeal.get().getStatus() == AppealStatus.APPROVED) {
            lore.add("&bAppeal &2approved");
        }

        ItemStack item = Items.editor(Items.createSkull(warning.getName())).setAmount(1)
            .setName("&6Warning")
            .setLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(warning.getId()));
    }


    @Override
    public InfractionType getType() {
    return InfractionType.WARNING;
    }

    @Override
    public ItemStack getMenuItem(Warning warning) {
        ItemStack itemStack = build(warning);
        itemStack.setType(IocContainer.getOptions().infractionsConfiguration.getWarningsGuiItem());
        return itemStack;
    }
}
