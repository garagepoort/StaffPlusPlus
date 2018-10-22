package net.shortninja.staffplus.player.attribute.gui.hub;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MinerGui extends AbstractGui {
    private static final int SIZE = 54;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UserManager userManager = StaffPlus.get().userManager;

    public MinerGui(Player player, String title) {
        super(SIZE, title);

        AbstractAction action = new AbstractAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                Player p = Bukkit.getPlayer(item.getItemMeta().getDisplayName().substring(2));

                if (p != null) {
                    player.teleport(p);
                } else message.send(player, messages.playerOffline, messages.prefixGeneral);
            }

            @Override
            public boolean shouldClose() {
                return true;
            }

            @Override
            public void execute(Player player, String input) {
            }
        };

        int slot = 0; // Better to use this because not every iteration is going to have a result.

        for (Player p : JavaUtils.getOnlinePlayers()) {
            if (p.getLocation().getBlockY() > options.modeGuiMinerLevel) {
                continue;
            } else if ((slot + 1) >= SIZE) {
                break;
            }

            setItem(slot, minerItem(p), action);
            slot++;
        }

        player.openInventory(getInventory());
        userManager.get(player.getUniqueId()).setCurrentGui(this);
    }

    private ItemStack minerItem(Player player) {
        Location location = player.getLocation();

        ItemStack item = Items.editor(Items.createSkull(player.getName())).setAmount(1)
                .setName("&b" + player.getName())
                .addLore("&7" + location.getWorld().getName() + " &8� &7" + JavaUtils.serializeLocation(location))
                .build();

        return item;
    }
}