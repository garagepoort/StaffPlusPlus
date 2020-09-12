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
        if (options.storageType.equalsIgnoreCase("flatfile")) {
            dataFile.set(node.prefix() + "name", node.name());
//            dataFile.set(node.prefix() + "password", node.password());
            dataFile.set(node.prefix() + "glass-color", node.glassColor());
            dataFile.set(node.prefix() + "reports", node.reports());
            dataFile.set(node.prefix() + "warnings", node.warnings());
            dataFile.set(node.prefix() + "notes", node.playerNotes());
            dataFile.set(node.prefix() + "alert-options", node.alertOptions());
        }/*else{
			try {
				PreparedStatement ao = connection.getConnection().prepareStatement("Inset into sp_alert_options (Name_Change,Mention,Xray,Player_UUID)");
				String[] nameChange = node.alertOptions().get(0).split(";");
				String[] mention = node.alertOptions().get(1).split(";");
				String[] xray = node .alertOptions().get(2).split(";");
				ao.setString(1,nameChange[1]);
				ao.setString(2, mention[1]);
				ao.setString(3, xray[1]);
				ao.setString(4,node.getUUID().toString());
				ao.executeUpdate();
				PreparedStatement ps = connection.getConnection().prepareStatement("Insert into sp_playerdata (GlassColor,Password,UUID)" +
						"");
				ps.setShort(1,node.glassColor());
				ps.setString(2,node.password());
				ps.setString(3,node.getUUID().toString());
				ps.executeUpdate();
				ps.close();
				ao.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}*/
    }
}