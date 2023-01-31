package net.shortninja.staffplus.core.punishments.ban.playerbans;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.punishments.ban.playerbans.config.BanConfiguration;
import net.shortninja.staffplus.core.punishments.ban.playerbans.database.BansRepository;
import net.shortninja.staffplus.core.domain.staff.infractions.Infraction;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionInfo;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionProvider;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.core.domain.staff.infractions.config.InfractionsConfiguration;
import net.shortninja.staffplusplus.ban.*;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.*;

import static java.util.stream.Collectors.toMap;
import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;
import static net.shortninja.staffplus.core.punishments.ban.playerbans.BanMessageStringUtil.replaceBanPlaceholders;
import static net.shortninja.staffplus.core.punishments.ban.playerbans.BanType.PERM_BAN;
import static net.shortninja.staffplus.core.punishments.ban.playerbans.BanType.TEMP_BAN;

@IocBean
@IocMultiProvider(InfractionProvider.class)
public class BanService implements InfractionProvider, net.shortninja.staffplusplus.ban.BanService {

    private static final String LIMIT = ".limit";
    private final PermissionHandler permission;
    private final BansRepository bansRepository;
    private final BanConfiguration banConfiguration;
    private final BanReasonResolver banReasonResolver;
    private final BanTemplateResolver banTemplateResolver;
    private final InfractionsConfiguration infractionsConfiguration;
    private final PlayerRanks playerRanks;

    public BanService(PermissionHandler permission,
                      BansRepository bansRepository,
                      BanConfiguration banConfiguration,
                      BanReasonResolver banReasonResolver,
                      BanTemplateResolver banTemplateResolver,
                      InfractionsConfiguration infractionsConfiguration,
                      @ConfigProperty("ban-module.ranks") List<String> ranks) {
        this.permission = permission;
        this.bansRepository = bansRepository;
        this.banConfiguration = banConfiguration;
        this.banReasonResolver = banReasonResolver;
        this.banTemplateResolver = banTemplateResolver;
        this.infractionsConfiguration = infractionsConfiguration;
        this.playerRanks = new PlayerRanks(ranks, permission);
    }

    @Override
    public void permBan(CommandSender issuer, SppPlayer playerToBan, String reason, String template, boolean isSilent) {
        ban(issuer, playerToBan, reason, template, null, PERM_BAN, isSilent);
    }

    @Override
    public void permBan(CommandSender issuer, SppPlayer playerToBan, String reason, boolean isSilent) {
        ban(issuer, playerToBan, reason, null, null, PERM_BAN, isSilent);
    }

    @Override
    public void tempBan(CommandSender issuer, SppPlayer playerToBan, Long durationInMillis, String reason, String template, boolean isSilent) {
        permission.validateDuration(issuer, banConfiguration.permissionTempbanPlayer + LIMIT, durationInMillis);
        ban(issuer, playerToBan, reason, template, durationInMillis, TEMP_BAN, isSilent);
    }

    @Override
    public void tempBan(CommandSender issuer, SppPlayer playerToBan, Long durationInMillis, String reason, boolean isSilent) {
        permission.validateDuration(issuer, banConfiguration.permissionTempbanPlayer + LIMIT, durationInMillis);
        ban(issuer, playerToBan, reason, null, durationInMillis, TEMP_BAN, isSilent);
    }

    @Override
    public void extendBan(CommandSender sender, SppPlayer player, long duration) {
        Ban ban = getBanByBannedUuid(player.getId()).orElseThrow(() -> new BusinessException("&CThis player isn't banned"));
        if (ban.getEndDate() == null) {
            throw new BusinessException("The player is permanently banned. Cannot extend ban");
        }

        long newDuration = (ban.getEndTimestamp() - System.currentTimeMillis()) + duration;
        permission.validateDuration(sender, banConfiguration.permissionTempbanPlayer + LIMIT, newDuration);
        permission.validateDuration(sender, banConfiguration.permissionExtendBanPlayer + LIMIT, duration);

        bansRepository.setBanDuration(ban.getId(), ban.getEndTimestamp() + duration);
        Ban updatedBan = getActiveById(ban.getId());
        sendEvent(new BanExtensionEvent(updatedBan, duration, sender));
    }

    @Override
    public void reduceBan(CommandSender sender, SppPlayer player, long duration) {
        Ban ban = getBanByBannedUuid(player.getId()).orElseThrow(() -> new BusinessException("&CThis player isn't banned"));
        if (ban.getEndDate() == null) {
            throw new BusinessException("The player is permanently banned. Cannot reduce ban");
        }

        permission.validateDuration(sender, banConfiguration.permissionReduceBanPlayer + LIMIT, duration);

        bansRepository.setBanDuration(ban.getId(), ban.getEndTimestamp() - duration);
        Ban updatedBan = getActiveById(ban.getId());
        sendEvent(new BanReductionEvent(updatedBan, duration, sender));
    }

    @Override
    public Optional<Ban> getBanByBannedUuid(UUID playerUuid) {
        return bansRepository.findActiveBan(playerUuid);
    }

    @Override
    public Ban getActiveById(int banId) {
        return bansRepository.findActiveBan(banId).orElseThrow(() -> new BusinessException("&CNo ban found with this id"));
    }

    @Override
    public Ban getById(int banId) {
        return bansRepository.getBan(banId).orElseThrow(() -> new BusinessException("&CNo ban found with this id"));
    }

    public List<Ban> getAllPaged(int offset, int amount) {
        return bansRepository.getActiveBans(offset, amount);
    }

    @Override
    public void unban(CommandSender issuer, SppPlayer playerToUnban, String reason, boolean isSilent) {
        if(!canBanRank(issuer, playerToUnban)) {
            throw new BusinessException("&CYou don't have permission to unban this player!");
        }

        Ban ban = bansRepository.findActiveBan(playerToUnban.getId())
            .orElseThrow(() -> new BusinessException("&CCannot unban, this user is not banned"));

        ban.setUnbannedByName(issuer instanceof Player ? issuer.getName() : "Console");
        ban.setUnbannedByUuid(issuer instanceof Player ? ((Player) issuer).getUniqueId() : CONSOLE_UUID);
        ban.setUnbanReason(reason);
        ban.setSilentUnban(isSilent);
        unban(ban);
    }

    @Override
    public void unban(SppPlayer issuer, int banId, String reason) {
        Ban ban = bansRepository.findActiveBan(banId)
            .orElseThrow(() -> new BusinessException("&CCannot unban, this user is not banned"));

        ban.setUnbannedByName(issuer.getUsername());
        ban.setUnbannedByUuid(issuer.getId());
        ban.setUnbanReason(reason);
        unban(ban);
    }

    private void ban(CommandSender issuer, SppPlayer playerToBan, final String reason, final String providedTemplateName, Long durationInMillis, BanType banType, boolean isSilent) {
        if (providedTemplateName != null) permission.validate(issuer, banConfiguration.permissionBanTemplateOverwrite);

        if (playerToBan.isOnline() && permission.has(playerToBan.getPlayer(), banConfiguration.permissionBanByPass)) {
            throw new BusinessException("&CThis player bypasses being banned");
        }
        if(!canBanRank(issuer, playerToBan)) {
            throw new BusinessException("&CYou don't have permission to ban this player!");
        }

        bansRepository.findActiveBan(playerToBan.getId()).ifPresent(ban -> {
            throw new BusinessException("&CCannot ban this player, the player is already banned");
        });

        String fullReason = banReasonResolver.resolveBanReason(reason, banType);
        String templateMessage = banTemplateResolver.resolveTemplate(reason, providedTemplateName, banType);
        String templateName = banTemplateResolver.getTemplateName(reason, providedTemplateName, banType).orElse(null);
        String issuerName = issuer instanceof Player ? issuer.getName() : "Console";
        UUID issuerUuid = issuer instanceof Player ? ((Player) issuer).getUniqueId() : CONSOLE_UUID;
        Long endDate = durationInMillis == null ? null : System.currentTimeMillis() + durationInMillis;

        Ban ban = new Ban(fullReason, endDate, issuerName, issuerUuid, playerToBan.getUsername(), playerToBan.getId(), isSilent, templateName);
        ban.setId(bansRepository.addBan(ban));

        String banMessage = replaceBanPlaceholders(templateMessage, ban);
        if (endDate == null || endDate > System.currentTimeMillis()) {
            sendEvent(new BanEvent(ban, banMessage));
        }
    }

    private void unban(Ban ban) {
        bansRepository.update(ban);
        sendEvent(new UnbanEvent(ban));
    }

    public List<Ban> getAppealedBans(int offset, int amount) {
        return bansRepository.getAppealedBans(offset, amount);
    }

    @Override
    public List<? extends Infraction> getInfractions(Player executor, UUID playerUUID) {
        if (!infractionsConfiguration.isShowBans()) {
            return Collections.emptyList();
        }
        return bansRepository.getBansForPlayer(playerUUID);
    }

    @Override
    public Optional<InfractionInfo> getInfractionsInfo() {
        if (!infractionsConfiguration.isShowBans()) {
            return Optional.empty();
        }
        Map<UUID, List<String>> banDurationByPlayer = bansRepository.getBanDurationByPlayer().entrySet().stream()
            .collect(toMap(Map.Entry::getKey, e -> Arrays.asList("&bTotal time banned: ", "&6" + JavaUtils.toHumanReadableDuration(e.getValue()))));


        for (UUID permBannedPlayer : bansRepository.getAllPermanentBannedPlayers()) {
            banDurationByPlayer.put(permBannedPlayer, Collections.singletonList("&CPermanently banned"));
        }

        return Optional.of(new InfractionInfo(InfractionType.BAN, bansRepository.getCountByPlayer(), banDurationByPlayer));
    }

    @Override
    public InfractionType getType() {
        return InfractionType.BAN;
    }

    @Override
    public long getTotalBanCount() {
        return bansRepository.getTotalCount();
    }

    @Override
    public long getActiveBanCount() {
        return bansRepository.getActiveCount();
    }

    @Override
    public long getBanCount(BanFilters banFilters) {
        return this.bansRepository.getBanCount(banFilters);
    }


    private boolean canBanRank(CommandSender issuer, SppPlayer bannedPlayer) {
        if (playerRanks.isEmpty() || issuer instanceof ConsoleCommandSender) {
            return true;
        }
        return playerRanks.hasHigherRank((Player) issuer, bannedPlayer.getOfflinePlayer());
    }
}
