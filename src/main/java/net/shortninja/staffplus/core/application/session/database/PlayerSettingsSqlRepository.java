package net.shortninja.staffplus.core.application.session.database;

import java.util.Optional;
import java.util.UUID;

public interface PlayerSettingsSqlRepository {

    int saveSessions(PlayerSettingsEntity playerSettingsEntity);

    void update(PlayerSettingsEntity playerSettingsEntity);

    Optional<PlayerSettingsEntity> findSettings(UUID uuid);
}
