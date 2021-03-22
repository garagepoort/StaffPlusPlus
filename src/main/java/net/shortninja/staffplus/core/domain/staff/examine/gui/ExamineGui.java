package net.shortninja.staffplus.core.domain.staff.examine.gui;

import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.gui.AbstractGui;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class ExamineGui extends AbstractGui {

    private static final int SIZE = 54;
    private final List<ExamineGuiItemProvider> guiItemProviders = IocContainer.getList(ExamineGuiItemProvider.class);
    private final SppPlayer targetPlayer;
    private final Player player;

    public ExamineGui(Player player, SppPlayer targetPlayer, String title) {
        super(SIZE, title);
        this.player = player;
        this.targetPlayer = targetPlayer;
    }

    public SppPlayer getTargetPlayer() {
        return targetPlayer;
    }

    @Override
    public void buildGui() {
        for (ExamineGuiItemProvider guiItemProvider : guiItemProviders) {
            if (guiItemProvider.enabled(player, targetPlayer)) {
                setItem(guiItemProvider.getSlot(), guiItemProvider.getItem(targetPlayer), guiItemProvider.getClickAction(this, player, targetPlayer));
            }
        }
    }
}