package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.player.ip.database.PlayerIpRepository;

import java.util.List;
import java.util.stream.Collectors;

public class V63_AlterPlayerIpTableSetIpNumericMigration implements Migration {
    @Override
    public List<String> getStatements() {
        PlayerIpRepository playerIpRepository = StaffPlus.get().getIocContainer().get(PlayerIpRepository.class);
        return playerIpRepository.getAllIpRecords().stream().map(record -> "UPDATE sp_player_ips SET ip_numeric = " + JavaUtils.convertIp(record.getIp()) + ";").collect(Collectors.toList());
    }

    @Override
    public int getVersion() {
        return 63;
    }
}
