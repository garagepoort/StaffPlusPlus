package net.shortninja.staffplus.staff.altaccountdetect;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.NoPermissionException;
import net.shortninja.staffplus.event.altdetect.AltDetectEvent;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.altaccountdetect.checks.AltDetector;
import net.shortninja.staffplus.staff.altaccountdetect.checks.AltDetectInfo;
import net.shortninja.staffplus.staff.altaccountdetect.checks.IpDetector;
import net.shortninja.staffplus.staff.altaccountdetect.checks.UsernameDetector;
import net.shortninja.staffplus.staff.altaccountdetect.database.ipcheck.PlayerIpRepository;
import net.shortninja.staffplus.staff.altaccountdetect.database.whitelist.AltDetectWhitelistRepository;
import net.shortninja.staffplus.unordered.altdetect.AltDetectTrustLevel;
import net.shortninja.staffplus.util.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class AltDetectionService {

    private final PlayerManager playerManager;
    private final List<AltDetector> altDetectors;
    private final PlayerIpRepository playerIpRepository;
    private final AltDetectWhitelistRepository altDetectWhitelistRepository;
    private final Permission permission;
    private final Options options;

    public AltDetectionService(PlayerManager playerManager, PlayerIpRepository playerIpRepository, AltDetectWhitelistRepository altDetectWhitelistRepository, Permission permission, Options options) {
        this.playerManager = playerManager;
        this.altDetectors = Arrays.asList(new UsernameDetector(), new IpDetector(playerIpRepository));
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
        List<SppPlayer> onAndOfflinePlayers = playerManager.getOnAndOfflinePlayers();

        AltDetectInfo altDetectInfo = new AltDetectInfo(playerName, playerIp);
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            playerIpRepository.save(player.getUniqueId(), altDetectInfo.getIp());
            List<AltDetectWhitelistedItem> whitelistedItems = altDetectWhitelistRepository.getWhitelistedItems(player.getUniqueId());

            for (SppPlayer onAndOfflinePlayer : onAndOfflinePlayers) {
                boolean isWhitelisted = whitelistedItems.stream().anyMatch(w -> w.isWhitelisted(player.getUniqueId(), onAndOfflinePlayer.getId()));
                if (onAndOfflinePlayer.getId() == player.getUniqueId() || isWhitelisted) {
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
