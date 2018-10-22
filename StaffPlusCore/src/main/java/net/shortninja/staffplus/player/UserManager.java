package net.shortninja.staffplus.player;

import net.shortninja.staffplus.StaffPlus;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {
    private static Map<UUID, User> users;
    private final StaffPlus staffPlus;

    public UserManager(StaffPlus staffPlus) {
        this.staffPlus = staffPlus;
        users = new HashMap<>();
        staffPlus.users = users;
    }

    public Collection<User> getAll() {
        return staffPlus.users.values();
    }

    public User get(UUID uuid) {
        return users.get(uuid);
    }

    public boolean has(UUID uuid) {
        return users.containsKey(uuid);
    }

    public void add(User user) {
        staffPlus.users.put(user.getUuid(), user);
    }

    public void remove(UUID uuid) {
        users.remove(uuid);
        Bukkit.getServer().getLogger().info("Remove called");
    }
}