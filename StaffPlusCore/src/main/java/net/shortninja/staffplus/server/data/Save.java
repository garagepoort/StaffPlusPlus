package net.shortninja.staffplus.server.data;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.NodeUser;
import org.bukkit.configuration.file.FileConfiguration;

public class Save {
    private final FileConfiguration dataFile = StaffPlus.get().dataFile.getConfiguration();
    private final NodeUser node;

    public Save(NodeUser node) {
        this.node = node;
        saveUser();
    }

    private void saveUser() {
        dataFile.set(node.prefix() + "name", node.name());
        dataFile.set(node.prefix() + "glass-color", node.glassColor());
        dataFile.set(node.prefix() + "notes", node.playerNotes());
        dataFile.set(node.prefix() + "alert-options", node.alertOptions());
    }
}