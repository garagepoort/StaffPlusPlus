package be.garagepoort.staffplusplus.discord;

import org.bukkit.event.Listener;

public interface StaffPlusPlusListener extends Listener {

    void init();

    boolean isEnabled();

    boolean isValid();
}
