package net.shortninja.staffplus.player.attribute;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.MySQLConnection;
import net.shortninja.staffplus.server.data.file.EncodedDataFile;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public final class SecurityHandler {

    private final EncodedDataFile dataFile = new EncodedDataFile("passwords.yml");
    private final SecureRandom random = new SecureRandom();
    private final BCrypt.Verifyer verifyer;
    private final BCrypt.Hasher hasher;
    private final int cost = 12;

    public SecurityHandler() {
        this.verifyer = BCrypt.verifyer();
        this.hasher = BCrypt.with(BCrypt.Version.VERSION_2Y, random, LongPasswordStrategies.strict());
    }

    public String getPassword(Player player) {
        if (StaffPlus.get().options.storageType.equalsIgnoreCase("flatfile")) {
            dataFile.load();
            return dataFile.getString(player.getUniqueId().toString());
        } else if (StaffPlus.get().options.storageType.equalsIgnoreCase("mysql")) {
            try (Connection c = MySQLConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement("SELECT Password FROM sp_playerdata WHERE Player_UUID=?;")) {
                ps.setString(1, player.getUniqueId().toString());

                try (ResultSet set = ps.executeQuery()) {
                    return set.next() ? set.getString("Password") : null;
                }
            } catch (SQLException e) {
                throw new IllegalStateException("Could not open connection.", e);
            }
        }

        return null;
    }

    public boolean hasPassword(Player player) {
        String password = this.getPassword(player);

        return password != null && !password.isEmpty();
    }

    public void setPassword(Player player, String password) {
        byte[] pass = password.getBytes(StandardCharsets.UTF_8);
        byte[] hashed = hasher.hash(cost, pass);

        if (StaffPlus.get().options.storageType.equalsIgnoreCase("flatfile")) {
            dataFile.load();
            dataFile.getConfiguration().set(player.getUniqueId().toString(), hashed);
            dataFile.save();
        } else if (StaffPlus.get().options.storageType.equalsIgnoreCase("mysql")) {
            try (Connection c = MySQLConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement("INSERT INTO sp_playerdata (Player_UUID, Password) VALUES (?, ?) ON DUPLICATE KEY UPDATE Password=?;")) {
                ps.setString(1, player.getUniqueId().toString());
                ps.setBytes(2, hashed);
                ps.setBytes(3, hashed);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new IllegalStateException("Could not open connection.", e);
            }
        }

        Arrays.fill(pass, (byte) 0x0);
        Arrays.fill(hashed, (byte) 0x0);
    }

    public boolean isPasswordMatch(String password, String hash) {
        return verifyer.verify(password.getBytes(StandardCharsets.UTF_8), hash.getBytes(StandardCharsets.UTF_8)).verified;
    }
}