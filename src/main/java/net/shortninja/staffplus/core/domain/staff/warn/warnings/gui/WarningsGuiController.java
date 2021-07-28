package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.CurrentAction;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.TubingGui;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class WarningsGuiController {

    private final ManageWarningsViewBuilder manageWarningsViewBuilder;
    private final ManageWarningViewBuilder manageWarningViewBuilder;
    private final MyWarningsViewBuilder myWarningsViewBuilder;
    private final ManageAppealedWarningsViewBuilder manageAppealedWarningsViewBuilder;
    private final PlayerManager playerManager;
    private final WarnService warnService;

    public WarningsGuiController(ManageWarningsViewBuilder manageWarningsViewBuilder,
                                 ManageWarningViewBuilder manageWarningViewBuilder,
                                 MyWarningsViewBuilder myWarningsViewBuilder,
                                 ManageAppealedWarningsViewBuilder manageAppealedWarningsViewBuilder,
                                 PlayerManager playerManager,
                                 WarnService warnService) {
        this.manageWarningsViewBuilder = manageWarningsViewBuilder;
        this.manageWarningViewBuilder = manageWarningViewBuilder;
        this.myWarningsViewBuilder = myWarningsViewBuilder;
        this.manageAppealedWarningsViewBuilder = manageAppealedWarningsViewBuilder;
        this.playerManager = playerManager;
        this.warnService = warnService;
    }

    @GuiAction("manage-warnings/view/overview")
    public TubingGui warningsOverview(@GuiParam("targetPlayerName") String targetPlayerName,
                                      @CurrentAction String currentAction,
                                      @GuiParam(value = "page", defaultValue = "0") int page) {
        if (StringUtils.isNotBlank(targetPlayerName)) {
            SppPlayer target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow((() -> new PlayerNotFoundException(targetPlayerName)));
            return manageWarningsViewBuilder.buildGui(target, currentAction, page);
        }
        return manageWarningsViewBuilder.buildGui(null, currentAction, page);
    }

    @GuiAction("manage-warnings/view/my-warnings")
    public TubingGui myWarningsOverview(Player player,
                                        @CurrentAction String currentAction,
                                        @GuiParam(value = "page", defaultValue = "0") int page) {
        return myWarningsViewBuilder.buildGui(player, currentAction, page);
    }

    @GuiAction("manage-warnings/view/appealed-warnings")
    public TubingGui appealedWarningsOverview(@CurrentAction String currentAction,
                                              @GuiParam(value = "page", defaultValue = "0") int page) {
        return manageAppealedWarningsViewBuilder.buildGui(currentAction, page);
    }

    @GuiAction("manage-warnings/view/detail")
    public TubingGui warningDetail(Player player,
                                   @GuiParam("warningId") int warningId,
                                   @CurrentAction String currentAction,
                                   @GuiParam("backAction") String backAction) {
        Warning warning = warnService.getWarning(warningId);
        return manageWarningViewBuilder.buildGui(player, warning, currentAction, backAction);
    }

    @GuiAction("manage-warnings/delete")
    public String deleteWarning(@GuiParam("warningId") int warningId,
                                @GuiParam("backAction") String backAction) {
        warnService.removeWarning(warningId);
        return backAction;
    }
}
