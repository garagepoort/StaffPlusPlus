<<<<<<< HEAD

=======
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
package net.shortninja.staffplus.player.attribute;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;
import net.shortninja.staffplus.StaffPlus;
<<<<<<< HEAD
import net.shortninja.staffplus.server.data.MySQLConnection;
import net.shortninja.staffplus.server.data.file.DataFile;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
=======
import net.shortninja.staffplus.server.data.file.DataFile;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
import java.util.Arrays;

public final class SecurityHandler {

    private static final int HASH_COST = 12;

    private static final DataFile dataFile = new DataFile("passwords.yml");
    private final SecureRandom random = new SecureRandom();
    private final BCrypt.Verifyer verifyer;
    private final BCrypt.Hasher hasher;

    public SecurityHandler() {
        this.verifyer = BCrypt.verifyer();
        this.hasher = BCrypt.with(BCrypt.Version.VERSION_2Y, random, LongPasswordStrategies.strict());
    }

    public byte[] getPassword(final Player player) {
<<<<<<< HEAD
        if (StaffPlus.get().options.storageType.equalsIgnoreCase("flatfile")) {
            dataFile.load();
            return dataFile.getString(player.getUniqueId().toString()).getBytes(StandardCharsets.UTF_8);
        } else if (StaffPlus.get().options.storageType.equalsIgnoreCase("mysql")) {
            try (Connection c = MySQLConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement("SELECT Password FROM sp_playerdata WHERE Player_UUID=?;")) {
                ps.setString(1, player.getUniqueId().toString());

                try (ResultSet set = ps.executeQuery()) {
                    return set.next() ? set.getBytes("Password") : null;
                }
            } catch (SQLException e) {
                throw new IllegalStateException("Could not open connection.", e);
            }
        }

        return null;
    }

=======
        return StaffPlus.get().storage.getPassword(player);
    }


>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
    public boolean hasPassword(final Player player) {
        byte[] password = this.getPassword(player);
        boolean result = password != null && password.length > 0;
        Arrays.fill(password, (byte) 0x0);

        return result;
    }

    public void setPassword(final Player player, final byte[] password) {
        final byte[] hashed = this.hash(password);
<<<<<<< HEAD

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

=======
        StaffPlus.get().storage.setPassword(player, hashed);
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
        Arrays.fill(password, (byte) 0x0);
        Arrays.fill(hashed, (byte) 0x0);
    }

    public boolean isPasswordMatch(final byte[] password, final byte[] passwordVerify) {
        final byte[] passwordHash = this.hash(password);
        final byte[] verifyHash = this.hash(passwordVerify);

        boolean result = verifyer.verify(passwordHash, verifyHash).verified;
        Arrays.fill(password, (byte) 0x0);
        Arrays.fill(passwordVerify, (byte) 0x0);
        Arrays.fill(passwordHash, (byte) 0x0);
        Arrays.fill(verifyHash, (byte) 0x0);

        return result;
    }

    private byte[] hash(final byte[] password) {
        return hasher.hash(SecurityHandler.HASH_COST, password);
    }
}

