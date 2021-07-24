package net.shortninja.staffplus.core.domain.player.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder.Builder;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@IocBean
@GuiController
public class CounterGuiController {

    private final Options options;

    public CounterGuiController(Options options) {
        this.options = options;
    }

    @GuiAction("counter/items")
    public TubingGui getItems(Player staffViewing, @GuiParam("page") int page) {
        int amount = 45;
        List<Player> players = options.staffItemsConfiguration.getCounterModeConfiguration().isModeCounterShowStaffMode() ? getModePlayers() : JavaUtils.getOnlinePlayers();
        List<Player> pageOfList = JavaUtils.getPageOfList(players, page, amount);
        List<ItemStack> items = pageOfList.stream()
            .filter(p -> StaffPlus.get().getIocContainer().get(PermissionHandler.class).has(p, StaffPlus.get().getIocContainer().get(Options.class).permissionMember))
            .map(p -> modePlayerItem(staffViewing, p))
            .collect(Collectors.toList());
        return new Builder(options.staffItemsConfiguration.getCounterModeConfiguration().getTitle())
            .addPagedItems("counter/items", items, page, amount)
            .build();
    }

    private List<Player> getModePlayers() {
        return StaffPlus.get().getIocContainer().get(StaffModeService.class).getModeUsers()
            .stream()
            .map(Bukkit::getPlayer)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private ItemStack modePlayerItem(Player staffViewing, Player player) {
        Location location = player.getLocation();
        PlayerSession playerSession = StaffPlus.get().getIocContainer().get(SessionManagerImpl.class).get(player.getUniqueId());

        Items.ItemStackBuilder itemStackBuilder = Items.editor(Items.createSkull(player.getName()))
            .setName("&b" + player.getName())
            .addLore("&7" + location.getWorld().getName() + " &8 | &7" + JavaUtils.serializeLocation(location));

        if (StaffPlus.get().getIocContainer().get(PermissionHandler.class).has(staffViewing, StaffPlus.get().getIocContainer().get(Options.class).permissionCounterGuiShowVanish)) {
            itemStackBuilder.addLore("&7Vanished: " + playerSession.isVanished());
        }

        return itemStackBuilder.build();
    }
}
