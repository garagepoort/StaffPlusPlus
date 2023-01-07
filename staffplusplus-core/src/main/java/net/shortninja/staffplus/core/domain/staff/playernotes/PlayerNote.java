package net.shortninja.staffplus.core.domain.staff.playernotes;

import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplusplus.playernotes.IPlayerNote;
import net.shortninja.staffplusplus.session.SppInteractor;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerNote implements IPlayerNote {

    private Long id;
    private final String note;
    private final UUID notedByUuid;
    private final String notedByName;
    private final String targetName;
    private final UUID targetUuid;
    private final Long creationTimestamp;
    private final boolean privateNote;
    private final String serverName;

    public PlayerNote(String note, CommandSender staff, SppPlayer target, boolean privateNote, String serverName) {
        this.note = note;
        this.notedByUuid = (staff instanceof Player) ? ((Player) staff).getUniqueId() : Constants.CONSOLE_UUID;
        this.notedByName = (staff instanceof Player) ? staff.getName() : "Console";
        this.targetUuid = target.getId();
        this.targetName = target.getUsername();
        this.privateNote = privateNote;
        this.serverName = serverName;
        this.creationTimestamp = System.currentTimeMillis();
    }

    public PlayerNote(String note, SppInteractor staff, SppPlayer target, boolean privateNote, String serverName) {
        this.note = note;
        this.notedByUuid = staff.getId();
        this.notedByName = staff.getUsername();
        this.targetUuid = target.getId();
        this.targetName = target.getUsername();
        this.privateNote = privateNote;
        this.serverName = serverName;
        this.creationTimestamp = System.currentTimeMillis();
    }

    public PlayerNote(Long id, String note, String notedByName, UUID notedByUuid, String targetName, UUID targetUuid, Long creationTimestamp, boolean privateNote, String serverName) {
        this.id = id;
        this.note = note;
        this.notedByUuid = notedByUuid;
        this.notedByName = notedByName;
        this.targetName = targetName;
        this.targetUuid = targetUuid;
        this.creationTimestamp = creationTimestamp;
        this.privateNote = privateNote;
        this.serverName = serverName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public UUID getNotedByUuid() {
        return notedByUuid;
    }

    public String getNotedByName() {
        return notedByName;
    }

    public String getTargetName() {
        return targetName;
    }

    public UUID getTargetUuid() {
        return targetUuid;
    }

    public Long getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getServerName() {
        return serverName;
    }

    @Override
    public boolean isPrivateNote() {
        return privateNote;
    }
}
