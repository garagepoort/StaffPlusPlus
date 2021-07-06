package net.shortninja.staffplus.core.domain.staff.ban.ipbans.database;

import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBan;

import java.util.List;
import java.util.Optional;

public interface IpBanRepository {

    List<IpBan> getBannedIps();

    Long saveBan(IpBan ipBan);

    Optional<IpBan> getBannedRule(String ipAddress);

    void deleteBan(IpBan ipBan);
}
