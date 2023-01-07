package net.shortninja.staffplus.core.domain.webui;

import java.util.UUID;

public interface WebUiRegistrationRepository {

    void addRegistrationRequest(String playerName, UUID playerUuid, String authenticationKey, String role);
}
