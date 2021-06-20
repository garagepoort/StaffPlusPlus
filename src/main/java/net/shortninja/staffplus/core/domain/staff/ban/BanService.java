package net.shortninja.staffplus.core.domain.staff.ban;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.exceptions.NoPermissionException;
import net.shortninja.staffplus.core.common.time.TimeUnitShort;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.ban.config.BanConfiguration;
import net.shortninja.staffplus.core.domain.staff.ban.database.BansRepository;
import net.shortninja.staffplus.core.domain.staff.infractions.Infraction;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionInfo;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionProvider;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplusplus.ban.BanEvent;
import net.shortninja.staffplusplus.ban.UnbanEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

import static java.util.stream.Collectors.toMap;
import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;
import static net.shortninja.staffplus.core.domain.staff.ban.BanMessageStringUtil.replaceBanPlaceholders;
import static net.shortninja.staffplus.core.domain.staff.ban.BanType.PERM_BAN;
import static net.shortninja.staffplus.core.domain.staff.ban.BanType.TEMP_BAN;

@IocBean
@IocMultiProvider(InfractionProvider.class)
public class BanService implements InfractionProvider, net.shortninja.staffplusplus.ban.BanService {

    private final PermissionHandler permission;
    private final BansRepository bansRepository;
    private final Options options;
    private final BanConfiguration banConfiguration;
    private final BanReasonResolver banReasonResolver;
    private final BanTemplateResolver banTemplateResolver;

    private final Messages messages;

    public BanService(PermissionHandler permission, BansRepository bansRepository, Options options, Messages messages, BanReasonResolver banReasonResolver, BanTemplateResolver banTemplateResolver) {

        this.permission = permission;
        this.bansRepository = bansRepository;
        this.options = options;
        this.messages = messages;
        banConfiguration = options.banConfiguration;
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
        this.checkDurationPermission(issuer, durationInMillis);
        ban(issuer, playerToBan, reason, template, durationInMillis, TEMP_BAN, isSilent);
    }

    public void tempBan(CommandSender issuer, SppPlayer playerToBan, Long durationInMillis, String reason, boolean isSilent) {
        this.checkDurationPermission(issuer, durationInMillis);
        ban(issuer, playerToBan, reason, null, durationInMillis, TEMP_BAN, isSilent);
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
        if (providedTemplateName != null) permission.validate(issuer, options.banConfiguration.getPermissionBanTemplateOverwrite());
        String fullReason = banReasonResolver.resolveBanReason(reason, banType);
        String templateMessage = banTemplateResolver.resolveTemplate(reason, providedTemplateName, banType);

        if (playerToBan.isOnline() && permission.has(playerToBan.getPlayer(), banConfiguration.getPermissionBanByPass())) {
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

        kickPlayer(playerToBan, durationInMillis, issuerName, fullReason, templateMessage);
        sendEvent(new BanEvent(ban));
    }

    private void kickPlayer(SppPlayer playerToBan, Long duration, String issuerName, String reason, String templateMessage) {
        if (playerToBan.isOnline()) {
            String banMessage = replaceBanPlaceholders(templateMessage, playerToBan.getUsername(), issuerName, reason, duration);
            playerToBan.getPlayer().kickPlayer(messages.colorize(banMessage));
        }
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

    private void checkDurationPermission(CommandSender player, long durationProvided) {
        if(!(player instanceof Player)) {
            return;
        }

        List<String> permissions = permission.getPermissions(player);
        if(permissions.stream().noneMatch(p -> p.startsWith(banConfiguration.getPermissionTempbanPlayer()))) {
            throw new NoPermissionException();
        }
        Optional<Long> duration = permissions.stream()
            .filter(p -> p.startsWith(banConfiguration.getPermissionTempbanPlayer() + "."))
            .map(p -> TimeUnitShort.getDurationFromString(p.substring(p.lastIndexOf(".") + 1)))
            .max(Comparator.naturalOrder());

        if(duration.isPresent() && duration.get() < durationProvided) {
            throw new NoPermissionException();
        }
    }
}
