package net.shortninja.staffplus.staff.warn.appeals.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.warn.appeals.AppealService;
import net.shortninja.staffplus.staff.warn.warnings.Warning;
import net.shortninja.staffplus.common.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class AppealReasonSelectGui extends AbstractGui {
    private static final int SIZE = 54;

    private final Options options = IocContainer.getOptions();
    private final AppealService appealService = IocContainer.getAppealService();
    private final Warning warning;

    public AppealReasonSelectGui(Warning warning, String title, Supplier<AbstractGui> previousGuiSupplier) {
        super(SIZE, title, previousGuiSupplier);
        this.warning = warning;
    }

    @Override
    public void buildGui() {

        IAction selectAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    String reason = StaffPlus.get().versionProtocol.getNbtString(item);
                    appealService.addAppeal(player, warning, reason);
                    previousGuiSupplier.get().show(player);
                });
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
        int count = 0;
        for (String appealReason : options.appealConfiguration.getAppealReasons()) {
            setItem(count, AppealReasonItemBuilder.build(appealReason), selectAction);
            count++;
        }
    }
}