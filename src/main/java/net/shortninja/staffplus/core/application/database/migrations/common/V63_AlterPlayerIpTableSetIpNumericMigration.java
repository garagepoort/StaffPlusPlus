package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.player.ip.database.PlayerIpRepository;

import java.util.List;
import java.util.stream.Collectors;

@IocBean
@IocMultiProvider(Migration.class)
public class V63_AlterPlayerIpTableSetIpNumericMigration implements Migration {
    private final PlayerIpRepository playerIpRepository;

    public V63_AlterPlayerIpTableSetIpNumericMigration(PlayerIpRepository playerIpRepository) {
        this.playerIpRepository = playerIpRepository;
    }

    @Override
    public List<String> getStatements() {
        return playerIpRepository.getAllIpRecords().stream().map(record -> "UPDATE sp_player_ips SET ip_numeric = " + JavaUtils.convertIp(record.getIp()) + ";").collect(Collectors.toList());
    }

    @Override
    public int getVersion() {
        return 63;
    }
}
