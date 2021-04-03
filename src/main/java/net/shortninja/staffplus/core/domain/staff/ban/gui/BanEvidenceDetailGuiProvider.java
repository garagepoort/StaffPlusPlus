package net.shortninja.staffplus.core.domain.staff.ban.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.ban.Ban;
import net.shortninja.staffplus.core.domain.staff.ban.BanService;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.evidence.EvidenceDetailGuiProvider;
import net.shortninja.staffplusplus.investigate.EvidenceType;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

@IocBean
@IocMultiProvider(EvidenceDetailGuiProvider.class)
public class BanEvidenceDetailGuiProvider implements EvidenceDetailGuiProvider {

    private final BanService banService;

    public BanEvidenceDetailGuiProvider(BanService banService) {
        this.banService = banService;
    }

    @Override
    public AbstractGui get(Player player, SppPlayer target, int id, Supplier<AbstractGui> previousGuiSupplier) {
        Ban ban = banService.getById(id);
        return new ManageBannedPlayerGui("Player: " + ban.getTargetName(), ban, previousGuiSupplier);
    }

    @Override
    public EvidenceType getType() {
        return EvidenceType.BAN;
    }
}
