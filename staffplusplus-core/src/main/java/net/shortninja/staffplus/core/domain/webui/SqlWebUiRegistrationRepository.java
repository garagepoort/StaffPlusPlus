package net.shortninja.staffplus.core.domain.webui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;

import java.util.UUID;



@IocBean
public class SqlWebUiRegistrationRepository implements WebUiRegistrationRepository {
    private final QueryBuilderFactory query;

    public SqlWebUiRegistrationRepository(QueryBuilderFactory query) {
        this.query = query;
    }

    @Override
    public void addRegistrationRequest(String playerName, UUID playerUuid, String authenticationKey, String role) {
        query.create().insertQuery("INSERT INTO sp_web_ui_registration(player_uuid, player_name, authentication_key, role, timestamp) " +
            "VALUES(?, ?, ?, ?, ?);", insert -> {
            insert.setString(1, playerUuid.toString());
            insert.setString(2, playerName);
            insert.setString(3, authenticationKey);
            insert.setString(4, role);
            insert.setLong(5, System.currentTimeMillis());
        });
    }
}
