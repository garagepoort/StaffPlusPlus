package net.shortninja.staffplus.core.domain.player.gui.hub;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.CurrentAction;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.gui.hub.views.MinerViewBuilder;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

@IocBean
@GuiController
public class MinerGuiController {

    private final MinerViewBuilder minerViewBuilder;
    private final PlayerManager playerManager;
    private final Messages messages;

    public MinerGuiController(MinerViewBuilder minerViewBuilder, PlayerManager playerManager, Messages messages) {
        this.minerViewBuilder = minerViewBuilder;
        this.playerManager = playerManager;
        this.messages = messages;
    }

    @GuiAction("miners/view")
    public TubingGui getMinersView(@GuiParam(value = "page", defaultValue = "0") int page,
                                   @CurrentAction String currentAction,
                                   @GuiParam("backAction") String backAction) {
        return minerViewBuilder.buildGui(page, currentAction, backAction);
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
