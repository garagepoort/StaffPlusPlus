package net.shortninja.staffplus.core.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.tubinggui.GuiActionBuilder;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.playernotes.PlayerNote;
import net.shortninja.staffplus.core.playernotes.PlayerNoteService;
import net.shortninja.staffplus.core.examine.config.ExamineConfiguration;
import net.shortninja.staffplus.core.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class NotesExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;

    private final ExamineConfiguration examineConfiguration;
    private final PlayerNoteService playerNoteService;

    public NotesExamineGuiProvider(Messages messages, ExamineConfiguration examineConfiguration, PlayerNoteService playerNoteService) {
        this.messages = messages;
        this.examineConfiguration = examineConfiguration;
        this.playerNoteService = playerNoteService;
    }

    @Override
    public ItemStack getItem(Player staff, SppPlayer player) {
        return notesItem(staff, player);
    }

    @Override
    public String getClickAction(Player staff, SppPlayer targetPlayer) {
        return GuiActionBuilder.builder()
            .action("player-notes/create")
            .param("targetPlayerName", targetPlayer.getUsername())
            .build();
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineConfiguration.modeExamineNotes >= 0;
    }

    @Override
    public int getSlot() {
        return examineConfiguration.modeExamineNotes - 1;
    }

    private ItemStack notesItem(Player staff, SppPlayer target) {
        List<PlayerNote> allPlayerNotes = playerNoteService.getAllPlayerNotes(staff, target.getId(), 0, 10);
        List<String> notes = allPlayerNotes.isEmpty() ? Arrays.asList("&7No notes found") : allPlayerNotes.stream().map(PlayerNote::getNote).collect(Collectors.toList());

        ItemStack item = Items.builder()
            .setMaterial(Material.MAP).setAmount(1)
            .setName(messages.examineNotes)
            .setLore(notes)
            .build();

        return item;
    }
}
