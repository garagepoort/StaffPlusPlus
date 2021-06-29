package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.core.domain.staff.infractions.gui.InfractionGuiProvider;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.config.AppealConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningSeverityConfiguration;
import net.shortninja.staffplusplus.warnings.AppealStatus;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.shortninja.staffplus.core.common.JavaUtils.formatLines;

@IocBean
@IocMultiProvider(InfractionGuiProvider.class)
public class WarningItemBuilder implements InfractionGuiProvider<Warning> {

    private final Options options;
    private final AppealConfiguration appealConfiguration;
    private final WarningConfiguration warningConfiguration;
    private final IProtocolService protocolService;

    public WarningItemBuilder(Options options, IProtocolService protocolService) {
        this.options = options;
        this.protocolService = protocolService;
        appealConfiguration = options.appealConfiguration;
        warningConfiguration = options.warningConfiguration;
    }


    public ItemStack build(Warning warning) {
        List<String> lore = new ArrayList<>();

        lore.add("&bId: " + warning.getId());
        if (options.serverSyncConfiguration.isWarningSyncEnabled()) {
            lore.add("&bServer: " + warning.getServerName());
        }
        lore.add("&bSeverity: " + warning.getSeverity());
        lore.add("&bTimeStamp: " + warning.getCreationDate().format(DateTimeFormatter.ofPattern(options.timestampFormat)));
        if (options.warningConfiguration.isShowIssuer()) {
            lore.add("&bIssuer: " + warning.getIssuerName());
        }
        lore.add("&bCulprit: " + warning.getTargetName());

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

        return protocolService.getVersionProtocol().addNbtString(item, String.valueOf(warning.getId()));
    }

    private void addExpiresAt(Warning warning, List<String> lore) {
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

    private boolean expirationEnabled(String severityLevel) {
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
        itemStack.setType(options.infractionsConfiguration.getWarningsGuiItem());
        return itemStack;
    }
}
