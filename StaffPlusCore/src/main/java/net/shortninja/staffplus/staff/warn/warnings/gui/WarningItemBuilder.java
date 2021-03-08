package net.shortninja.staffplus.staff.warn.warnings.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.Items;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.staff.infractions.InfractionType;
import net.shortninja.staffplus.staff.infractions.gui.InfractionGuiProvider;
import net.shortninja.staffplus.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.staff.warn.appeals.config.AppealConfiguration;
import net.shortninja.staffplus.staff.warn.warnings.Warning;
import net.shortninja.staffplus.staff.warn.warnings.config.WarningConfiguration;
import net.shortninja.staffplus.staff.warn.warnings.config.WarningSeverityConfiguration;
import net.shortninja.staffplusplus.warnings.AppealStatus;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.shortninja.staffplus.common.JavaUtils.formatLines;

public class WarningItemBuilder implements InfractionGuiProvider<Warning> {

    private static AppealConfiguration appealConfiguration = IocContainer.getOptions().appealConfiguration;
    private static WarningConfiguration warningConfiguration = IocContainer.getOptions().warningConfiguration;

    public static ItemStack build(Warning warning) {
        List<String> lore = new ArrayList<String>();

        lore.add("&bId: " + warning.getId());
        if (IocContainer.getOptions().serverSyncConfiguration.isWarningSyncEnabled()) {
            lore.add("&bServer: " + warning.getServerName());
        }
        lore.add("&bSeverity: " + warning.getSeverity());
        lore.add("&bTimeStamp: " + warning.getCreationDate().format(DateTimeFormatter.ofPattern(IocContainer.getOptions().timestampFormat)));
        if (IocContainer.getOptions().warningConfiguration.isShowIssuer()) {
            lore.add("&bIssuer: " + warning.getIssuerName());
        }

        lore.add("&bReason:");
        for (String line : formatLines(warning.getReason(), 30)) {
            lore.add("  &b" + line);
        }

        Optional<Appeal> appeal = warning.getAppeal();
        if (appealConfiguration.isEnabled() && appeal.isPresent() && appeal.get().getStatus() == AppealStatus.APPROVED) {
            lore.add("&bAppeal &2approved");
        } else if (warning.isExpired()) {
            lore.add("&cExpired");
        } else if (expirationEnabled(warning.getSeverity())) {
            addExpiresAt(warning, lore);
        }

        ItemStack item = Items.editor(Items.createSkull(warning.getTargetName())).setAmount(1)
            .setName("&6Warning")
            .setLore(lore)
            .build();

        return StaffPlus.get().versionProtocol.addNbtString(item, String.valueOf(warning.getId()));
    }

    private static void addExpiresAt(Warning warning, List<String> lore) {
        warningConfiguration.getSeverityConfiguration(warning.getSeverity())
            .ifPresent(warningSeverityConfiguration -> {
                long now = System.currentTimeMillis();
                long expireTimestamp = warning.getCreationTimestamp() + warningSeverityConfiguration.getExpirationDuration();
                long expirationDuration = expireTimestamp - now;
                if (expirationDuration <= 0) {
                    lore.add("&bExpires after: &5Less than a minute");
                } else {
                    lore.add("&bExpires after: &5" + JavaUtils.toHumanReadableDuration(expirationDuration));
                }
            });
    }

    private static boolean expirationEnabled(String severityLevel) {
        return warningConfiguration.getSeverityConfiguration(severityLevel)
            .map(WarningSeverityConfiguration::isExpirationEnabled)
            .orElse(false);
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
