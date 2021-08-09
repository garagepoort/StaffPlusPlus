package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.staffmode.ExitStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.IModeData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@IocBean
@IocListener
public class ExitStaffModeListener implements Listener {

    private final PlayerManager playerManager;
    private final Messages messages;
    private final ModeProvider modeProvider;

    public ExitStaffModeListener(PlayerManager playerManager, Messages messages, ModeProvider modeProvider) {
        this.playerManager = playerManager;
        this.messages = messages;
        this.modeProvider = modeProvider;
    }

    @EventHandler
    public void onExitStaffMode(ExitStaffModeEvent event) {
        Optional<SppPlayer> playerOptional = playerManager.getOnlinePlayer(event.getPlayerUuid());
        if (!playerOptional.isPresent()) {
            return;
        }

        Player player = playerOptional.get().getPlayer();
        GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, event.getMode());

        if (modeConfiguration.isModeOriginalLocation()) {
            player.teleport(event.getModeData().getPreviousLocation().setDirection(player.getLocation().getDirection()));
            messages.send(player, messages.modeOriginalLocation, messages.prefixGeneral);
        }

        resetPlayer(player, event.getModeData());
        messages.send(player, messages.modeStatus.replace("%status%", messages.disabled), messages.prefixGeneral);
    }

    private void resetPlayer(Player player, IModeData modeData) {
        JavaUtils.clearInventory(player);
        player.getInventory().setContents(modeData.getPlayerInventory());
        player.updateInventory();
        player.setExp(modeData.getXp());
        player.setAllowFlight(modeData.hasFlight());
        player.setGameMode(modeData.getGameMode());
        player.setFireTicks(modeData.getFireTicks());
    }

}
