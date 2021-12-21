package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.model.TubingGui;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.gui.PagedGuiBuilder;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static be.garagepoort.mcioc.gui.model.TubingGuiActions.NOOP;

@IocBean
public class MyWarningsViewBuilder {

    private static final int PAGE_SIZE = 45;
    private final PermissionHandler permission;
    private final WarnService warnService;
    private final WarningItemBuilder warningItemBuilder;
    private final Options options;

    public MyWarningsViewBuilder(PermissionHandler permission, WarnService warnService, WarningItemBuilder warningItemBuilder, Options options) {
        this.permission = permission;
        this.warnService = warnService;
        this.warningItemBuilder = warningItemBuilder;
        this.options = options;
    }

    public TubingGui buildGui(Player player, String currentAction, int page) {
        return new PagedGuiBuilder.Builder("My warnings")
            .addPagedItems(currentAction, getItems(player, page * PAGE_SIZE, PAGE_SIZE), warningItemBuilder::build, w -> getDetailAction(player, w, currentAction), page)
            .build();
    }

    @NotNull
    private String getDetailAction(Player player, Warning w, String currentAction) {
        if (options.warningAppealConfiguration.enabled && permission.has(player, options.warningAppealConfiguration.createAppealPermission)) {
            return GuiActionBuilder.builder()
                .action("manage-warnings/view/detail")
                .param("warningId", String.valueOf(w.getId()))
                .param("backAction", currentAction)
                .build();
        }
        return NOOP;
    }

    public List<Warning> getItems(Player player, int offset, int amount) {
        return warnService
            .getWarnings(player.getUniqueId(), offset, amount, false);
    }
}