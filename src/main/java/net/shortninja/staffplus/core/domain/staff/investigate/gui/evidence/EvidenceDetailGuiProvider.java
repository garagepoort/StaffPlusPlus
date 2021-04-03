package net.shortninja.staffplus.core.domain.staff.investigate.gui.evidence;

import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplusplus.investigate.EvidenceType;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

public interface EvidenceDetailGuiProvider {

    AbstractGui get(Player player, SppPlayer target, int id, Supplier<AbstractGui> previousGuiSupplier);

    EvidenceType getType();
}
