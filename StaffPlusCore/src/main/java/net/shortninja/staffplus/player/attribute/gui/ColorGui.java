package net.shortninja.staffplus.player.attribute.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.common.IAction;
import net.shortninja.staffplus.util.GlassData;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.common.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ColorGui extends AbstractGui {
    private static final int SIZE = 27;
    private SessionManager sessionManager = IocContainer.getSessionManager();

    public ColorGui(String title) {
        super(SIZE, IocContainer.getMessage().colorize(title));
    }

    @Override
    public void buildGui() {
        IAction action = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                sessionManager.get(player.getUniqueId()).setGlassColor(item.getType());
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };

        for (short i = 0; i < 15; i++) {
            setItem(i, glassItem(i), action);
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