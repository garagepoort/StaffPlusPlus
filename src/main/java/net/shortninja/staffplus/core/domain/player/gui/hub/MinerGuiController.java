package net.shortninja.staffplus.core.domain.player.gui.hub;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static be.garagepoort.mcioc.gui.templates.GuiTemplate.template;

@IocBean
@GuiController
public class MinerGuiController {

    private final PlayerManager playerManager;
    private final Messages messages;
    private final Options options;

    public MinerGuiController(PlayerManager playerManager, Messages messages, Options options) {
        this.playerManager = playerManager;
        this.messages = messages;
        this.options = options;
    }

    @GuiAction("miners/view")
    public GuiTemplate getMinersView(@GuiParam(value = "page", defaultValue = "0") int page) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("players", getPlayers(page));
        return template("gui/player/miners-overview.ftl", params);
    }

    private List<SppPlayer> getPlayers(int page) {
        List<SppPlayer> onlinePlayers = playerManager.getOnlineSppPlayers().stream()
            .filter(p -> p.getPlayer().getLocation().getBlockY() < options.staffItemsConfiguration.getGuiModeConfiguration().modeGuiMinerLevel)
            .collect(Collectors.toList());

        return JavaUtils.getPageOfList(new ArrayList<>(onlinePlayers), page, 45);
    }

    @GuiAction("miners/teleport")
    public void changeGlass(Player player, @GuiParam("to") String toPlayerUuid) {
        Optional<SppPlayer> p = playerManager.getOnlinePlayer(UUID.fromString(toPlayerUuid));
        if (p.isPresent()) {
            player.teleport(p.get().getPlayer());
        } else {
            messages.send(player, messages.playerOffline, messages.prefixGeneral);
        }
    }

}
