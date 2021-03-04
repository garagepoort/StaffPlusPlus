package net.shortninja.staffplus.player.attribute.gui;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.common.IAction;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.common.Items;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CounterGui extends PagedGui {
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Messages messages = IocContainer.getMessages();


    public CounterGui(Player player, String title, int page) {
        super(player, title, page);
    }

    @Override
    protected CounterGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new CounterGui(player, title, page);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                Player p = Bukkit.getPlayerExact(item.getItemMeta().getDisplayName().substring(2));

                if (p != null) {
                    player.teleport(p);
                } else message.send(player, messages.playerOffline, messages.prefixGeneral);
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player staffViewing, SppPlayer target, int offset, int amount) {
        List<Player> players = IocContainer.getOptions().modeConfiguration.getCounterModeConfiguration().isModeCounterShowStaffMode() ? getModePlayers() : JavaUtils.getOnlinePlayers();
        return players.stream()
            .filter(p -> IocContainer.getPermissionHandler().has(p, IocContainer.getOptions().permissionMember))
            .map(p -> modePlayerItem(staffViewing, p))
            .collect(Collectors.toList());
    }

    private List<Player> getModePlayers() {
        return IocContainer.getModeCoordinator().getModeUsers()
            .stream()
            .map(Bukkit::getPlayer)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private ItemStack modePlayerItem(Player staffViewing, Player player) {
        Location location = player.getLocation();
        PlayerSession playerSession = IocContainer.getSessionManager().get(player.getUniqueId());

        Items.ItemStackBuilder itemStackBuilder = Items.editor(Items.createSkull(player.getName()))
            .setName("&b" + player.getName())
            .addLore("&7" + location.getWorld().getName() + " &8 | &7" + JavaUtils.serializeLocation(location));

        if (IocContainer.getPermissionHandler().has(staffViewing, IocContainer.getOptions().permissionCounterGuiShowVanish)) {
            itemStackBuilder.addLore("&7Vanished: " + playerSession.isVanished());
        }

        return itemStackBuilder.build();
    }
}