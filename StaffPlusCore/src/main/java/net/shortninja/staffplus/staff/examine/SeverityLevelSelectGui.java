package net.shortninja.staffplus.staff.examine;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.warn.config.WarningSeverityConfiguration;
import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

public class SeverityLevelSelectGui extends AbstractGui {
    private static final int SIZE = 54;

    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final Options options = IocContainer.getOptions();

    public SeverityLevelSelectGui(Player player, String title, Player targetPlayer, Supplier<AbstractGui> previousGuiSupplier) {
        super(SIZE, title, previousGuiSupplier);

        IAction selectAction = new WarnPlayerAction(targetPlayer);
        int count = 0;
        for (WarningSeverityConfiguration severityConfiguration : options.warningConfiguration.getSeverityLevels()) {
            setItem(count, SeverityLevelItemBuilder.build(severityConfiguration), selectAction);
            count++;
        }


        player.closeInventory();
        player.openInventory(getInventory());
        sessionManager.get(player.getUniqueId()).setCurrentGui(this);
    }

}