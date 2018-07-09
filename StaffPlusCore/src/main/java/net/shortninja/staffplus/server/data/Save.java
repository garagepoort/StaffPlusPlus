package net.shortninja.staffplus.server.data;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.NodeUser;

import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class Save
{
	private FileConfiguration dataFile = StaffPlus.get().dataFile.getConfiguration();
	private NodeUser node;
	private MySQLConnection connection = new MySQLConnection();
	private Options options = StaffPlus.get().options;

	public Save(NodeUser node)
	{
		this.node = node;
		
		saveUser();
	}
	
	private void saveUser()
	{
		if(options.storageType.equalsIgnoreCase("flatfile")) {
			dataFile.set(node.prefix() + "name", node.name());
			dataFile.set(node.prefix() + "password", node.password());
			dataFile.set(node.prefix() + "glass-color", node.glassColor());
			dataFile.set(node.prefix() + "reports", node.reports());
			dataFile.set(node.prefix() + "warnings", node.warnings());
			dataFile.set(node.prefix() + "notes", node.playerNotes());
			dataFile.set(node.prefix() + "alert-options", node.alertOptions());
		}else{
			try {
				PreparedStatement reports = null;
				PreparedStatement warnings = null;
				for(String string : node.reports()){
					String[] parts = string.split(";");
					UUID reporterUuid = UUID.fromString(parts[2]);
					reports = connection.getConnection().prepareStatement("Inert into sp_reports (Reason,Reporter_UUID,Player_UUID) Values(?,?,?)");
					reports.setString(2, parts[0]);
					reports.setString(3,reporterUuid.toString());
					reports.setString(4,node.getUUID().toString());
					reports.executeUpdate();
				}
				for(String string : node.warnings()){
					String[] parts = string.split(";");
					UUID warnererUuid = UUID.fromString(parts[2]);
					warnings = connection.getConnection().prepareStatement("Inert into sp_reports (Reason,Reporter_UUID,Player_UUID) Values(?,?,?)");
					warnings.setString(2, parts[0]);
					warnings.setString(3,warnererUuid.toString());
					warnings.setString(4,node.getUUID().toString());
					warnings.executeUpdate();
				}
				PreparedStatement ao = connection.getConnection().prepareStatement("Inset into sp_alert_options (Name_Change,Mention,Xray,Player_UUID)");
				String[] nameChange = node.alertOptions().get(0).split(";");
				String[] mention = node.alertOptions().get(1).split(";");
				String[] xray = node .alertOptions().get(2).split(";");
				ao.setString(0,nameChange[1]);
				ao.setString(1, mention[1]);
				ao.setString(2, xray[1]);
				ao.setString(3,node.getUUID().toString());
				ao.executeUpdate();
				PreparedStatement ps = connection.getConnection().prepareStatement("Insert into sp_playerdata (GlassColor,Password,UUID)" +
						"");
				ps.setShort(0,node.glassColor());
				ps.setString(1,node.password());
				ps.setString(2,node.getUUID().toString());
				ps.executeUpdate();
				ps.close();
				ao.close();
				reports.close();
				warnings.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}