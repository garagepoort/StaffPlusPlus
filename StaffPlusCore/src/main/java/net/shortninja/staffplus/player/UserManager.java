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
    public IUser getOffline(UUID playerUuid) {
        if (staffPlus.options.offlinePlayersModeEnabled) {
            Optional<ProvidedPlayer> user = staffPlus.offlinePlayerProvider.findUser(playerUuid);
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

        Player player = Bukkit.getPlayer(playerName);
        if (player == null && !staffPlus.options.offlinePlayersModeEnabled) {
            return null;
        }

        if (player == null) {
            user = getOffline(playerName);
        } else {
            user = get(player.getUniqueId());
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


    @Override
    public IUser getOnOrOfflineUser(UUID playerUuid) {
        IUser user = null;

        Player player = Bukkit.getPlayer(playerUuid);
        if (player == null && !staffPlus.options.offlinePlayersModeEnabled) {
            return null;
        }

        if (player == null) {
            user = getOffline(playerUuid);
        } else {
            user = get(player.getUniqueId());
            if (user == null) {
                if (staffPlus.options.offlinePlayersModeEnabled) {
                    user = getOffline(playerUuid);
                } else {
                    return null;
                }
            }
        }
        return user;
    }

    public Optional<ProvidedPlayer> getOnOrOfflinePlayer(UUID playerUuid) {
        IUser user = null;

        Player player = Bukkit.getPlayer(playerUuid);
        if (player == null && !staffPlus.options.offlinePlayersModeEnabled) {
            return Optional.empty();
        }

        if (player == null) {
            return staffPlus.offlinePlayerProvider.findUser(playerUuid);
        }
        return Optional.of(new ProvidedPlayer(playerUuid, player.getName()));
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