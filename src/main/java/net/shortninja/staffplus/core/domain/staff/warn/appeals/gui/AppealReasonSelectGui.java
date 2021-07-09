package net.shortninja.staffplus.core.domain.staff.warn.appeals.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.cmd.CommandUtil;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.AppealService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class AppealReasonSelectGui extends AbstractGui {
    private static final int SIZE = 54;

    private final Options options = StaffPlus.get().getIocContainer().get(Options.class);
    private final AppealService appealService = StaffPlus.get().getIocContainer().get(AppealService.class);
    private final Warning warning;

    public AppealReasonSelectGui(Warning warning, String title, Supplier<AbstractGui> previousGuiSupplier) {
        super(SIZE, title, previousGuiSupplier);
        this.warning = warning;
    }

    @Override
    public void buildGui() {

        IAction selectAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                CommandUtil.playerAction(player, () -> {
                    String reason = StaffPlus.get().getIocContainer().get(IProtocolService.class).getVersionProtocol().getNbtString(item);
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
        for (String appealReason : options.appealConfiguration.appealReasons) {
            setItem(count, AppealReasonItemBuilder.build(appealReason), selectAction);
            count++;
        }
    }
}