package net.shortninja.staffplus.staff.protect.database;

import net.shortninja.staffplus.staff.protect.ProtectedArea;
import org.bukkit.entity.Player;

import java.util.List;

public interface ProtectedAreaRepository {

    void addProtectedArea(Player player, ProtectedArea protectedArea);

    List<ProtectedArea> getProtectedAreas();

//    void removeProtectedArea(int id);
//
//    Optional<ProtectedArea> findProtectedArea(int id);

}
