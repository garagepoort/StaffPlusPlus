package net.shortninja.staffplus.core.domain.staff.ban.ipbans.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.confirmation.ChoiceAction;
import net.shortninja.staffplus.core.domain.confirmation.ConfirmationConfig;
import net.shortninja.staffplus.core.domain.confirmation.ConfirmationService;
import net.shortninja.staffplus.core.domain.player.ip.PlayerIpRecord;
import net.shortninja.staffplus.core.domain.player.ip.PlayerIpService;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBan;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBanConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.item.ConfirmationType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@IocBean
public class IpBanCmdUtil {

    private static final String TEMPLATE = "-template";

    private final PlayerIpService playerIpService;
    private final Messages messages;
    private final ConfirmationService confirmationService;
    private final PermissionHandler permission;
    private final IpBanConfiguration banConfiguration;

    public IpBanCmdUtil(PlayerIpService playerIpService, Messages messages, ConfirmationService confirmationService, PermissionHandler permission, IpBanConfiguration banConfiguration) {
        this.playerIpService = playerIpService;
        this.messages = messages;
        this.confirmationService = confirmationService;
        this.permission = permission;
        this.banConfiguration = banConfiguration;
    }


    public void  sendBanChoiceMessage(Player sender, String ipAddress, ChoiceAction confirmAction) {
        List<PlayerIpRecord> players = playerIpService.getPlayersMatchingIp(ipAddress);

        messages.sendTranslation(sender, "ipbans.ban-confirmation-matching-players", messages.prefixBans);
        printPlayerList(sender, players);
        confirmationService.showConfirmation(sender,
            new ConfirmationConfig(ConfirmationType.CHAT, messages.get("ipbans.ban-confirmation-question")),
            new HashMap<>(),
            confirmAction,
            player -> messages.sendTranslation(sender, "ipbans.ban-cancelled", messages.prefixBans));
    }

    public void sendUnbanChoiceMessage(Player sender, List<IpBan> matchingIpBans, ChoiceAction confirmAction) {
        List<PlayerIpRecord> players = matchingIpBans.stream()
            .flatMap(ipBan -> ipBan.isSubnet() ? playerIpService.getMatchedBySubnet(ipBan.getIp()).stream() : playerIpService.getMatchedByIp(ipBan.getIp()).stream())
            .collect(Collectors.toList());

        messages.sendTranslation(sender, "ipbans.unban-confirmation-matching-players", messages.prefixBans);
        printPlayerList(sender, players);

        confirmationService.showConfirmation(sender,
            new ConfirmationConfig(ConfirmationType.CHAT, messages.get("ipbans.unban-confirmation-question")),
            new HashMap<>(),
            confirmAction,
            player -> messages.sendTranslation(sender, "ipbans.unban-cancelled", messages.prefixBans));
    }

    private void printPlayerList(Player sender, List<PlayerIpRecord> players) {
        messages.send(sender, messages.LONG_LINE, messages.prefixBans);
        for (int i = 0; i < players.size(); i++) {
            PlayerIpRecord player = players.get(i);
            messages.sendTranslation(sender, "ipbans.list-entry", messages.prefixBans, "%count%", Integer.toString(i + 1), "%value%", player.getPlayerName());
        }
    }

    public String retrieveTemplate(CommandSender sender, Map<String, String> args) {
        if(args.containsKey(TEMPLATE)) {
            permission.validate(sender, banConfiguration.permissionBanTemplateOverwrite);
            if(args.get(TEMPLATE) == null) {
                throw new BusinessException(messages.get("ipbans.error-invalid-template"));
            }
            String templateName = args.get(TEMPLATE);
            banConfiguration.getTemplate(templateName)
                .orElseThrow(() -> new BusinessException(messages.get("ipbans.error-template-not-found", "%template%", templateName)));
            return templateName;
        }
        return null;
    }

}
