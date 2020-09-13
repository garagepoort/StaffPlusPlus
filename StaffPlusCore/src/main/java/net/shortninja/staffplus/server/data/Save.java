package net.shortninja.staffplus.server.data;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.NodeUser;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.configuration.file.FileConfiguration;

public class Save {
    private FileConfiguration dataFile = StaffPlus.get().dataFile.getConfiguration();
    private NodeUser node;
    private Options options = StaffPlus.get().options;

    public Save(NodeUser node) {
        this.node = node;

        saveUser();
    }

    private void saveUser() {
        dataFile.set(node.prefix() + "name", node.name());
//            dataFile.set(node.prefix() + "password", node.password());
        dataFile.set(node.prefix() + "glass-color", node.glassColor());
        dataFile.set(node.prefix() + "warnings", node.warnings());
        dataFile.set(node.prefix() + "notes", node.playerNotes());
        dataFile.set(node.prefix() + "alert-options", node.alertOptions());
    }
}