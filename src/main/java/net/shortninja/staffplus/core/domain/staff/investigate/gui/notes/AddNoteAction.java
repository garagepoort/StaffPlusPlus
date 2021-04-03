package net.shortninja.staffplus.core.domain.staff.investigate.gui.notes;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.staff.investigate.InvestigationNoteService;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class AddNoteAction implements IAction {
    private static final String CANCEL = "cancel";
    private final Messages messages = StaffPlus.get().getIocContainer().get(Messages.class);

    private final SessionManagerImpl sessionManager = StaffPlus.get().getIocContainer().get(SessionManagerImpl.class);
    private final InvestigationNoteService investigationNoteService = StaffPlus.get().getIocContainer().get(InvestigationNoteService.class);

    private final Investigation investigation;

    public AddNoteAction(Investigation investigation) {
        this.investigation = investigation;
    }

    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        messages.send(player, "&1===================================================", messages.prefixInvestigations);
        messages.send(player, "&6Type your note in chat", messages.prefixInvestigations);
        messages.send(player, "&6      Type \"cancel\" to cancel adding a note ", messages.prefixInvestigations);
        messages.send(player, "&1===================================================", messages.prefixInvestigations);
        PlayerSession playerSession = sessionManager.get(player.getUniqueId());
        playerSession.setChatAction((player1, message) -> {
            if (message.equalsIgnoreCase(CANCEL)) {
                messages.send(player, "&CYou have cancelled your note", messages.prefixInvestigations);
                return;
            }
            investigationNoteService.addNote(player, investigation, message);
        });
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
