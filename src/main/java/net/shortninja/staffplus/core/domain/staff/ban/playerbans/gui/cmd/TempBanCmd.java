package net.shortninja.staffplus.core.domain.staff.ban.playerbans.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.exceptions.NoPermissionException;
import net.shortninja.staffplus.core.common.time.TimeUnit;

import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.config.BanConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.config.BanReasonConfiguration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanType.TEMP_BAN;

@IocBean(conditionalOnProperty = "ban-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class TempBanCmd extends AbstractCmd {

    private static final String TEMPLATE_FILE = "-template=";

    private final PermissionHandler permissionHandler;
    private final BanConfiguration banConfiguration;
    private final BanService banService;
    private final PlayerManager playerManager;

    public TempBanCmd(PermissionHandler permissionHandler, Messages messages, BanConfiguration banConfiguration, Options options, BanService banService, CommandService commandService, PlayerManager playerManager) {
        super(banConfiguration.getCommandTempBanPlayer(), messages, options, commandService);
        this.permissionHandler = permissionHandler;
        this.banConfiguration = banConfiguration;
        this.banService = banService;
        this.playerManager = playerManager;
        setDescription("Temporary ban a player");
        setUsage("[player] [amount] [unit] [reason]");
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        boolean isSilent = Arrays.stream(args).anyMatch(a -> a.equalsIgnoreCase("-silent"));
        if (isSilent && !permissionHandler.has(sender, banConfiguration.getPermissionBanSilent())) {
            throw new NoPermissionException("You don't have the permission to execute a silent ban");
        }
        args = Arrays.stream(args).filter(a -> !a.equalsIgnoreCase("-silent")).toArray(String[]::new);

        if (!JavaUtils.isInteger(args[1])) {
            throw new BusinessException(messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()));
        }

        int amount = Integer.parseInt(args[1]);
        String timeUnit = args[2];

        if (args[3].toLowerCase().startsWith(TEMPLATE_FILE.toLowerCase())) {
            String template = getTemplateName(args[3]);
            String reason = JavaUtils.compileWords(args, 4);
            banService.tempBan(sender, player, TimeUnit.getDuration(timeUnit, amount), reason, template, isSilent);
            return true;
        }

        String reason = JavaUtils.compileWords(args, 3);
        banService.tempBan(sender, player, TimeUnit.getDuration(timeUnit, amount), reason, isSilent);
        return true;
    }

    private String getTemplateName(String arg) {
        String[] templateParams = arg.split("=");
        if (templateParams.length != 2) {
            throw new BusinessException("&CInvalid template provided");
        }
        return templateParams[1];
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (args.length >= 4 && args[3].toLowerCase().contains(TEMPLATE_FILE.toLowerCase())) {
            return 5;
        }
        return 4;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.BOTH;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }

    @Override
    protected boolean canBypass(Player player) {
        return permissionHandler.has(player, banConfiguration.getPermissionBanByPass());
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        args = Arrays.stream(args).filter(a -> !a.equalsIgnoreCase("-silent")).toArray(String[]::new);

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
            return Stream.of(
                TimeUnit.YEAR.name(), TimeUnit.MONTH.name(),
                TimeUnit.WEEK.name(), TimeUnit.DAY.name(),
                TimeUnit.HOUR.name(), TimeUnit.MINUTE.name())
                .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                .collect(Collectors.toList());
        }
        if (args.length == 4 && currentArg.startsWith("-")) {
            return getTemplateCompletion();
        }

        if (args.length >= 4 && !banConfiguration.getBanReasons(TEMP_BAN).isEmpty()) {
            return getBanReasonCompletion(currentArg);
        }

        return Collections.emptyList();
    }

    private List<String> getTemplateCompletion() {
        return banConfiguration.getTemplates().keySet().stream()
            .map(k -> TEMPLATE_FILE + k)
            .collect(Collectors.toList());
    }

    private List<String> getBanReasonCompletion(String currentArg) {
        return banConfiguration.getBanReasons(TEMP_BAN).stream()
            .map(BanReasonConfiguration::getName)
            .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
            .collect(Collectors.toList());
    }
}
