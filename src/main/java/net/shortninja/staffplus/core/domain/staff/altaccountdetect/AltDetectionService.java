package net.shortninja.staffplus.core.domain.staff.altaccountdetect;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.exceptions.NoPermissionException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.ip.database.PlayerIpRepository;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.checks.AltDetectInfo;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.checks.AltDetector;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.checks.IpDetector;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.checks.UsernameDetector;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.config.AltDetectConfiguration;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.database.AltDetectWhitelistRepository;
import net.shortninja.staffplusplus.altdetect.AltDetectEvent;
import net.shortninja.staffplusplus.altdetect.AltDetectResultType;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static net.shortninja.staffplusplus.altdetect.AltDetectResultType.SAME_IP;

@IocBean
public class AltDetectionService {

    private final PlayerManager playerManager;
    private final List<AltDetector> altDetectors;
    private final PlayerIpRepository playerIpRepository;
    private final AltDetectWhitelistRepository altDetectWhitelistRepository;
    private final PermissionHandler permission;
    private final IpDetector ipDetector;
    private final AltDetectConfiguration altDetectConfiguration;

    public AltDetectionService(PlayerManager playerManager, PlayerIpRepository playerIpRepository, AltDetectWhitelistRepository altDetectWhitelistRepository, PermissionHandler permission, AltDetectConfiguration altDetectConfiguration) {
        this.playerManager = playerManager;
        ipDetector = new IpDetector(playerIpRepository);
        this.altDetectConfiguration = altDetectConfiguration;
        this.altDetectors = Arrays.asList(new UsernameDetector(), ipDetector);
        this.playerIpRepository = playerIpRepository;
        this.altDetectWhitelistRepository = altDetectWhitelistRepository;
        this.permission = permission;
    }

    public void addToWhitelist(CommandSender sender, SppPlayer player1, SppPlayer player2) {
        if (!permission.has(sender, altDetectConfiguration.whitelistPermission)) {
            throw new NoPermissionException();
        }
        altDetectWhitelistRepository.addWhitelistedItem(player1.getId(), player2.getId());
        sender.sendMessage("Successfully added to whitelist");
    }

    public void removeFromWhitelist(CommandSender sender, SppPlayer player1, SppPlayer player2) {
        if (!permission.has(sender, altDetectConfiguration.whitelistPermission)) {
            throw new NoPermissionException();
        }
        altDetectWhitelistRepository.removeWhitelistedItem(player1.getId(), player2.getId());
        sender.sendMessage("Successfully removed from whitelist");
    }

    public List<AltDetectWhitelistedItem> getWhitelistedItems(CommandSender sender, int offset, int amount) {
        if (!permission.has(sender, altDetectConfiguration.whitelistPermission)) {
            throw new NoPermissionException();
        }
        return altDetectWhitelistRepository.getAllPAgedWhitelistedItems(offset, amount);
    }

    public void detectAltAccount(SppPlayer player) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> getAltAccounts(player).forEach(a -> Bukkit.getPluginManager().callEvent(new AltDetectEvent(a))));
    }

    public List<AltDetectResult> getAltAccounts(SppPlayer sppPlayer) {
        ArrayList<AltDetectResult> result = new ArrayList<>();
        if (permission.has(sppPlayer.getOfflinePlayer(), altDetectConfiguration.bypassPermission)) {
            return result;
        }
        OfflinePlayer player = sppPlayer.isOnline() ? sppPlayer.getPlayer() : sppPlayer.getOfflinePlayer();
        String playerName = player.getName();

        String playerIp = getPlayerIp(sppPlayer);
        AltDetectInfo altDetectInfo = new AltDetectInfo(playerName, playerIp);

        List<AltDetectWhitelistedItem> whitelistedItems = altDetectWhitelistRepository.getWhitelistedItems(player.getUniqueId());

        Set<SppPlayer> playersToMatch = playerManager.getOnAndOfflinePlayers().stream()
            .filter(onAndOfflinePlayer -> !onAndOfflinePlayer.getId().equals(player.getUniqueId()))
            .filter(onAndOfflinePlayer -> !isWhitelisted(player, whitelistedItems, onAndOfflinePlayer))
            .filter(onAndOfflinePlayer -> !altDetectConfiguration.sameIpRequired || ipDetector.getResult(altDetectInfo, onAndOfflinePlayer) == SAME_IP)
            .collect(Collectors.toSet());

        for (SppPlayer onAndOfflinePlayer : playersToMatch) {
            List<AltDetectResultType> altDetectResults = altDetectors.stream().map(a -> a.getResult(altDetectInfo, onAndOfflinePlayer))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            if (!altDetectResults.isEmpty()) {
                result.add(new AltDetectResult(
                    player.getUniqueId(),
                    playerName,
                    onAndOfflinePlayer.getId(),
                    onAndOfflinePlayer.getUsername(),
                    altDetectResults));
            }
        }
        return result;
    }

    private String getPlayerIp(SppPlayer sppPlayer) {
        String playerIp;
        if (sppPlayer.isOnline()) {
            playerIp = BukkitUtils.getIpFromPlayer(sppPlayer.getPlayer());
        } else {
            playerIp = playerIpRepository.getLastIp(sppPlayer.getId()).orElse(null);
        }
        return playerIp;
    }

    private boolean isWhitelisted(OfflinePlayer player, List<AltDetectWhitelistedItem> whitelistedItems, SppPlayer onAndOfflinePlayer) {
        return whitelistedItems.stream().anyMatch(w -> w.isWhitelisted(player.getUniqueId(), onAndOfflinePlayer.getId()));
    }
}
