package net.shortninja.staffplus.core.domain.staff.examine.items;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.application.IocMultiProvider;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.chests.EnderChestService;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGui;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGuiItemProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class EnderchestExamineGuiProvider implements ExamineGuiItemProvider {

    private final Options options;
    private PermissionHandler permissionHandler;

    public EnderchestExamineGuiProvider(Options options, PermissionHandler permissionHandler) {
        this.options = options;
        this.permissionHandler = permissionHandler;
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return locationItem();
    }

    @Override
    public IAction getClickAction(ExamineGui examineGui, Player staff, SppPlayer target) {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                IocContainer.get(EnderChestService.class).openEnderChest(staff, target);
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        if (player.isOnline()) {
            return permissionHandler.has(staff, options.enderchestsConfiguration.getPermissionViewOnline());
        } else {
            return permissionHandler.has(staff, options.enderchestsConfiguration.getPermissionViewOffline());
        }
    }

    @Override
    public int getSlot() {
        return 24;
    }

    private ItemStack locationItem() {
        ItemStack item = Items.builder()
            .setMaterial(Material.ENDER_CHEST).setAmount(1)
            .setName("&bPlayer's Enderchest")
            .addLore("View player's Enderchest")
            .build();

        return item;
    }
}
