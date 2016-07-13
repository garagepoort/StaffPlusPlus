package net.shortninja.staffplus.player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager
{
	private static Map<UUID, User> users = new HashMap<UUID, User>();
	
	public Collection<User> getUsers()
	{
		return users.values();
	}
	
	public User getUser(UUID uuid)
	{
		return users.get(uuid);
	}
	
	public boolean hasUser(UUID uuid)
	{
		return users.containsKey(uuid);
	}
	
	public void addUser(User user)
	{
		users.put(user.getUuid(), user);
	}
	
	public void removeUser(String uuid)
	{
		users.remove(uuid);
	}
}