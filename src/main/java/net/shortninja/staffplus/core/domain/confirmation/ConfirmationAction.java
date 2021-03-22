package net.shortninja.staffplus.core.domain.confirmation;

import org.bukkit.entity.Player;

public interface ConfirmationAction {
    void execute(Player player);
}
