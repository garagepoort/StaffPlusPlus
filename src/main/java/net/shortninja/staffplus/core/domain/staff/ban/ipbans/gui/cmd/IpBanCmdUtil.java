package net.shortninja.staffplus.core.domain.staff.ban.ipbans.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Messages;
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


    public void sendBanChoiceMessage(Player sender, String ipAddress, ChoiceAction confirmAction) {
        List<PlayerIpRecord> players = ipAddress.contains("/") ? playerIpService.getMatchedBySubnet(ipAddress) : playerIpService.getMatchedByIp(ipAddress);

        messages.send(sender, "&6Following players are matching the current IP rule you will add. They will all be banned", messages.prefixBans);
        messages.send(sender, messages.LONG_LINE, messages.prefixBans);
        for (int i = 0; i < players.size(); i++) {
            PlayerIpRecord player = players.get(i);
            messages.send(sender, "&c" + (i + 1) + ". &7" + player.getPlayerName(), messages.prefixBans);
        }
        confirmationService.showConfirmation(sender,
            new ConfirmationConfig(ConfirmationType.CHAT, "Are you sure you want to ban this ip?"),
            new HashMap<>(),
            confirmAction,
            player -> messages.send(sender, "&7You have cancelled banning this ip", messages.prefixBans));
    }

    public void sendUnbanChoiceMessage(Player sender, List<IpBan> matchingIpBans, ChoiceAction confirmAction) {
        List<PlayerIpRecord> players = matchingIpBans.stream()
            .flatMap(ipBan -> ipBan.isSubnet() ? playerIpService.getMatchedBySubnet(ipBan.getIp()).stream() : playerIpService.getMatchedByIp(ipBan.getIp()).stream())
            .collect(Collectors.toList());

        messages.send(sender, "&6Following players are matching the given IP rule.", messages.prefixBans);
        messages.send(sender, messages.LONG_LINE, messages.prefixBans);
        for (int i = 0; i < players.size(); i++) {
            PlayerIpRecord player = players.get(i);
            messages.send(sender, "&c" + (i + 1) + ". &7" + player.getPlayerName(), messages.prefixBans);
        }

        confirmationService.showConfirmation(sender,
            new ConfirmationConfig(ConfirmationType.CHAT, "Are you sure you want to unban this rule?"),
            new HashMap<>(),
            confirmAction,
            player -> messages.send(sender, "&6You have cancelled unbanning this ip", messages.prefixBans));
    }

    public String retrieveTemplate(CommandSender sender, Map<String, String> args) {
        if(args.containsKey(TEMPLATE)) {
            permission.validate(sender, banConfiguration.permissionBanTemplateOverwrite);
            if(args.get(TEMPLATE) == null) {
                throw new BusinessException("&CInvalid template provided");
            }
            String templateName = args.get(TEMPLATE);
            banConfiguration.getTemplate(templateName).orElseThrow(() -> new BusinessException("&CCannot find ban template with name [" + templateName + "]"));
            return templateName;
        }
        return null;
    }

}
