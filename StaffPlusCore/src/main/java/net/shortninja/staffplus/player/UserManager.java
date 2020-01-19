package net.shortninja.staffplus.player;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.unordered.IUserManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager implements IUserManager {
    private static Map<UUID, IUser> users;
    private final StaffPlus staffPlus;

    public UserManager(StaffPlus staffPlus) {
        this.staffPlus = staffPlus;
        users = new HashMap<>();
        staffPlus.users = users;
    }

    @Override
    public Collection<IUser> getAll() {
        return staffPlus.users.values();
    }

    @Override
    public IUser get(UUID uuid) {
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
    }
}