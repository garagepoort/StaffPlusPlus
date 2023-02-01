package net.shortninja.staffplus.core.altaccountdetect.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.altaccountdetect.AltDetectWhitelistedItem;

import java.util.List;
import java.util.UUID;

@IocBean
public class AltDetectWhitelistRepositoryImpl implements AltDetectWhitelistRepository {

    private final QueryBuilderFactory query;

    public AltDetectWhitelistRepositoryImpl(QueryBuilderFactory query) {
        this.query = query;
    }

    @Override
    public void addWhitelistedItem(UUID playerUuid1, UUID playerUuid2) {
        query.create().insertQuery("INSERT INTO sp_alt_detect_whitelist(player_uuid_1, player_uuid_2) " +
            "SELECT * FROM (SELECT ?, ?) AS tmp  WHERE NOT EXISTS (SELECT 1 FROM sp_alt_detect_whitelist WHERE (player_uuid_1=? OR player_uuid_2=?) AND (player_uuid_1=? OR player_uuid_2=?));", (insert) -> {
            insert.setString(1, playerUuid1.toString());
            insert.setString(2, playerUuid2.toString());
            insert.setString(3, playerUuid1.toString());
            insert.setString(4, playerUuid1.toString());
            insert.setString(5, playerUuid2.toString());
            insert.setString(6, playerUuid2.toString());
        });
    }

    @Override
    public void removeWhitelistedItem(UUID playerUuid1, UUID playerUuid2) {
        query.create().deleteQuery("DELETE FROM sp_alt_detect_whitelist WHERE (player_uuid_1=? AND player_uuid_2=?) OR (player_uuid_1=? AND player_uuid_2=?)", (insert) -> {
            insert.setString(1, playerUuid1.toString());
            insert.setString(2, playerUuid2.toString());
            insert.setString(3, playerUuid2.toString());
            insert.setString(4, playerUuid1.toString());
        });
    }

    @Override
    public List<AltDetectWhitelistedItem> getWhitelistedItems(UUID playerUuid) {
        return query.create().find("SELECT * FROM sp_alt_detect_whitelist WHERE player_uuid_1 = ? OR player_uuid_2 = ?", (ps) -> {
            ps.setString(1, playerUuid.toString());
            ps.setString(2, playerUuid.toString());
        }, (rs) -> new AltDetectWhitelistedItem(UUID.fromString(rs.getString(1)), UUID.fromString(rs.getString(2))));
    }

    @Override
    public List<AltDetectWhitelistedItem> getAllPAgedWhitelistedItems(int offset, int amount) {
        return query.create().find("SELECT * FROM sp_alt_detect_whitelist LIMIT ?,?", (ps) -> {
            ps.setInt(1, offset);
            ps.setInt(2, amount);
        }, (rs) -> new AltDetectWhitelistedItem(UUID.fromString(rs.getString(1)), UUID.fromString(rs.getString(2))));
    }
}
