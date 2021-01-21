package net.shortninja.staffplus.staff.examine;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.entity.Player;

import java.util.List;

public class ExamineGui extends AbstractGui {

    private static final int SIZE = 54;
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final List<ExamineGuiItemProvider> guiItemProviders = IocContainer.getExamineGuiItemProviders();
    private final SppPlayer targetPlayer;
    private final Player player;

    public ExamineGui(Player player, SppPlayer targetPlayer, String title) {
        super(SIZE, title);
        this.player = player;
        this.targetPlayer = targetPlayer;

        initiate();

        player.closeInventory();
        player.openInventory(getInventory());
        sessionManager.get(player.getUniqueId()).setCurrentGui(this);
    }

    public void initiate() {
        for (ExamineGuiItemProvider guiItemProvider : guiItemProviders) {
            if(guiItemProvider.enabled(player, targetPlayer)) {
                setItem(guiItemProvider.getSlot(), guiItemProvider.getItem(targetPlayer), guiItemProvider.getClickAction(this, player, targetPlayer));
            }
        }
        for (int i = 0; i < SIZE; i++) {
            if(getInventory().getItem(i) == null) {
                setItem(i, Items.createGrayColoredGlass("No action", ""), null);
            }
        }
    }

    public SppPlayer getTargetPlayer() {
        return targetPlayer;
    }


}