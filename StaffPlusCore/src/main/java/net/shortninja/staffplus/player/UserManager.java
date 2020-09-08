package net.shortninja.staffplus.player;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.Load;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.unordered.IUserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

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

    @Override
    public IUser getOffline(String playerName) {
        if (staffPlus.options.offlinePlayersModeEnabled) {
            Optional<ProvidedPlayer> user = staffPlus.offlinePlayerProvider.findUser(playerName);
            if (user.isPresent()) {
                User loadedUser = new Load().build(user.get().getId(), user.get().getUsername());
                loadedUser.setOnline(false);
                return loadedUser;
            }
        }
        return null;
    }

    @Override
    public IUser getOnOrOfflineUser(String playerName) {
        IUser user = null;

        Player reported = Bukkit.getPlayer(playerName);
        if (reported == null && !staffPlus.options.offlinePlayersModeEnabled) {
            return null;
        }

        if (reported == null) {
            user = getOffline(playerName);
        } else {
            user = get(reported.getUniqueId());
            if (user == null) {
                if (staffPlus.options.offlinePlayersModeEnabled) {
                    user = getOffline(playerName);
                } else {
                    return null;
                }
            }
        }
        return user;
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