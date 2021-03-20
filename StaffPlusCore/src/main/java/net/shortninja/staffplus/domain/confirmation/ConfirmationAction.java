package net.shortninja.staffplus.domain.confirmation;

import org.bukkit.entity.Player;

public interface ConfirmationAction {
    void execute(Player player);
}
