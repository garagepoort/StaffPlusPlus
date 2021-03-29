package net.shortninja.staffplus.core.domain.player.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.gui.PagedGui;

import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CounterGui extends PagedGui {
    private final Messages messages = StaffPlus.get().iocContainer.get(Messages.class);


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
                } else messages.send(player, messages.playerOffline, messages.prefixGeneral);
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player staffViewing, SppPlayer target, int offset, int amount) {
        List<Player> players = StaffPlus.get().iocContainer.get(Options.class).modeConfiguration.getCounterModeConfiguration().isModeCounterShowStaffMode() ? getModePlayers() : JavaUtils.getOnlinePlayers();
        return players.stream()
            .filter(p -> StaffPlus.get().iocContainer.get(PermissionHandler.class).has(p, StaffPlus.get().iocContainer.get(Options.class).permissionMember))
            .map(p -> modePlayerItem(staffViewing, p))
            .collect(Collectors.toList());
    }

    private List<Player> getModePlayers() {
        return StaffPlus.get().iocContainer.get(StaffModeService.class).getModeUsers()
            .stream()
            .map(Bukkit::getPlayer)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private ItemStack modePlayerItem(Player staffViewing, Player player) {
        Location location = player.getLocation();
        PlayerSession playerSession = StaffPlus.get().iocContainer.get(SessionManagerImpl.class).get(player.getUniqueId());

        Items.ItemStackBuilder itemStackBuilder = Items.editor(Items.createSkull(player.getName()))
            .setName("&b" + player.getName())
            .addLore("&7" + location.getWorld().getName() + " &8 | &7" + JavaUtils.serializeLocation(location));

        if (StaffPlus.get().iocContainer.get(PermissionHandler.class).has(staffViewing, StaffPlus.get().iocContainer.get(Options.class).permissionCounterGuiShowVanish)) {
            itemStackBuilder.addLore("&7Vanished: " + playerSession.isVanished());
        }

        return itemStackBuilder.build();
    }
}