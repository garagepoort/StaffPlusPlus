package net.shortninja.staffplus.core.domain.staff.altaccountdetect;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.NoPermissionException;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.checks.AltDetectInfo;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.checks.AltDetector;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.checks.IpDetector;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.checks.UsernameDetector;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.database.ipcheck.PlayerIpRepository;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.database.whitelist.AltDetectWhitelistRepository;
import net.shortninja.staffplusplus.altdetect.AltDetectEvent;
import net.shortninja.staffplusplus.altdetect.AltDetectTrustLevel;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@IocBean
public class AltDetectionService {

    private final PlayerManager playerManager;
    private final List<AltDetector> altDetectors;
    private final PlayerIpRepository playerIpRepository;
    private final AltDetectWhitelistRepository altDetectWhitelistRepository;
    private final PermissionHandler permission;
    private final Options options;
    private final IpDetector ipDetector;

    public AltDetectionService(PlayerManager playerManager, PlayerIpRepository playerIpRepository, AltDetectWhitelistRepository altDetectWhitelistRepository, PermissionHandler permission, Options options) {
        this.playerManager = playerManager;
        ipDetector = new IpDetector(playerIpRepository);
        this.altDetectors = Arrays.asList(new UsernameDetector(), ipDetector);
        this.playerIpRepository = playerIpRepository;
        this.altDetectWhitelistRepository = altDetectWhitelistRepository;
        this.permission = permission;
        this.options = options;
    }

    public void addToWhitelist(CommandSender sender, SppPlayer player1, SppPlayer player2) {
        if (!permission.has(sender, options.altDetectConfiguration.getWhitelistPermission())) {
            throw new NoPermissionException();
        }
        altDetectWhitelistRepository.addWhitelistedItem(player1.getId(), player2.getId());
        sender.sendMessage("Successfully added to whitelist");
    }

    public void removeFromWhitelist(CommandSender sender, SppPlayer player1, SppPlayer player2) {
        if (!permission.has(sender, options.altDetectConfiguration.getWhitelistPermission())) {
            throw new NoPermissionException();
        }
        altDetectWhitelistRepository.removeWhitelistedItem(player1.getId(), player2.getId());
        sender.sendMessage("Successfully removed from whitelist");
    }

    public List<AltDetectWhitelistedItem> getWhitelistedItems(CommandSender sender, int offset, int amount) {
        if (!permission.has(sender, options.altDetectConfiguration.getWhitelistPermission())) {
            throw new NoPermissionException();
        }
        return altDetectWhitelistRepository.getAllPAgedWhitelistedItems(offset, amount);
    }

    public void detectAltAccount(Player player) {
        if (permission.has(player, options.altDetectConfiguration.getBypassPermission())) {
            // Bypass the alt detection
            return;
        }

        String playerIp = player.getAddress().getAddress().getHostAddress().replace("/", "");
        String playerName = player.getName();
        Set<SppPlayer> onAndOfflinePlayers = playerManager.getOnAndOfflinePlayers();

        AltDetectInfo altDetectInfo = new AltDetectInfo(playerName, playerIp);
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            playerIpRepository.save(player.getUniqueId(), altDetectInfo.getIp());
            List<AltDetectWhitelistedItem> whitelistedItems = altDetectWhitelistRepository.getWhitelistedItems(player.getUniqueId());

            for (SppPlayer onAndOfflinePlayer : onAndOfflinePlayers) {
                boolean isWhitelisted = whitelistedItems.stream().anyMatch(w -> w.isWhitelisted(player.getUniqueId(), onAndOfflinePlayer.getId()));
                if (onAndOfflinePlayer.getId().equals(player.getUniqueId()) || isWhitelisted) {
                    continue;
                }

                boolean sameIp = ipDetector.getScore(altDetectInfo, onAndOfflinePlayer) == 1;
                if(options.altDetectConfiguration.isSameIpRequired() && !sameIp) {
                    continue;
                }

                int trustScore = altDetectors.stream().mapToInt(a -> a.getScore(altDetectInfo, onAndOfflinePlayer)).sum();
                if (trustScore != 0) {
                    Bukkit.getPluginManager().callEvent(new AltDetectEvent(new AltDetectResult(
                        player.getUniqueId(),
                        playerName,
                        onAndOfflinePlayer.getId(),
                        onAndOfflinePlayer.getUsername(),
                        AltDetectTrustLevel.fromScore(trustScore)
                    )));
                }
            }
        });
    }

}
