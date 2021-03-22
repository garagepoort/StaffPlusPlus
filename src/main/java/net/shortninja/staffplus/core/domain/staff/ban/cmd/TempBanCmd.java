package net.shortninja.staffplus.core.domain.staff.ban.cmd;

import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.time.TimeUnit;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.ban.BanService;
import net.shortninja.staffplus.core.domain.staff.ban.config.BanReasonConfiguration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.shortninja.staffplus.core.domain.staff.ban.BanType.TEMP_BAN;

public class TempBanCmd extends AbstractCmd {

    private static final String TEMPLATE_FILE = "-template=";
    private final BanService banService = IocContainer.get(BanService.class);
    private PermissionHandler permissionHandler;
    private Options options;

    public TempBanCmd(String name) {
        super(name, IocContainer.get(Options.class).banConfiguration.getPermissionBanPlayer());
        permissionHandler = IocContainer.get(PermissionHandler.class);
        options = IocContainer.get(Options.class);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (!JavaUtils.isInteger(args[1])) {
            throw new BusinessException(messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()));
        }

        int amount = Integer.parseInt(args[1]);
        String timeUnit = args[2];

        if (args[3].toLowerCase().startsWith(TEMPLATE_FILE.toLowerCase())) {
            String template = getTemplateName(args[3]);
            String reason = JavaUtils.compileWords(args, 4);
            banService.tempBan(sender, player, TimeUnit.getDuration(timeUnit, amount), reason, template);
            return true;
        }

        String reason = JavaUtils.compileWords(args, 3);
        banService.tempBan(sender, player, TimeUnit.getDuration(timeUnit, amount), reason);
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
        return permissionHandler.has(player, options.banConfiguration.getPermissionBanByPass());
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
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
            return options.banConfiguration.getTemplates().keySet().stream()
                .map(k -> TEMPLATE_FILE + k)
                .collect(Collectors.toList());
        }

        if (args.length == 4) {
            if (currentArg.startsWith("-")) {
                return getTemplateCompletion();
            } else if (!options.banConfiguration.getBanReasons(TEMP_BAN).isEmpty()) {
                return getBanReasonCompletion(currentArg);
            }
        }

        if (args.length == 5 && !options.banConfiguration.getBanReasons(TEMP_BAN).isEmpty()) {
            return getBanReasonCompletion(currentArg);
        }

        return Collections.emptyList();
    }

    private List<String> getTemplateCompletion() {
        return options.banConfiguration.getTemplates().keySet().stream()
            .map(k -> TEMPLATE_FILE + k)
            .collect(Collectors.toList());
    }

    private List<String> getBanReasonCompletion(String currentArg) {
        return options.banConfiguration.getBanReasons(TEMP_BAN).stream()
            .map(BanReasonConfiguration::getName)
            .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
            .collect(Collectors.toList());
    }
}
