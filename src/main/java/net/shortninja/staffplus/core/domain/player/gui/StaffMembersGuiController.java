package net.shortninja.staffplus.core.domain.player.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Messages;
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
public class StaffMembersGuiController {

    @ConfigProperty("permissions:member")
    private String permissionMember;

    private final Options options;
    private final StaffModeService staffModeService;
    private final SessionManagerImpl sessionManager;
    private final PermissionHandler permissionHandler;
    private final Messages messages;

    public StaffMembersGuiController(Options options, StaffModeService staffModeService, SessionManagerImpl sessionManager, PermissionHandler permissionHandler, Messages messages) {
        this.options = options;
        this.staffModeService = staffModeService;
        this.sessionManager = sessionManager;
        this.permissionHandler = permissionHandler;
        this.messages = messages;
    }

    @GuiAction("membersGUI")
    public TubingGui getItems(Player staffViewing, @GuiParam("page") int page) {
        int amount = 45;
        List<Player> players = options.staffItemsConfiguration.getCounterModeConfiguration().isModeCounterShowStaffMode() ? getModePlayers() : JavaUtils.getOnlinePlayers();
        List<Player> pageOfList = JavaUtils.getPageOfList(players, page, amount);
        List<ItemStack> items = pageOfList.stream()
            .filter(p -> permissionHandler.has(p, permissionMember))
            .map(p -> modePlayerItem(staffViewing, p))
            .collect(Collectors.toList());
        return new Builder(messages.colorize(options.staffItemsConfiguration.getCounterModeConfiguration().getTitle()))
            .addPagedItems("membersGUI", items, page, amount)
            .build();
    }

    private List<Player> getModePlayers() {
        return staffModeService.getModeUsers()
            .stream()
            .map(Bukkit::getPlayer)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private ItemStack modePlayerItem(Player staffViewing, Player player) {
        Location location = player.getLocation();
        PlayerSession playerSession = sessionManager.get(player.getUniqueId());

        Items.ItemStackBuilder itemStackBuilder = Items.editor(Items.createSkull(player.getName()))
            .setName("&b" + player.getName())
            .addLore("&7" + location.getWorld().getName() + " &8 | &7" + JavaUtils.serializeLocation(location));

        if (permissionHandler.has(staffViewing, options.permissionCounterGuiShowVanish)) {
            itemStackBuilder.addLore("&7Vanished: " + playerSession.isVanished());
        }

        return itemStackBuilder.build();
    }
}
