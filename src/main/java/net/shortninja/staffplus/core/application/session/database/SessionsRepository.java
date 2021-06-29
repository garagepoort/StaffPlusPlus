package net.shortninja.staffplus.core.application.session.database;

import java.util.Optional;
import java.util.UUID;

public interface SessionsRepository {

    int addSession(SessionEntity sessionEntity);

    void update(SessionEntity sessionEntity);

    Optional<SessionEntity> findSession(UUID uuid);
}
