package net.shortninja.staffplus.core.domain.player.gui.hub.views;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.gui.GuiModeConfiguration;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@IocBean
public class MinerViewBuilder {
    private final Messages messages;
    private final Options options;
    private final PlayerManager playerManager;
    private final GuiModeConfiguration guiModeConfiguration;

    public MinerViewBuilder(Messages messages, Options options, PlayerManager playerManager) {
        this.messages = messages;
        this.options = options;
        this.playerManager = playerManager;
        guiModeConfiguration = options.staffItemsConfiguration.getGuiModeConfiguration();
    }

    public TubingGui buildGui(int page, String currentAction, String backAction) {
        List<? extends Player> players = getPlayers(page);
        return new PagedGuiBuilder.Builder(messages.colorize(guiModeConfiguration.modeGuiMinerTitle))
            .addPagedItems(currentAction, players, this::minerItem, p -> "miners/teleport?to=" + p.getUniqueId(), page)
            .backAction(backAction)
            .build();
    }

    private List<? extends Player> getPlayers(int page) {
        Collection<? extends Player> onlinePlayers = playerManager.getOnlinePlayers().stream()
            .filter(p -> p.getLocation().getBlockY() < options.staffItemsConfiguration.getGuiModeConfiguration().modeGuiMinerLevel)
            .collect(Collectors.toList());

        return JavaUtils.getPageOfList(new ArrayList<>(onlinePlayers), page, 45);
    }

    private ItemStack minerItem(Player player) {
        Location location = player.getLocation();

        return Items.editor(Items.createSkull(player.getName())).setAmount(1)
            .setName("&b" + player.getName())
            .addLore("&7" + location.getWorld().getName() + " &8 | &7" + JavaUtils.serializeLocation(location))
            .build();
    }
}