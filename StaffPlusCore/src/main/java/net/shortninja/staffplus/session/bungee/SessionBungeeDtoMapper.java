package net.shortninja.staffplus.session.bungee;

import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;

public class SessionBungeeDtoMapper {

    private final Options options;

    public SessionBungeeDtoMapper(Options options) {
        this.options = options;
    }

    public boolean shouldSync() {
        return options.vanishSyncEnabled || options.modeConfiguration.isBungeeSyncEnabled();
    }

    public SessionBungeeDto map(PlayerSession playerSession) {
        SessionBungeeDto sessionBungeeDto = new SessionBungeeDto();
        sessionBungeeDto.setPlayerUuid(playerSession.getUuid());
        if (options.vanishSyncEnabled) {
            sessionBungeeDto.setVanishType(playerSession.getVanishType());
        }
        if (options.modeConfiguration.isBungeeSyncEnabled()) {
            sessionBungeeDto.setStaffMode(playerSession.isInStaffMode());
        }
        return sessionBungeeDto;
    }
}
