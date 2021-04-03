package net.shortninja.staffplus.core.domain.staff.mute.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.investigate.gui.evidence.EvidenceDetailGuiProvider;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplusplus.investigate.EvidenceType;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

@IocBean
@IocMultiProvider(EvidenceDetailGuiProvider.class)
public class MuteEvidenceDetailGuiProvider implements EvidenceDetailGuiProvider {

    private final MuteService banService;

    public MuteEvidenceDetailGuiProvider(MuteService muteService) {
        this.banService = muteService;
    }

    @Override
    public AbstractGui get(Player player, SppPlayer target, int id, Supplier<AbstractGui> previousGuiSupplier) {
        Mute mute = banService.getById(id);
        return new ManageMutedPlayerGui("Player: " + mute.getTargetName(), mute, previousGuiSupplier);
    }

    @Override
    public EvidenceType getType() {
        return EvidenceType.MUTE;
    }
}
