package net.shortninja.staffplus.staff.examine;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.common.exceptions.PlayerOfflineException;
import net.shortninja.staffplus.common.gui.IAction;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplus.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.staff.warn.warnings.config.WarningSeverityConfiguration;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WarnPlayerAction implements IAction {
    private static final String CANCEL = "cancel";
    private static final String NONE = "none";
    private final Messages messages = IocContainer.getMessages();
    private final MessageCoordinator messageCoordinator = IocContainer.getMessage();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();
    private final WarnService warnService = IocContainer.getWarnService();
    private final PlayerManager playerManager = IocContainer.getPlayerManager();
    private final Options options = IocContainer.getOptions();

    private final SppPlayer targetPlayer;

    public WarnPlayerAction(SppPlayer targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void click(Player player, ItemStack item, int slot) {
        String severityLevel = StaffPlus.get().versionProtocol.getNbtString(item);
        PlayerSession playerSession = sessionManager.get(player.getUniqueId());

        WarningSeverityConfiguration severityConfiguration = options.warningConfiguration.getSeverityConfiguration(severityLevel)
            .orElseThrow(() -> new BusinessException("&CNo severity configuration found for level [" + severityLevel + "]"));

        SppPlayer onOrOfflinePlayer = playerManager.getOnOrOfflinePlayer(targetPlayer.getId()).orElseThrow(PlayerOfflineException::new);

        if (!severityConfiguration.isReasonSettable()) {
            warnService.sendWarning(player, onOrOfflinePlayer, null, severityConfiguration);
        } else {
            promptReasonInput(player, playerSession, onOrOfflinePlayer, severityConfiguration);
        }

    }

    private void promptReasonInput(Player player, PlayerSession playerSession, SppPlayer onOrOfflinePlayer, WarningSeverityConfiguration severityConfiguration) {
        boolean defaultReasonSettable = severityConfiguration.getReason().isPresent() && severityConfiguration.isReasonOverwriteEnabled();

        messageCoordinator.send(player, "&1=====================================================", messages.prefixGeneral);
        messageCoordinator.send(player, "&6         You have chosen to warn this player", messages.prefixGeneral);
        messageCoordinator.send(player, "&6Type your reason for warning this player in chat", messages.prefixGeneral);
        if (defaultReasonSettable) {
            messageCoordinator.send(player, "&6Type \"none\" to use the default reason", messages.prefixGeneral);
        }
        messageCoordinator.send(player, "&6        Type \"cancel\" to cancel the warning ", messages.prefixGeneral);
        messageCoordinator.send(player, "&1=====================================================", messages.prefixGeneral);


        playerSession.setChatAction((player1, input) -> {
            if (input.equalsIgnoreCase(CANCEL)) {
                messageCoordinator.send(player, "&CYou have cancelled warning this player", messages.prefixWarnings);
                return;
            }
            if (input.equalsIgnoreCase(NONE) && defaultReasonSettable) {
                input = null;
            }

            warnService.sendWarning(player1, onOrOfflinePlayer, input, severityConfiguration);
            messageCoordinator.send(player1, messages.inputAccepted, messages.prefixGeneral);
        });
    }

    @Override
    public boolean shouldClose(Player player) {
        return true;
    }
}
