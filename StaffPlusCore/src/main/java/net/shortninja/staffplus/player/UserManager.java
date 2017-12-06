package net.shortninja.staffplus.player;

import net.shortninja.staffplus.StaffPlus;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager
{
	private final StaffPlus staffPlus;
	private static final Map<UUID, User> users = new HashMap<>();

	public UserManager(StaffPlus staffPlus){
		this.staffPlus = staffPlus;
	}

	public Collection<User> getAll()
	{
		return staffPlus.users.values();
	}
	
	public User get(UUID uuid)
	{
		return users.get(uuid);
	}
	
	public boolean has(UUID uuid)
	{
		return staffPlus.users.containsKey(uuid);
	}
	
	public void add(User user)
	{
		staffPlus.users.put(user.getUuid(), user);
	}

	public void remove(UUID uuid)
	{
		staffPlus.users.remove(uuid);
		Bukkit.getServer().getLogger().info("Remove called");
	}
}