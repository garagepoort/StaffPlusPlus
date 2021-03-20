package net.shortninja.staffplus.domain.staff.altaccountdetect;

import java.util.UUID;

public class AltDetectWhitelistedItem {

    private UUID playerUuid1;
    private UUID playerUuid2;

    public AltDetectWhitelistedItem(UUID playerUuid1, UUID playerUuid2) {
        this.playerUuid1 = playerUuid1;
        this.playerUuid2 = playerUuid2;
    }

    public boolean isWhitelisted(UUID playerUuid1, UUID playerUuid2) {
        return (this.playerUuid1.equals(playerUuid1) && this.playerUuid2.equals(playerUuid2))
            || (this.playerUuid1.equals(playerUuid2) && this.playerUuid2.equals(playerUuid1));
    }

    public UUID getPlayerUuid1() {
        return playerUuid1;
    }

    public UUID getPlayerUuid2() {
        return playerUuid2;
    }
}
