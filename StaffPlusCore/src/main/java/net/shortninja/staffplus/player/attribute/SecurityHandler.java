package net.shortninja.staffplus.player.attribute;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.MySQLConnection;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SecurityHandler {
    private static Map<UUID, String> hashedPasswords = new HashMap<UUID, String>();
    private MessageDigest encrypter;

    public SecurityHandler() {
        try {
            encrypter = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
    }

    public String getPassword(UUID uuid) {
        if (StaffPlus.get().options.storageType.equalsIgnoreCase("flatfile"))
            return hashedPasswords.containsKey(uuid) ? hashedPasswords.get(uuid) : "";
        else if (StaffPlus.get().options.storageType.equalsIgnoreCase("mysql")) {
            if (!hasPassword(uuid))
                return "";
            try (Connection sql = MySQLConnection.getConnection();
                 PreparedStatement ps = sql.prepareStatement("SELECT Password FROM sp_playerdata WHERE Player_UUID=?");) {
                ps.setString(1, uuid.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.getString("Password");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public boolean hasPassword(UUID uuid) {
        if (StaffPlus.get().options.storageType.equalsIgnoreCase("flatfile"))
            return hashedPasswords.containsKey(uuid);
        else if (StaffPlus.get().options.storageType.equalsIgnoreCase("mysql")) {
            try (Connection sql = MySQLConnection.getConnection();
                 PreparedStatement ps = sql.prepareStatement("SELECT Password FROM sp_playerdata WHERE Player_UUID=?");) {
                ps.setString(1, uuid.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean matches(UUID uuid, String input) {
        return hash(input, uuid).equals(getPassword(uuid));
    }

    public void setPassword(UUID uuid, String password, boolean shouldHash) {
        if (StaffPlus.get().options.storageType.equalsIgnoreCase("flatfile"))
            hashedPasswords.put(uuid, shouldHash ? hash(password, uuid) : password);
        else if (StaffPlus.get().options.storageType.equalsIgnoreCase("mysql")) {
            try (Connection sql = MySQLConnection.getConnection();
                 PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_playerdata(Password, Player_UUID) " +
                         "VALUES(?, ?)  ON DUPLICATE KEY UPDATE Password=?;")) {
                String hashPass = shouldHash ? hash(password, uuid) : password;
                insert.setString(1, hashPass);
                insert.setString(2, uuid.toString());
                insert.setString(3, hashPass);
                insert.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String hash(String string, UUID uuid) {
        string += uuid.toString();
        encrypter.update(string.getBytes(), 0, string.length());
        return new BigInteger(1, encrypter.digest()).toString(16);
    }
}