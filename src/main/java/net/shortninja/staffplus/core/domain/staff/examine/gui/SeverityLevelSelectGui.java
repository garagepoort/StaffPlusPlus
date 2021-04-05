package net.shortninja.staffplus.core.domain.staff.examine.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningSeverityConfiguration;

import java.util.function.Supplier;

public class SeverityLevelSelectGui extends AbstractGui {
    private static final int SIZE = 54;

    private final Options options = StaffPlus.get().getIocContainer().get(Options.class);
    private final SppPlayer targetPlayer;

    public SeverityLevelSelectGui(String title, SppPlayer targetPlayer, Supplier<AbstractGui> previousGuiSupplier) {
        super(SIZE, title, previousGuiSupplier);
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void buildGui() {

        IAction selectAction = new WarnPlayerAction(targetPlayer);
        int count = 0;
        for (WarningSeverityConfiguration severityConfiguration : options.warningConfiguration.getSeverityLevels()) {
            setItem(count, SeverityLevelItemBuilder.build(severityConfiguration), selectAction);
            count++;
        }
    }
}