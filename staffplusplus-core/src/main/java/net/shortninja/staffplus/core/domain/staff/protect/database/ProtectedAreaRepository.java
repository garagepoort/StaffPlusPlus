package net.shortninja.staffplus.core.domain.staff.protect.database;

import net.shortninja.staffplus.core.domain.staff.protect.ProtectedArea;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public interface ProtectedAreaRepository {

    void addProtectedArea(Player player, ProtectedArea protectedArea);

    List<ProtectedArea> getProtectedAreas();

    Optional<ProtectedArea> findById(int id);

    Optional<ProtectedArea> findByName(String name);

    void deleteProtectedArea(int id);

    List<ProtectedArea> getProtectedAreasPaginated(int offset, int amount);

}
