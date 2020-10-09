package net.shortninja.staffplus.player.attribute.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AbstractGui implements IGui {
    private MessageCoordinator message = IocContainer.getMessage();
    private Options options = IocContainer.getOptions();
    private String title;
    private Inventory inventory;
    private Map<Integer, IAction> actions = new HashMap<>();

    public AbstractGui(int size, String title) {
        this.title = title;
        inventory = Bukkit.createInventory(null, size, message.colorize(title));
    }

    public String getTitle() {
        return title;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public IAction getAction(int slot) {
        return actions.get(slot);
    }

    public void setItem(int slot, ItemStack item, IAction action) {
        inventory.setItem(slot, item);

        if (action != null) {
            actions.put(slot, action);
        }
    }

    public void setGlass(PlayerSession user) {
        ItemStack item = glassItem(user.getGlassColor());

        IAction action = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                new ColorGui(player, options.glassTitle);
            }

            @Override
            public boolean shouldClose() {
                return false;
            }

        };

        for (int i = 0; i < 3; i++) {
            int slot = 9 * i;

            setItem(slot, item, action);
            setItem(slot + 8, item, action);
        }
    }

    private ItemStack glassItem(Material data) {
        return Items.builder()
                .setMaterial(data)
                .setName("&bColor #" + data)
                .addLore("&7Click to change your GUI color!")
                .build();
    }
}