package net.shortninja.staffplus.staff.warn.warnings.gui.actions;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DeleteWarningAction implements IAction {

    private final WarnService warnService = IocContainer.getWarnService();

    @Override
    public void click(Player player, ItemStack item, int slot) {
        CommandUtil.playerAction(player, () -> {
            int warningId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
            warnService.removeWarning(player, warningId);
        });
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
