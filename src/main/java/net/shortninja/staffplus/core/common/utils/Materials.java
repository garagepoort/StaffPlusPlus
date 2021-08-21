package net.shortninja.staffplus.core.common.utils;


import net.shortninja.staffplus.core.common.JavaUtils;
import org.bukkit.Bukkit;

public enum Materials {
    SPAWNER("MOB_SPAWNER", "SPAWNER"),
    HEAD("SKULL_ITEM", "SKULL_ITEM"),
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
        String[] tmp = Bukkit.getVersion().split("MC: ");
        String version = tmp[tmp.length - 1].substring(0, 4);
        int ver = JavaUtils.parseMcVer(version);
        if(ver>=13) {
            return newName;
        } else
            return oldName;
    }
}
