package net.shortninja.staffplus.player.attribute.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.mode.ModeCoordinator;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CounterGui extends AbstractGui {
    private static final int SIZE = 54;
    private final PermissionHandler permission = IocContainer.getPermissionHandler();
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Options options = IocContainer.getOptions();
    private final Messages messages = IocContainer.getMessages();
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final ModeCoordinator modeCoordinator = IocContainer.getModeCoordinator();

    public CounterGui(Player player, String title) {
        super(SIZE, title);

        IAction action = new IAction() {
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
        };

        List<Player> players = options.modeCounterShowStaffMode ? getModePlayers() : JavaUtils.getOnlinePlayers();
        int slot = 0; // Better to use this because not every iteration is going to have a result.

        for (Player p : players) {
            if (!permission.has(p, options.permissionMember)) {
                continue;
            } else if ((slot + 1) >= SIZE) {
                break;
            }

            setItem(slot, modePlayerItem(p), action);
            slot++;
        }

        player.openInventory(getInventory());
        sessionManager.get(player.getUniqueId()).setCurrentGui(this);
    }

    private List<Player> getModePlayers() {
        List<Player> modePlayers = new ArrayList<Player>();

        for (UUID uuid : modeCoordinator.getModeUsers()) {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                modePlayers.add(player);
            }
        }

        return modePlayers;
    }

    private ItemStack modePlayerItem(Player player) {
        Location location = player.getLocation();

        ItemStack item = Items.editor(Items.createSkull(player.getName()))
                .setName("&b" + player.getName())
                .addLore("&7" + location.getWorld().getName() + " &8� &7" + JavaUtils.serializeLocation(location))
                .build();

        return item;
    }
}