package net.shortninja.staffplus.player.attribute;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SecurityHandler
{
	private static Map<UUID, String> hashedPasswords = new HashMap<UUID, String>();
	private MessageDigest encrypter;
	
	public SecurityHandler()
	{
		try
		{
			encrypter = MessageDigest.getInstance("MD5");
		}catch (NoSuchAlgorithmException exception)
		{
			exception.printStackTrace();
		}
	}
	
	public String getPassword(UUID uuid)
	{
		return hashedPasswords.containsKey(uuid) ? hashedPasswords.get(uuid) : "";
	}
	
	public boolean hasPassword(UUID uuid)
	{
		return hashedPasswords.containsKey(uuid);
	}
	
	public boolean matches(UUID uuid, String input)
	{
		return hash(input, uuid).equals(hashedPasswords.get(uuid));
	}
	
	public void setPassword(UUID uuid, String password, boolean shouldHash)
	{
		hashedPasswords.put(uuid, shouldHash ? hash(password, uuid) : password);
	}
	
	private String hash(String string, UUID uuid)
	{
		string += uuid.toString();
		encrypter.update(string.getBytes(), 0, string.length());
		return new BigInteger(1, encrypter.digest()).toString(16);
	}
}