package net.shortninja.staffplus.core.domain.staff.ban.playerbans;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.config.BanConfiguration;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.database.BansRepository;
import net.shortninja.staffplus.core.domain.staff.infractions.Infraction;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionInfo;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionProvider;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplusplus.ban.BanEvent;
import net.shortninja.staffplusplus.ban.BanExtensionEvent;
import net.shortninja.staffplusplus.ban.BanReductionEvent;
import net.shortninja.staffplusplus.ban.UnbanEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toMap;
import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;
import static net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanMessageStringUtil.replaceBanPlaceholders;
import static net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanType.PERM_BAN;
import static net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanType.TEMP_BAN;

@IocBean
@IocMultiProvider(InfractionProvider.class)
public class BanService implements InfractionProvider, net.shortninja.staffplusplus.ban.BanService {

    private static final String LIMIT = ".limit";
    private final PermissionHandler permission;
    private final BansRepository bansRepository;
    private final Options options;
    private final BanConfiguration banConfiguration;
    private final BanReasonResolver banReasonResolver;
    private final BanTemplateResolver banTemplateResolver;

    public BanService(PermissionHandler permission, BansRepository bansRepository, BanConfiguration banConfiguration, Options options,
                      BanReasonResolver banReasonResolver, BanTemplateResolver banTemplateResolver) {
        this.permission = permission;
        this.bansRepository = bansRepository;
        this.options = options;
        this.banConfiguration = banConfiguration;
        this.banReasonResolver = banReasonResolver;
        this.banTemplateResolver = banTemplateResolver;
    }

    public void permBan(CommandSender issuer, SppPlayer playerToBan, String reason, String template, boolean isSilent) {
        ban(issuer, playerToBan, reason, template, null, PERM_BAN, isSilent);
    }

    public void permBan(CommandSender issuer, SppPlayer playerToBan, String reason, boolean isSilent) {
        ban(issuer, playerToBan, reason, null, null, PERM_BAN, isSilent);
    }

    public void tempBan(CommandSender issuer, SppPlayer playerToBan, Long durationInMillis, String reason, String template, boolean isSilent) {
        permission.validateDuration(issuer, banConfiguration.permissionTempbanPlayer + LIMIT, durationInMillis);
        ban(issuer, playerToBan, reason, template, durationInMillis, TEMP_BAN, isSilent);
    }

    public void tempBan(CommandSender issuer, SppPlayer playerToBan, Long durationInMillis, String reason, boolean isSilent) {
        permission.validateDuration(issuer, banConfiguration.permissionTempbanPlayer + LIMIT, durationInMillis);
        ban(issuer, playerToBan, reason, null, durationInMillis, TEMP_BAN, isSilent);
    }

    public void extendBan(CommandSender sender, SppPlayer player, long duration) {
        Ban ban = getBanByBannedUuid(player.getId()).orElseThrow(() -> new BusinessException("&CThis player isn't banned"));
        if(ban.getEndDate() == null) {
            throw new BusinessException("The player is permanently banned. Cannot extend ban");
        }

        long newDuration = (ban.getEndTimestamp() - System.currentTimeMillis()) + duration;
        permission.validateDuration(sender, banConfiguration.permissionTempbanPlayer + LIMIT, newDuration);
        permission.validateDuration(sender, banConfiguration.permissionExtendBanPlayer + LIMIT, newDuration);

        bansRepository.setBanDuration(ban.getId(), ban.getEndTimestamp() + duration);
        Ban updatedBan = getBanByBannedUuid(player.getId()).orElseThrow(() -> new BusinessException("&CThis player isn't banned"));
        sendEvent(new BanExtensionEvent(updatedBan, duration));
    }

    public void reduceBan(CommandSender sender, SppPlayer player, long duration) {
        Ban ban = getBanByBannedUuid(player.getId()).orElseThrow(() -> new BusinessException("&CThis player isn't banned"));
        if(ban.getEndDate() == null) {
            throw new BusinessException("The player is permanently banned. Cannot extend ban");
        }

        long newDuration = (ban.getEndTimestamp() - System.currentTimeMillis()) - duration;
        permission.validateDuration(sender, banConfiguration.permissionReduceBanPlayer + LIMIT, newDuration);

        bansRepository.setBanDuration(ban.getId(), ban.getEndTimestamp() - duration);
        Ban updatedBan = getBanByBannedUuid(player.getId()).orElseThrow(() -> new BusinessException("&CThis player isn't banned"));
        sendEvent(new BanReductionEvent(updatedBan, duration));
    }

    public Optional<Ban> getBanByBannedUuid(UUID playerUuid) {
        return bansRepository.findActiveBan(playerUuid);
    }

    public Ban getById(int banId) {
        return bansRepository.findActiveBan(banId).orElseThrow(() -> new BusinessException("&CNo ban found with this id"));
    }

    public List<Ban> getAllPaged(int offset, int amount) {
        return bansRepository.getActiveBans(offset, amount);
    }

    public void unban(CommandSender issuer, SppPlayer playerToUnban, String reason, boolean isSilent) {
        Ban ban = bansRepository.findActiveBan(playerToUnban.getId())
            .orElseThrow(() -> new BusinessException("&CCannot unban, this user is not banned"));

        ban.setUnbannedByName(issuer instanceof Player ? issuer.getName() : "Console");
        ban.setUnbannedByUuid(issuer instanceof Player ? ((Player) issuer).getUniqueId() : CONSOLE_UUID);
        ban.setUnbanReason(reason);
        ban.setSilentUnban(isSilent);
        unban(ban);
    }

    public void unban(Player issuer, int banId, String reason) {
        Ban ban = bansRepository.findActiveBan(banId)
            .orElseThrow(() -> new BusinessException("&CCannot unban, this user is not banned"));

        ban.setUnbannedByName(issuer.getName());
        ban.setUnbannedByUuid(issuer.getUniqueId());
        ban.setUnbanReason(reason);
        unban(ban);
    }

    private void ban(CommandSender issuer, SppPlayer playerToBan, final String reason, final String providedTemplateName, Long durationInMillis, BanType banType, boolean isSilent) {
        if (providedTemplateName != null) permission.validate(issuer, banConfiguration.permissionBanTemplateOverwrite);
        String fullReason = banReasonResolver.resolveBanReason(reason, banType);
        String templateMessage = banTemplateResolver.resolveTemplate(reason, providedTemplateName, banType);

        if (playerToBan.isOnline() && permission.has(playerToBan.getPlayer(), banConfiguration.permissionBanByPass)) {
            throw new BusinessException("&CThis player bypasses being banned");
        }

        bansRepository.findActiveBan(playerToBan.getId()).ifPresent(ban -> {
            throw new BusinessException("&CCannot ban this player, the player is already banned");
        });

        String issuerName = issuer instanceof Player ? issuer.getName() : "Console";
        UUID issuerUuid = issuer instanceof Player ? ((Player) issuer).getUniqueId() : CONSOLE_UUID;

        Long endDate = durationInMillis == null ? null : System.currentTimeMillis() + durationInMillis;
        Ban ban = new Ban(fullReason, endDate, issuerName, issuerUuid, playerToBan.getUsername(), playerToBan.getId(), isSilent);
        ban.setId(bansRepository.addBan(ban));

        String banMessage = replaceBanPlaceholders(templateMessage, ban);
        sendEvent(new BanEvent(ban, banMessage));
    }

    private void unban(Ban ban) {
        bansRepository.update(ban);
        sendEvent(new UnbanEvent(ban));
    }

    @Override
    public List<? extends Infraction> getInfractions(Player executor, UUID playerUUID) {
        if (!options.infractionsConfiguration.isShowBans()) {
            return Collections.emptyList();
        }
        return bansRepository.getBansForPlayer(playerUUID);
    }

    @Override
    public Optional<InfractionInfo> getInfractionsInfo() {
        if (!options.infractionsConfiguration.isShowBans()) {
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

}
