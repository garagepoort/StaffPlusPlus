package net.shortninja.staffplus.core.domain.player.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder.Builder;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@IocBean
@GuiController
public class StaffMembersGuiController {

    @ConfigProperty("permissions:member")
    private String permissionMember;
    @ConfigProperty("permissions:counter-show-vanished")
    private String permissionCounterGuiShowVanish;

    private final Options options;
    private final OnlineSessionsManager sessionManager;
    private final PermissionHandler permissionHandler;
    private final Messages messages;
    private final PlayerManager playerManager;

    public StaffMembersGuiController(Options options,
                                     OnlineSessionsManager sessionManager,
                                     PermissionHandler permissionHandler,
                                     Messages messages,
                                     PlayerManager playerManager) {
        this.options = options;
        this.sessionManager = sessionManager;
        this.permissionHandler = permissionHandler;
        this.messages = messages;
        this.playerManager = playerManager;
    }

    @GuiAction("membersGUI")
    public TubingGui getItems(Player staffViewing, @GuiParam(value = "page", defaultValue="0") int page) {
        int amount = 45;
        List<Player> players = options.staffItemsConfiguration.getCounterModeConfiguration().isModeCounterShowStaffMode() ? getModePlayers() : JavaUtils.getOnlinePlayers();
        List<Player> pageOfList = JavaUtils.getPageOfList(players, page, amount);
        List<ItemStack> items = pageOfList.stream()
            .filter(p -> permissionHandler.has(p, permissionMember))
            .map(p -> modePlayerItem(staffViewing, p))
            .collect(Collectors.toList());
        return new Builder(messages.colorize(options.staffItemsConfiguration.getCounterModeConfiguration().getTitle()))
            .addPagedItems("membersGUI", items, page)
            .build();
    }

    private List<Player> getModePlayers() {
        return sessionManager.getAll().stream()
            .map(s -> playerManager.getOnlinePlayer(s.getUuid()))
            .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
            .map(SppPlayer::getPlayer)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private ItemStack modePlayerItem(Player staffViewing, Player player) {
        Location location = player.getLocation();
        OnlinePlayerSession playerSession = sessionManager.get(player);

        Items.ItemStackBuilder itemStackBuilder = Items.editor(Items.createSkull(player.getName()))
            .setName("&b" + player.getName())
            .addLore("&7" + location.getWorld().getName() + " &8 | &7" + JavaUtils.serializeLocation(location));

        if (permissionHandler.has(staffViewing, permissionCounterGuiShowVanish)) {
            itemStackBuilder.addLore("&7Vanished: " + playerSession.isVanished());
        }

        return itemStackBuilder.build();
    }
}
