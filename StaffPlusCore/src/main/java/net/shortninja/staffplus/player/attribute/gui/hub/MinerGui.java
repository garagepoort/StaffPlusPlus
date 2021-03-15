package net.shortninja.staffplus.player.attribute.gui.hub;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.common.gui.AbstractGui;
import net.shortninja.staffplus.player.attribute.gui.PagedGui;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.common.JavaUtils;
import net.shortninja.staffplus.common.Items;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MinerGui extends PagedGui {
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Options options = IocContainer.getOptions();
    private final Messages messages = IocContainer.getMessages();

    public MinerGui(Player player, String title, int page, Supplier<AbstractGui> backGuiSupplier) {
        super(player, title, page, backGuiSupplier);
    }

    @Override
    protected MinerGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new MinerGui(player, title, page, previousGuiSupplier);
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
    public List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount) {
        return IocContainer.getPlayerManager().getOnlinePlayers().stream()
            .filter(p -> p.getLocation().getBlockY() < IocContainer.getOptions().modeConfiguration.getGuiModeConfiguration().modeGuiMinerLevel)
            .map(this::minerItem)
            .collect(Collectors.toList());
    }

    private ItemStack minerItem(Player player) {
        Location location = player.getLocation();

        ItemStack item = Items.editor(Items.createSkull(player.getName())).setAmount(1)
                .setName("&b" + player.getName())
                .addLore("&7" + location.getWorld().getName() + " &8 | &7" + JavaUtils.serializeLocation(location))
                .build();

        return item;
    }
}