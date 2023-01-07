package net.shortninja.staffplus.core.domain.staff.ban.ipbans.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubinggui.GuiActionBuilder;
import be.garagepoort.mcioc.tubinggui.GuiActionService;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.time.TimeUnit;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.ip.database.PlayerIpRepository;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBanService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.shortninja.staffplus.core.common.JavaUtils.isValidCidrOrIp;
import static net.shortninja.staffplus.core.common.time.TimeUnit.getDuration;
import static net.shortninja.staffplus.core.common.utils.BukkitUtils.getIpFromPlayer;

@Command(
    command = "commands:ipban.tempban",
    permissions = "permissions:ipban.tempban",
    description = "Temporary ban an ip-address",
    usage = "[player/ip-address]"
)
@IocBean(conditionalOnProperty = "ban-module.enabled=true && ban-module.ipban.enabled=true")
@IocMultiProvider(SppCommand.class)
public class IpTempBanCmd extends AbstractCmd {

    @ConfigProperty("ban-module.ipban.confirmation")
    private String confirmationType;

    private final IpBanService banService;
    private final PlayerManager playerManager;
    private final PlayerIpRepository playerIpRepository;
    private final IpBanCmdUtil ipBanCmdUtil;
    private final GuiActionService guiActionService;

    public IpTempBanCmd(Messages messages,
                        IpBanService banService,
                        CommandService commandService,
                        PlayerManager playerManager,
                        PlayerIpRepository playerIpRepository,
                        IpBanCmdUtil ipBanCmdUtil,
                        PermissionHandler permissionHandler, GuiActionService guiActionService) {
        super(messages, permissionHandler, commandService);
        this.banService = banService;
        this.playerManager = playerManager;
        this.playerIpRepository = playerIpRepository;
        this.ipBanCmdUtil = ipBanCmdUtil;
        this.guiActionService = guiActionService;
    }


    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        String ipArg = args[0];
        if (!isValidCidrOrIp(ipArg)) {
            SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(ipArg).orElseThrow(() -> new BusinessException("No player found or Ip address given is invalid"));
            ipArg = getIpAddress(sppPlayer);
        }

        int amount = Integer.parseInt(args[1]);
        String timeUnit = args[2];
        String ipAddress = ipArg;
        String templateName = ipBanCmdUtil.retrieveTemplate(sender, optionalParameters);

        executeBan(sender, ipAddress, templateName, getDuration(timeUnit, amount), optionalParameters.containsKey("-silent"));
        return true;
    }

    public void executeBan(CommandSender sender, String ipAddress, String templateName, long duration, boolean silent) {
        if (!(sender instanceof Player) || confirmationType.equalsIgnoreCase("DISABLED")) {
            banService.tempBanIp(sender, ipAddress, templateName, duration, silent);
        } else {
            if (confirmationType.equalsIgnoreCase("CHAT")) {
                ipBanCmdUtil.sendBanChoiceMessage((Player) sender, ipAddress, p -> banService.tempBanIp(sender, ipAddress, templateName, duration, silent));
            } else if (confirmationType.equalsIgnoreCase("GUI")) {
                guiActionService.executeAction((Player) sender, GuiActionBuilder.builder()
                    .action("ipbans/temp-ban/confirm")
                    .param("ipAddress", ipAddress)
                    .param("templateName", templateName)
                    .param("duration", String.valueOf(duration))
                    .param("silent", String.valueOf(silent))
                    .build());
            }
        }
    }

    @Override
    protected List<String> getOptionalParameters() {
        return Arrays.asList("-silent", "-template");
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 3;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

    @Override
    public List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {
        String currentArg = args.length > 0 ? args[args.length - 1] : "";

        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                .collect(Collectors.toList());
        }

        if (args.length == 2) {
            return Stream.of("5", "10", "15", "20")
                .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                .collect(Collectors.toList());
        }

        if (args.length == 3) {
            return Arrays.stream(TimeUnit.values())
                .map(Enum::name)
                .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private String getIpAddress(SppPlayer sppPlayer) {
        return sppPlayer.isOnline() ? getIpFromPlayer(sppPlayer.getPlayer()) : playerIpRepository.getLastIp(sppPlayer.getId()).orElseThrow(() -> new BusinessException("This player's Ip address is unknown."));
    }

}
