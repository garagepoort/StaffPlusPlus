package net.shortninja.staffplus.server.data.storage;

import net.shortninja.staffplus.session.PlayerSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MemoryStorage implements IStorage {

    private final Map<UUID, DataHolder> data = new HashMap<>();

    @Override
    public short getGlassColor(PlayerSession playerSession) {
        return getOrPut(playerSession.getUuid()).glassColor;
    }

    @Override
    public void setGlassColor(PlayerSession playerSession, short glassColor) {
        getOrPut(playerSession.getUuid()).glassColor = glassColor;
    }

    private DataHolder getOrPut(UUID id) {
        if (data.containsKey(id)) {
            return data.get(id);
        } else {
            final DataHolder holder = new DataHolder();
            data.putIfAbsent(id, holder);

            return holder;
        }
    }

    private static class DataHolder {
        private short glassColor;
    }
}
