package net.shortninja.staffplus.core.punishments.ban.ipbans.database;

import net.shortninja.staffplus.core.punishments.ban.ipbans.IpBan;

import java.util.List;
import java.util.Optional;

public interface IpBanRepository {

    List<IpBan> getBannedIps();

    Long saveBan(IpBan ipBan);

    Optional<IpBan> getActiveBannedRule(String ipAddress);

    void deleteBan(IpBan ipBan);
}
