package net.shortninja.staffplus.core.domain.staff.warn.warnings.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.evidence.EvidenceDetailGuiProvider;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplusplus.investigate.EvidenceType;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

@IocBean
@IocMultiProvider(EvidenceDetailGuiProvider.class)
public class WarningEvidenceDetailGuiProvider implements EvidenceDetailGuiProvider {

    private final WarnService warnService;

    public WarningEvidenceDetailGuiProvider(WarnService muteService) {
        this.warnService = muteService;
    }

    @Override
    public AbstractGui get(Player player, SppPlayer target, int id, Supplier<AbstractGui> previousGuiSupplier) {
        Warning warning = warnService.getWarning(id);
        return new ManageWarningGui(player, "Manage warning", warning, previousGuiSupplier);
    }

    @Override
    public EvidenceType getType() {
        return EvidenceType.WARNING;
    }
}
