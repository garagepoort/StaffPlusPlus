package net.shortninja.staffplus.util.lib.json;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.util.MessageCoordinator;

import org.json.simple.JSONObject;

/**
 * @author JustisR, Shortninja
 */

public class JsonMessage
{
	private MessageCoordinator message = StaffPlus.get().message;
	String msg;
	
	/**
	 * Create a new json message!
	 */
	public JsonMessage()
	{
		msg = "[{\"text\":\"\",\"extra\":[{\"text\": \"\"}";
	}
	
	public String getMessage()
	{
		return msg + "]}]";
	}
	
	/**
	 * Append text to the json message.
	 * 
	 * @param text to be appended
	 * @return json string builder
	 */
	public JsonStringBuilder append(String text)
	{
		return new JsonStringBuilder(this, esc(text));
	}
	
	/**
	 * Appends text and saves at the same time
	 * 
	 * @param text to be appended
	 * @return json message
	 */
	public JsonMessage appendSave(String text)
	{
		return append(text).save();
	}
	
	private String esc(String s)
	{
		return JSONObject.escape(message.colorize(s));
	}
}