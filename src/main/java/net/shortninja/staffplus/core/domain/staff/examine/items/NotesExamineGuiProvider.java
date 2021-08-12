package net.shortninja.staffplus.core.domain.staff.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class NotesExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;

    private final ExamineModeConfiguration examineModeConfiguration;
    private final PlayerSettingsRepository playerSettingsRepository;

    public NotesExamineGuiProvider(Messages messages, Options options, PlayerSettingsRepository playerSettingsRepository) {
        this.messages = messages;

        this.playerSettingsRepository = playerSettingsRepository;
        examineModeConfiguration = options.staffItemsConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return notesItem(playerSettingsRepository.get(player.getPlayer()));
    }

    @Override
    public String getClickAction(Player staff, SppPlayer targetPlayer, String backAction) {
        return GuiActionBuilder.builder()
            .action("manage-notes/create")
            .param("targetPlayerName", targetPlayer.getUsername())
            .build();
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineModeConfiguration.getModeExamineNotes() >= 0 && player.isOnline();
    }

    @Override
    public int getSlot() {
        return examineModeConfiguration.getModeExamineNotes() - 1;
    }

    private ItemStack notesItem(PlayerSettings playerSettings) {
        List<String> notes = playerSettings.getPlayerNotes().isEmpty() ? Arrays.asList("&7No notes found") : playerSettings.getPlayerNotes();

        ItemStack item = Items.builder()
            .setMaterial(Material.MAP).setAmount(1)
            .setName(messages.examineNotes)
            .setLore(notes)
            .build();

        return item;
    }
}
