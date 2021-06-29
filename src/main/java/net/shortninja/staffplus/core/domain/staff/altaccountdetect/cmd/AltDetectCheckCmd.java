package net.shortninja.staffplus.core.domain.staff.altaccountdetect.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.AltDetectResult;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.AltDetectionService;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.config.AltDetectConfiguration;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.database.ipcheck.PlayerIpRepository;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@IocBean(conditionalOnProperty = "alt-detect-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class AltDetectCheckCmd extends AbstractCmd {

    private final AltDetectionService altDetectionService;
    private final PlayerManager playerManager;
    private final PlayerIpRepository playerIpRepository;

    public AltDetectCheckCmd(Messages messages, Options options, AltDetectConfiguration altDetectConfiguration, AltDetectionService altDetectionService, CommandService commandService, PlayerManager playerManager, PlayerIpRepository playerIpRepository) {
        super(altDetectConfiguration.commandCheck, messages, options, commandService);
        this.altDetectionService = altDetectionService;
        this.playerManager = playerManager;
        this.playerIpRepository = playerIpRepository;
        setPermission(altDetectConfiguration.checkPermission);
        setDescription("Run an alt detection check on a certain player. Will print all results");
        setUsage("[player]");
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            messages.send(sender, "&fChecking alt accounts for player: &6%player%".replace("%player%", player.getUsername()), messages.prefixGeneral);
            messages.send(sender, "&fLast know IP: &6%ip%".replace("%ip%", playerIpRepository.getLastIp(player.getId()).orElse("Unknown")), messages.prefixGeneral);
            messages.send(sender, messages.LONG_LINE, messages.prefixGeneral);

            List<AltDetectResult> altAccounts = altDetectionService.getAltAccounts(player).stream()
                .sorted((a, b) -> a.getAltDetectTrustLevel().getScore() > b.getAltDetectTrustLevel().getScore() ? -1 : 1)
                .collect(Collectors.toList());
            for (AltDetectResult altAccount : altAccounts) {
                String results = altAccount.getAltDetectResultTypes().stream().map(Enum::name).collect(Collectors.joining(" - "));
                messages.send(sender, buildResultLine(altAccount, results), messages.prefixGeneral);
            }
        });
        return true;
    }

    @NotNull
    private String buildResultLine(AltDetectResult altAccount, String results) {
        return "&fMatch: &b%playerMatched% &f| &b%trustlevel% &f| %ipmatched%"
            .replace("%playerMatched%", altAccount.getPlayerMatchedName())
            .replace("%trustlevel%", altAccount.getAltDetectTrustLevel().name())
            .replace("%ipmatched%", altAccount.isIpMatched() ? "&2Ip match" : "&cNo Ip Match");
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.BOTH;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.of(args[0]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        return super.tabComplete(sender, alias, args);
    }
}
