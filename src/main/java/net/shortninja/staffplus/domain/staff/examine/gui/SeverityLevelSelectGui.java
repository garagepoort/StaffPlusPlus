package net.shortninja.staffplus.domain.staff.examine.gui;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.common.gui.AbstractGui;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.domain.player.SppPlayer;
import net.shortninja.staffplus.domain.staff.warn.warnings.config.WarningSeverityConfiguration;

import java.util.function.Supplier;

public class SeverityLevelSelectGui extends AbstractGui {
    private static final int SIZE = 54;

    private final Options options = IocContainer.getOptions();
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