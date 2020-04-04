package net.shortninja.staffplus.player.attribute.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.unordered.IGui;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.util.GlassData;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AbstractIGui implements IGui {
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private String title;
    private Inventory inventory;
    private Map<Integer, IAction> actions = new HashMap<>();

    public AbstractIGui(int size, String title) {
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

    public void setGlass(IUser user) {
        ItemStack item = glassItem(user.getGlassColor());

        IAction action = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                new ColorIGui(player, options.glassTitle);
            }

            @Override
            public boolean shouldClose() {
                return false;
            }

            @Override
            public void execute(Player player, String input) {
            }
        };

        for (int i = 0; i < 3; i++) {
            int slot = 9 * i;

            setItem(slot, item, action);
            setItem(slot + 8, item, action);
        }
    }

    private ItemStack glassItem(short data) {
        String[] tmp = Bukkit.getVersion().split("MC: ");
        String version = tmp[tmp.length - 1].substring(0, 4);
        if (JavaUtils.parseMcVer(version) < 13) {
            return Items.builder()
                    .setMaterial(Material.valueOf("STAINED_GLASS_PANE")).setAmount(1).setData(data)
                    .setName("&bColor #" + data)
                    .addLore("&7Click to change your GUI color!")
                    .build();
        } else {
            return Items.builder()
                    .setMaterial(Material.valueOf(GlassData.getName(data)))
                    .setName("&bColor #" + data)
                    .addLore("&7Click to change your GUI color!")
                    .build();
        }
    }
}