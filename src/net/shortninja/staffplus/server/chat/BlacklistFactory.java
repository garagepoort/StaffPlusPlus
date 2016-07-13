package net.shortninja.staffplus.server.chat;

import java.util.Arrays;
import java.util.Collections;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Options;

public class BlacklistFactory
{
	private Options options = StaffPlus.get().options;
	private String originalMessage;
	private String censoredMessage;
	private boolean hasChanged = false;
	private static String[] words = null;
	
	public BlacklistFactory(String originalMessage)
	{
		if(words == null)
		{
			Collections.sort(options.chatBlacklistWords);
			words = options.chatBlacklistWords.toArray(new String[options.chatBlacklistWords.size()]);
		}
		
		this.originalMessage = originalMessage;
		this.censoredMessage = originalMessage;
	}
	
	public boolean hasChanged()
	{
		return hasChanged;
	}
	
	public String getResult()
	{
		return censoredMessage;
	}
	
	public BlacklistFactory runCheck()
	{
		censoredMessage = checkIllegalCharacters();
		censoredMessage = checkIllegalWords();
		
		return this;
	}
	
	private String checkIllegalCharacters()
	{
		String newMessage = originalMessage;
		
		for(String string : options.chatBlacklistCharacters)
		{
			if(newMessage.contains(string))
			{
				newMessage.replace(string, options.chatBlacklistCharacter);
				hasChanged = true;
			}
		}
		
		return newMessage;
	}
	
	private String checkIllegalWords()
	{
		String newMessage = originalMessage;
		
		for(String word : newMessage.split(" "))
		{
			if(isBlacklisted(word) && isBypassable(word))
			{
				newMessage.replace(word, options.chatBlacklistCharacter);
				hasChanged = true;
			}
		}
		
		return newMessage;
	}
	
	private boolean isBlacklisted(String string)
	{
		return Arrays.binarySearch(words, string) >= 0;
	}
	
	private boolean isBypassable(String word)
	{
		boolean isBypassable = false;
		
		for(String string : options.chatBlacklistAllowed)
		{
			if(string.contains(word))
			{
				isBypassable = true;
				break;
			}
		}
		
		return isBypassable;
	}
}