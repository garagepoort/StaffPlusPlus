package net.shortninja.staffplus.player.attribute.gui;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.server.data.config.Options;
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

public class AbstractGui {
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private String title;
    private Inventory inventory;
    private Map<Integer, AbstractAction> actions = new HashMap<Integer, AbstractAction>();

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

    public AbstractAction getAction(int slot) {
        return actions.get(slot);
    }

    public void setItem(int slot, ItemStack item, AbstractAction action) {
        inventory.setItem(slot, item);

        if (action != null) {
            actions.put(slot, action);
        }
    }

    public void setGlass(User user) {
        ItemStack item = glassItem(user.getGlassColor());

        AbstractAction action = new AbstractAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                new ColorGui(player, options.glassTitle);
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
        if (JavaUtils.parseMcVer(version)<13) {
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

    public interface AbstractAction {
        void click(Player player, ItemStack item, int slot);

        boolean shouldClose();

        void execute(Player player, String input);
    }
}