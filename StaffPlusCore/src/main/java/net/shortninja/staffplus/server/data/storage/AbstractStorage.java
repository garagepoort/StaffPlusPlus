package net.shortninja.staffplus.server.data.storage;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.ProvidedPlayer;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.attribute.Ticket;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.unordered.IWarning;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static net.shortninja.staffplus.IocContainer.getUserManager;

public abstract class AbstractStorage implements IStorage {

    protected abstract Connection getConnection() throws SQLException;

    @Override
    public short getGlassColor(User user) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT GlassColor FROM sp_playerdata WHERE Player_UUID=?")) {
            ps.setString(1, user.getUuid().toString());
            short data = 0;
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    data = rs.getShort("GlassColor");
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void setGlassColor(User user, short glassColor) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_playerdata(GlassColor, Player_UUID) " +
                     "VALUES(?, ?) ON DUPLICATE KEY UPDATE GlassColor=?;")) {
            insert.setInt(1, glassColor);
            insert.setString(2, user.getUuid().toString());
            insert.setInt(3, glassColor);
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<IWarning> getWarnings(UUID uuid) {
        List<IWarning> warnings = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_warnings WHERE Player_UUID = ?")
        ) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UUID playerUUID = UUID.fromString(rs.getString("Player_UUID"));
                    UUID warnerUuid = UUID.fromString(rs.getString("Warner_UUID"));

                    Optional<ProvidedPlayer> warner = getUserManager().getOnOrOfflinePlayer(warnerUuid);
                    String warnerName = warnerUuid.equals(StaffPlus.get().consoleUUID) ? "Console" : warner.map(ProvidedPlayer::getUsername).orElse("Unknown user");
                    int id = rs.getInt("ID");
                    //NPE \/
                    Optional<ProvidedPlayer> player = getUserManager().getOnOrOfflinePlayer(playerUUID);
                    String name = player.map(ProvidedPlayer::getUsername).orElse("Unknown user");
                    warnings.add(new Warning(playerUUID, name, id, rs.getString("Reason"), warnerName, warnerUuid, System.currentTimeMillis()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warnings;
    }

    @Override
    public void addWarning(IWarning warning) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_warnings(Reason, Warner_UUID, Player_UUID) " +
                     "VALUES(? ,?, ?);");) {
            insert.setString(1, warning.getReason());
            insert.setString(2, warning.getIssuerUuid().toString());
            insert.setString(3, warning.getUuid().toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void removeWarnings(UUID uuid) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_warnings WHERE UUID = ?");) {
            insert.setString(1, uuid.toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Ticket getTicketByUUID(UUID uuid) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_tickets WHERE UUID=?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Ticket(uuid, Bukkit.getPlayer(uuid).getDisplayName(),
                            rs.getInt("ID"), rs.getString("Inquiry"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Ticket getTickById(int id) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_tickets WHERE ID=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return new Ticket(UUID.fromString(rs.getString("UUID")),
                            Bukkit.getPlayer(UUID.fromString(rs.getString("UUID"))).getDisplayName(), id, rs.getString("Inquiry"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addTicket(Ticket ticket) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_tickets(UUID,ID,Inquiry) " +
                     "VALUES(?, ?, ?);")) {
            insert.setString(1, ticket.getUuid().toString());
            insert.setInt(2, ticket.getId());
            insert.setString(3, ticket.getInquiry());
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeTicket(Ticket ticket) {
        try (Connection sql = getConnection();
             PreparedStatement delete = sql.prepareCall("DELETE FROM sp_tickets WHERE UUID =?")) {
            delete.setString(1, ticket.getUuid().toString());
            delete.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<Ticket> getTickets() {
        Set<Ticket> tickets = new HashSet<>();
        try (Connection sql = getConnection();
             PreparedStatement get = sql.prepareCall("SELECT UUID, ID, Inquiry FROM sp_tickets ")) {
            try (ResultSet rs = get.executeQuery()) {
                while (rs.next()) {
                    tickets.add(new Ticket(UUID.fromString(rs.getString("UUID")),
                            Bukkit.getPlayer(UUID.fromString(rs.getString("UUID"))).getDisplayName(), rs.getInt("ID"), rs.getString("Inquiry")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
}
