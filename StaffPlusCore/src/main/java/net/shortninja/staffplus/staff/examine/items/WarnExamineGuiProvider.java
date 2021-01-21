package net.shortninja.staffplus.staff.examine.items;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.examine.ExamineGui;
import net.shortninja.staffplus.staff.examine.ExamineGuiItemProvider;
import net.shortninja.staffplus.staff.examine.SeverityLevelSelectGui;
import net.shortninja.staffplus.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class WarnExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final MessageCoordinator message;
    private final ExamineModeConfiguration examineModeConfiguration;
    private final Options options;
    private final SessionManager sessionManager;
    private final PlayerManager playerManager;

    public WarnExamineGuiProvider(Messages messages, MessageCoordinator message, Options options, SessionManager sessionManager, PlayerManager playerManager) {
        this.messages = messages;
        this.message = message;
        this.options = options;
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
        examineModeConfiguration = this.options.modeConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return warnItem();
    }

    @Override
    public IAction getClickAction(ExamineGui examineGui, Player staff, SppPlayer targetPlayer) {
        IAction severityAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                new SeverityLevelSelectGui(player, "Select severity level", targetPlayer, () -> new ExamineGui(player, targetPlayer, examineGui.getTitle()));
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };

        IAction warnAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                PlayerSession playerSession = sessionManager.get(targetPlayer.getPlayer().getUniqueId());

                message.send(targetPlayer.getPlayer(), messages.typeInput, messages.prefixGeneral);

                playerSession.setChatAction((player1, input) -> {
                    Optional<SppPlayer> onOrOfflinePlayer = playerManager.getOnOrOfflinePlayer(targetPlayer.getId());
                    if (!onOrOfflinePlayer.isPresent()) {
                        message.send(player1, messages.playerOffline, messages.prefixGeneral);
                    } else {
                        IocContainer.getWarnService().sendWarning(player1, onOrOfflinePlayer.get(), input);
                        message.send(player1, messages.inputAccepted, messages.prefixGeneral);
                    }
                });
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };

        return options.warningConfiguration.getSeverityLevels().isEmpty() ? warnAction : severityAction;
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        return examineModeConfiguration.getModeExamineWarn() >= 0;
    }

    @Override
    public int getSlot() {
        return examineModeConfiguration.getModeExamineWarn() - 1;
    }

    private ItemStack warnItem() {
        ItemStack item = Items.builder()
            .setMaterial(Material.PAPER).setAmount(1)
            .setName("&bWarn player")
            .addLore(messages.examineWarn)
            .build();

        return item;
    }
}
