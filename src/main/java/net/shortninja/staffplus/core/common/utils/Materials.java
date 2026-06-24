package net.shortninja.staffplus.core.common.utils;


import net.shortninja.staffplus.core.common.JavaUtils;
import org.bukkit.Bukkit;

public enum Materials {
    SPAWNER("MOB_SPAWNER", "SPAWNER"),
    HEAD("SKULL_ITEM", "PLAYER_HEAD"),
    ENDEREYE("EYE_OF_ENDER", "ENDER_EYE"),
    CLOCK("WATCH", "CLOCK"),
    LEAD("LEASH", "LEAD"),
    INK("INK_SACK", "INK_SAC");

    private final String oldName, newName;

    Materials(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getName() {
        if (JavaUtils.isMcVerGreaterOrEqual(1, 13, 0)) {
            return newName;
        }
        
        return oldName;
    }
}
