package net.shortninja.staffplus.core.domain.staff.investigate.gui.actions;

import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationService;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ResumeInvestigationAction implements IAction {

    private final InvestigationService investigationService;
    private final Investigation investigation;
    private final PlayerManager playerManager;

    public ResumeInvestigationAction(InvestigationService investigationService, Investigation investigation, PlayerManager playerManager) {
        this.investigationService = investigationService;
        this.investigation = investigation;
        this.playerManager = playerManager;
    }

    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        SppPlayer investigated = playerManager.getOnOrOfflinePlayer(investigation.getInvestigatedUuid())
            .orElseThrow(() -> new BusinessException("Can't resume investigation. Player not found."));
        investigationService.resumeInvestigation(player, investigated);
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
