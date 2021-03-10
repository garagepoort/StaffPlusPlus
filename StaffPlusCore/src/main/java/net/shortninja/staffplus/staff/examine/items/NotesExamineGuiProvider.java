package net.shortninja.staffplus.staff.examine.items;

import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplus.staff.examine.ExamineGui;
import net.shortninja.staffplus.staff.examine.ExamineGuiItemProvider;
import net.shortninja.staffplus.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplus.common.IAction;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.common.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class NotesExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final MessageCoordinator message;
    private final ExamineModeConfiguration examineModeConfiguration;
    private final Options options;
    private final SessionManagerImpl sessionManager;

    public NotesExamineGuiProvider(Messages messages, MessageCoordinator message, Options options, SessionManagerImpl sessionManager) {
        this.messages = messages;
        this.message = message;
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

                message.send(staff, messages.typeInput, messages.prefixGeneral);

                playerSession.setChatAction((player12, input) -> {
                    sessionManager.get(targetPlayer.getId()).addPlayerNote("&7" + input);
                    message.send(player12, messages.inputAccepted, messages.prefixGeneral);
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
