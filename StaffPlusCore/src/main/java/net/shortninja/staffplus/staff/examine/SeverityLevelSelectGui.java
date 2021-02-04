package net.shortninja.staffplus.staff.examine;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.warn.config.WarningSeverityConfiguration;
import net.shortninja.staffplus.unordered.IAction;

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