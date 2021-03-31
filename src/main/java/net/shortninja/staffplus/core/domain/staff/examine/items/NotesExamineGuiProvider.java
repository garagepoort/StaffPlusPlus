package net.shortninja.staffplus.core.domain.staff.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.gui.IAction;

import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGui;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
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
    private final Options options;
    private final SessionManagerImpl sessionManager;

    public NotesExamineGuiProvider(Messages messages, Options options, SessionManagerImpl sessionManager) {
        this.messages = messages;

        this.options = options;
        this.sessionManager = sessionManager;
        examineModeConfiguration = this.options.modeConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return notesItem(sessionManager.get(player.getId()));
    }

    @Override
    public IAction getClickAction(ExamineGui examineGui, Player staff, SppPlayer targetPlayer) {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                PlayerSession playerSession = sessionManager.get(staff.getUniqueId());

                messages.send(staff, messages.typeInput, messages.prefixGeneral);

                playerSession.setChatAction((player12, input) -> {
                    sessionManager.get(targetPlayer.getId()).addPlayerNote("&7" + input);
                    messages.send(player12, messages.inputAccepted, messages.prefixGeneral);
                });
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineModeConfiguration.getModeExamineNotes() >= 0 && player.isOnline();
    }

    @Override
    public int getSlot() {
        return examineModeConfiguration.getModeExamineNotes() - 1;
    }

    private ItemStack notesItem(PlayerSession playerSession) {
        List<String> notes = playerSession.getPlayerNotes().isEmpty() ? Arrays.asList("&7No notes found") : playerSession.getPlayerNotes();

        ItemStack item = Items.builder()
            .setMaterial(Material.MAP).setAmount(1)
            .setName(messages.examineNotes)
            .setLore(notes)
            .build();

        return item;
    }
}
