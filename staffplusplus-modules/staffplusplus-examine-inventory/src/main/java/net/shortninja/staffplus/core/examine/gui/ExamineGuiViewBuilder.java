package net.shortninja.staffplus.core.examine.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import be.garagepoort.mcioc.tubinggui.model.TubingGui;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.examine.config.ExamineConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import java.util.List;

@IocBean
public class ExamineGuiViewBuilder {

    private static final int SIZE = 54;
    private final List<ExamineGuiItemProvider> guiItemProviders;
    private final Messages messages;
    private final ExamineConfiguration examineConfiguration;

    public ExamineGuiViewBuilder(@IocMulti(ExamineGuiItemProvider.class) List<ExamineGuiItemProvider> guiItemProviders, Messages messages, ExamineConfiguration examineConfiguration) {
        this.guiItemProviders = guiItemProviders;
        this.messages = messages;
        this.examineConfiguration = examineConfiguration;
    }

    public TubingGui buildGui(Player player, SppPlayer targetPlayer) {
        TubingGui.Builder builder = new TubingGui.Builder(messages.colorize(examineConfiguration.examineGuiTitle), SIZE);
        for (ExamineGuiItemProvider guiItemProvider : guiItemProviders) {
            if (guiItemProvider.enabled(player, targetPlayer)) {
                builder.addItem(guiItemProvider.getClickAction(player, targetPlayer), guiItemProvider.getSlot(), guiItemProvider.getItem(player, targetPlayer));
            }
        }
        return builder.build();
    }
}