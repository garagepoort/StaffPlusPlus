package net.shortninja.staffplus.unordered;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public interface IUserManager {

	Collection<IUser> getAll();

	IUser get(UUID id);

	IUser getOffline(String playerName);

	boolean playerExists(String playerName);

    Player getOnlinePlayer(String playerName);

    IUser getOffline(UUID playerUuid);

    IUser getOnOrOfflineUser(String playerName);

	IUser getOnOrOfflineUser(UUID playerUuid);
}
