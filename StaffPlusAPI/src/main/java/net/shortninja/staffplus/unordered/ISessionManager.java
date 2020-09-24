package net.shortninja.staffplus.unordered;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public interface ISessionManager {

    void initialize(Player player);

    Collection<IPlayerSession> getAll();

	IPlayerSession get(UUID id);

}
