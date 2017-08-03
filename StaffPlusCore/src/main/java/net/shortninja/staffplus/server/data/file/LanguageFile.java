package net.shortninja.staffplus.server.data.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.util.MessageCoordinator;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageFile
{
	private final String FILE_NAME = StaffPlus.get().getConfig().getString("lang") + ".yml";
	private FileConfiguration lang;
	private File langFile;
	private MessageCoordinator message = StaffPlus.get().message;
	
	public LanguageFile()
	{
		for(String fileName : LANG_FILES)
		{
			try
			{
				copyFile(fileName);
			}catch(IOException exception)
			{
				System.out.println(exception);
				message.sendConsoleMessage("Error occured while initializing '" + fileName + "'!", true);
			}
		}
		
		setup();
	}
	
	public void setup()
	{
		langFile = new File(StaffPlus.get().getDataFolder() + "/lang/", FILE_NAME);
		lang = YamlConfiguration.loadConfiguration(langFile);
	}
	
	public FileConfiguration get()
	{
		return lang;
	}
	
	private void copyFile(String fileName) throws IOException
	{
	    File file = new File(StaffPlus.get().getDataFolder() + "/lang/", fileName + ".yml");
	    InputStream in = this.getClass().getResourceAsStream("/lang/" + fileName + ".yml");
	    
	    if(!file.exists())
	    {
	    	StaffPlus.get().getDataFolder().mkdirs();
	    	file.getParentFile().mkdirs();
	        file.createNewFile();
	        message.sendConsoleMessage("Creating language file '" + fileName +"'.", false);

	    }else return;
	    
	    OutputStream out = new FileOutputStream(file);
	    byte[] buffer = new byte[1024];
	    int current = 0;
        
	    while((current = in.read(buffer)) > -1)
	    {
	        out.write(buffer, 0, current);
	    }
	    
	    out.close();
	    in.close();
	}
	
	private static final String[] LANG_FILES =
	{
		"lang_en", "lang_sv", "lang_de", "lang_nl", "lang_es", "lang_hr", "lang_no", "lang_fr"
	};
}