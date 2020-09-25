package net.shortninja.staffplus.server.data.storage;

import net.shortninja.staffplus.session.PlayerSession;

public interface IStorage {

    short getGlassColor(PlayerSession playerSession);

    void setGlassColor(PlayerSession playerSession, short glassColor);

}
