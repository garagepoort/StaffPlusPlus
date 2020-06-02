package net.shortninja.staffplus.util;

<<<<<<< HEAD
import net.shortninja.staffplus.StaffPlus;
=======
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
import net.shortninja.staffplus.util.lib.JavaUtils;
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
        String[] tmp = Bukkit.getVersion().split("MC: ");
        String version = tmp[tmp.length - 1].substring(0, 4);
        int ver = JavaUtils.parseMcVer(version);
<<<<<<< HEAD
            if(ver>=13) {
=======
        if (ver >= 13) {
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
            return newName;
        } else
            return oldName;
    }
}
