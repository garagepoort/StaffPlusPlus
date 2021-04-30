package net.shortninja.staffplus.core.domain.webui;

import java.util.UUID;

public interface WebUiRegistrationRepository {

    void addRegistrationRequest(UUID playerUuid, String authenticationKey);
}
