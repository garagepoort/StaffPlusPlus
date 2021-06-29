package net.shortninja.staffplus.core.domain.staff.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.gui.IAction;

import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGui;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplus.core.domain.staff.examine.gui.SeverityLevelSelectGui;
import net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class WarnExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;

    private final ExamineModeConfiguration examineModeConfiguration;
    private final Options options;
    private final SessionManagerImpl sessionManager;
    private final PlayerManager playerManager;

    public WarnExamineGuiProvider(Messages messages, Options options, SessionManagerImpl sessionManager, PlayerManager playerManager) {
        this.messages = messages;

        this.options = options;
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
        examineModeConfiguration = this.options.staffItemsConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return warnItem();
    }

    @Override
    public IAction getClickAction(ExamineGui examineGui, Player staff, SppPlayer targetPlayer) {
        IAction severityAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                new SeverityLevelSelectGui("Select severity level", targetPlayer, () -> new ExamineGui(player, targetPlayer, examineGui.getTitle())).show(player);
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };

        IAction warnAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                PlayerSession playerSession = sessionManager.get(staff.getUniqueId());

                messages.send(staff, messages.typeInput, messages.prefixGeneral);

                playerSession.setChatAction((player1, input) -> {
                    Optional<SppPlayer> onOrOfflinePlayer = playerManager.getOnOrOfflinePlayer(targetPlayer.getId());
                    if (!onOrOfflinePlayer.isPresent()) {
                        messages.send(player1, messages.playerOffline, messages.prefixGeneral);
                    } else {
                        StaffPlus.get().getIocContainer().get(WarnService.class).sendWarning(player1, onOrOfflinePlayer.get(), input);
                        messages.send(player1, messages.inputAccepted, messages.prefixGeneral);
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
