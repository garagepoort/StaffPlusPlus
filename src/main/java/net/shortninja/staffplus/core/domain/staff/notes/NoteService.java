package net.shortninja.staffplus.core.domain.staff.notes;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@IocBean
public class NoteService {

    private final PlayerSettingsRepository playerSettingsRepository;
    private final Messages messages;

    public NoteService(PlayerSettingsRepository playerSettingsRepository, Messages messages) {
        this.playerSettingsRepository = playerSettingsRepository;
        this.messages = messages;
    }

    public void addPlayerNote(CommandSender sender, Player player, String note) {
        PlayerSettings session = playerSettingsRepository.get(player);
        session.addPlayerNote(note);
        playerSettingsRepository.save(session);
        messages.send(sender, messages.noteAdded.replace("%target%", player.getName()), messages.prefixGeneral);
    }

    public void listNotes(CommandSender sender, Player player) {
        PlayerSettings user = playerSettingsRepository.get(player);
        List<String> notes = user.getPlayerNotes();

        for (String message : messages.noteListStart) {
            this.messages.send(sender, message.replace("%longline%", this.messages.LONG_LINE).replace("%target%", player.getName()).replace("%notes%", Integer.toString(notes.size())), message.contains("%longline%") ? "" : messages.prefixGeneral);
        }

        for (int i = 0; i < notes.size(); i++) {
            String note = notes.get(i);
            messages.send(sender, messages.noteListEntry.replace("%count%", Integer.toString(i + 1)).replace("%note%", note), messages.prefixGeneral);
        }

        for (String message : messages.noteListEnd) {
            this.messages.send(sender, message.replace("%longline%", this.messages.LONG_LINE).replace("%target%", player.getName()).replace("%notes%", Integer.toString(notes.size())), message.contains("%longline%") ? "" : messages.prefixGeneral);
        }
    }

    public void clearNotes(CommandSender sender, Player player) {
        PlayerSettings session = playerSettingsRepository.get(player);
        session.getPlayerNotes().clear();
        playerSettingsRepository.save(session);
        messages.send(sender, messages.noteCleared.replace("%target%", player.getName()), messages.prefixGeneral);
    }
}
