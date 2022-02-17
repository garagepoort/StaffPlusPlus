package net.shortninja.staffplus.core.domain.player.namechanged.bungee;

import net.shortninja.staffplus.core.common.bungee.BungeeMessage;
import net.shortninja.staffplusplus.chat.NameChangeEvent;

import java.util.UUID;

public class NameChangeBungeeDto extends BungeeMessage {

    private final UUID playerUuid;
    private final String oldName;
    private final String newName;

    public NameChangeBungeeDto(NameChangeEvent nameChangeEvent) {
        super(nameChangeEvent.getServerName());
        this.playerUuid = nameChangeEvent.getPlayer().getUniqueId();
        this.oldName = nameChangeEvent.getOldName();
        this.newName = nameChangeEvent.getNewName();
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }
}
