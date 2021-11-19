package net.shortninja.staffplus.core.domain.staff.examine.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.exceptions.PlayerOfflineException;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningSeverityConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class WarnPlayerAction implements IAction {
    private static final String CANCEL = "cancel";
    private static final String NONE = "none";
    private final Messages messages = StaffPlus.get().getIocContainer().get(Messages.class);

    private final OnlineSessionsManager sessionManager = StaffPlus.get().getIocContainer().get(OnlineSessionsManager.class);
    private final WarnService warnService = StaffPlus.get().getIocContainer().get(WarnService.class);
    private final PlayerManager playerManager = StaffPlus.get().getIocContainer().get(PlayerManager.class);
    private final WarningConfiguration warningConfiguration = StaffPlus.get().getIocContainer().get(WarningConfiguration.class);

    private final SppPlayer targetPlayer;

    public WarnPlayerAction(SppPlayer targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        String severityLevel = StaffPlus.get().getIocContainer().get(IProtocolService.class).getVersionProtocol().getNbtString(item);
        OnlinePlayerSession playerSession = sessionManager.get(player);

        WarningSeverityConfiguration severityConfiguration = warningConfiguration.getSeverityConfiguration(severityLevel)
            .orElseThrow(() -> new BusinessException("&CNo severity configuration found for level [" + severityLevel + "]"));

        SppPlayer onOrOfflinePlayer = playerManager.getOnOrOfflinePlayer(targetPlayer.getId()).orElseThrow(PlayerOfflineException::new);

        if (!severityConfiguration.isReasonSettable()) {
            warnService.sendWarning(player, onOrOfflinePlayer, null, severityConfiguration);
        } else {
            promptReasonInput(player, playerSession, onOrOfflinePlayer, severityConfiguration);
        }

    }

    private void promptReasonInput(Player player, OnlinePlayerSession playerSession, SppPlayer onOrOfflinePlayer, WarningSeverityConfiguration severityConfiguration) {
        boolean defaultReasonSettable = severityConfiguration.getReason().isPresent() && severityConfiguration.isReasonOverwriteEnabled();

        messages.send(player, "&1=====================================================", messages.prefixGeneral);
        messages.send(player, "&6         You have chosen to warn this player", messages.prefixGeneral);
        messages.send(player, "&6Type your reason for warning this player in chat", messages.prefixGeneral);
        if (defaultReasonSettable) {
            messages.send(player, "&6Type \"none\" to use the default reason", messages.prefixGeneral);
        }
        messages.send(player, "&6        Type \"cancel\" to cancel the warning ", messages.prefixGeneral);
        messages.send(player, "&1=====================================================", messages.prefixGeneral);


        playerSession.setChatAction((player1, input) -> {
            if (input.equalsIgnoreCase(CANCEL)) {
                messages.send(player, "&CYou have cancelled warning this player", messages.prefixWarnings);
                return;
            }
            if (input.equalsIgnoreCase(NONE) && defaultReasonSettable) {
                input = null;
            }

            warnService.sendWarning(player1, onOrOfflinePlayer, input, severityConfiguration);
            messages.send(player1, messages.inputAccepted, messages.prefixGeneral);
        });
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
