package net.shortninja.staffplus.staff.protect.cmd;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.staff.protect.ProtectService;
import net.shortninja.staffplus.staff.protect.ProtectedArea;
import net.shortninja.staffplus.staff.teleport.TeleportService;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class ManageProtectedAreaGui extends AbstractGui {
    private static final int SIZE = 54;

    private final ProtectService protectService = IocContainer.getProtectService();
    private final TeleportService teleportService = IocContainer.getTeleportService();
    private final ProtectedArea protectedArea;

    public ManageProtectedAreaGui(String title, ProtectedArea protectedArea, Supplier<AbstractGui> previousGuiSupplier) {
        super(SIZE, title, previousGuiSupplier);
        this.protectedArea = protectedArea;
    }

    @Override
    public void buildGui() {

        IAction teleportAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    teleportService.teleportSelf(player, protectedArea.getCornerPoint1());
                });
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };

        IAction deleteAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                protectService.deleteProtectedArea(player, protectedArea.getId());
                previousGuiSupplier.get();
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };

        setItem(13, ProtectedAreaItemBuilder.build(protectedArea), null);

        addTeleportItem(protectedArea, teleportAction, 34);
        addTeleportItem(protectedArea, teleportAction, 35);
        addTeleportItem(protectedArea, teleportAction, 43);
        addTeleportItem(protectedArea, teleportAction, 44);

        addDeleteItem(protectedArea, deleteAction, 30);
        addDeleteItem(protectedArea, deleteAction, 31);
        addDeleteItem(protectedArea, deleteAction, 32);
        addDeleteItem(protectedArea, deleteAction, 39);
        addDeleteItem(protectedArea, deleteAction, 40);
        addDeleteItem(protectedArea, deleteAction, 41);
    }

    private void addDeleteItem(ProtectedArea protectedArea, IAction action, int slot) {
        ItemStack item = StaffPlus.get().versionProtocol.addNbtString(
            Items.editor(Items.createRedColoredGlass("Delete protected area", "Click to delete the protected area"))
                .setAmount(1)
                .build(), String.valueOf(protectedArea.getId()));
        setItem(slot, item, action);
    }

    private void addTeleportItem(ProtectedArea protectedArea, IAction action, int slot) {
        ItemStack item = StaffPlus.get().versionProtocol.addNbtString(
            Items.editor(Items.createOrangeColoredGlass("Teleport", "Click to teleport yourself to this area"))
                .setAmount(1)
                .build(), String.valueOf(protectedArea.getId()));
        setItem(slot, item, action);
    }
}