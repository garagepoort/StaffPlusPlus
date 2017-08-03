package net.shortninja.staffplus.server.data.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.StaffPlus;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DataFile
{
	private MessageCoordinator message;
	private String resourceName;
	private File file;
	private YamlConfiguration configuration;

	public DataFile(String name)
	{
		message = StaffPlus.get().message;
		resourceName = name;
		file = new File(StaffPlus.get().getDataFolder(), resourceName);

		if(!file.getParentFile().exists())
		{
			file.getParentFile().mkdir();
		}

		if(!file.exists())
		{
			StaffPlus.get().saveResource(resourceName, false);
		}

		configuration = YamlConfiguration.loadConfiguration(file);
	}

	public FileConfiguration getConfiguration()
	{
		return configuration;
	}
	
	public void save()
	{
		try
		{
			configuration.save(file);
		}catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}
	
	public double getDouble(String path)
	{
		if(configuration.contains(path))
		{
			return configuration.getDouble(path);
		}
		
		return 0;
	}

	public int getInt(String path)
	{
		if(configuration.contains(path))
		{
			return configuration.getInt(path);
		}
		
		return 0;
	}

	public boolean getBoolean(String path, boolean isStatus)
	{
		boolean value = false;
		
		if(configuration.contains(path))
		{
			value = configuration.getBoolean(path);
		}
		
		return value;
	}

	public String getString(String path)
	{
		if(configuration.contains(path))
		{
			return message.colorize(configuration.getString(path));
		}
		
		return "null";
	}

	public List<String> getStringList(String path)
	{
		if(configuration.contains(path))
		{
			ArrayList<String> strings = new ArrayList<>();
			
			for(String string : configuration.getStringList(path))
			{
				strings.add(message.colorize(string));
			}
			
			return strings;
		}
		
		return Arrays.asList("null");
	}
}