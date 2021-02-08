package net.shortninja.staffplus.staff.warn.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.warn.WarnService;
import net.shortninja.staffplus.staff.warn.Warning;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class ManageWarningGui extends AbstractGui {
    private static final int SIZE = 54;

    private final WarnService warnService = IocContainer.getWarnService();
    private final Permission permission = IocContainer.getPermissionHandler();
    private final Options options = IocContainer.getOptions();
    private final Player player;
    private final Warning warning;

    public ManageWarningGui(Player player, String title, Warning warning, Supplier<AbstractGui> previousGuiSupplier) {
        super(SIZE, title, previousGuiSupplier);
        this.player = player;
        this.warning = warning;
    }

    @Override
    public void buildGui() {
        IAction deleteAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    int warningId = Integer.parseInt(StaffPlus.get().versionProtocol.getNbtString(item));
                    warnService.removeWarning(warningId);
                });
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };

        setItem(13, WarningItemBuilder.build(warning), null);


        if(permission.has(player, options.manageWarningsConfiguration.getPermissionDelete())) {
            addDeleteItem(warning, deleteAction, 8);
        }
    }

    private void addDeleteItem(Warning warning, IAction action, int slot) {
        ItemStack itemstack = Items.builder()
            .setMaterial(Material.REDSTONE_BLOCK)
            .setName("Delete")
            .addLore("Click to delete this warning")
            .build();

        ItemStack item = StaffPlus.get().versionProtocol.addNbtString(
            Items.editor(itemstack)
                .setAmount(1)
                .build(), String.valueOf(warning.getId()));
        setItem(slot, item, action);
    }
}