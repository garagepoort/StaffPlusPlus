package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.IocBean;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@IocBean
public class ModeDataRepository {

    private static final Map<UUID, ModeData> cache = new HashMap<>();
    private final ModeDataSerializer modeDataSerializer = new ModeDataSerializer();

    public Optional<ModeData> retrieveModeData(UUID uuid) {
        if (cache.containsKey(uuid)) {
            return Optional.ofNullable(cache.get(uuid));
        }
        Optional<ModeData> retrievedModeData = modeDataSerializer.retrieve(uuid);
        retrievedModeData.ifPresent(m -> cache.put(m.getUuid(), m));
        return retrievedModeData;
    }

    public void saveModeData(ModeData modeData) {
        if (cache.containsKey(modeData.getUuid())) {
            cache.put(modeData.getUuid(), modeData);
        }
        modeDataSerializer.save(modeData);
    }

    public void deleteModeData(Player player) {
        cache.remove(player.getUniqueId());
        modeDataSerializer.delete(player.getUniqueId().toString());
    }
}
