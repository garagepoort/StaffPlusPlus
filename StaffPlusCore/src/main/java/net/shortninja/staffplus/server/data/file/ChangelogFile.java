package net.shortninja.staffplus.server.data.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.shortninja.staffplus.StaffPlus;

public class ChangelogFile
{
	public ChangelogFile()
	{
		try
		{
			copyFile();
		}catch(IOException | NullPointerException exception)
		{
			StaffPlus.get().message.sendConsoleMessage("Error occurred while copying 'changelog.txt'!", true);
		}
	}
	
	private void copyFile() throws IOException, NullPointerException
	{
	    File file = new File(StaffPlus.get().getDataFolder(), "changelog.txt");
	    InputStream in = this.getClass().getResourceAsStream("/changelog.txt");
	    OutputStream out = new FileOutputStream(file);
	    byte[] buffer = new byte[1024];
	    int current;
	    
	    if(!file.exists())
	    {
	    	StaffPlus.get().getDataFolder().mkdirs();
	        file.createNewFile();
	    }
	    
	    while((current = in.read(buffer)) > -1)
	    {
	        out.write(buffer, 0, current);
	    }
	    
	    in.close();
	    out.close();
	}
}