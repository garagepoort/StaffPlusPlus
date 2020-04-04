package net.shortninja.staffplus.player.attribute;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.file.DataFile;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
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
        return StaffPlus.get().storage.getPassword(player);
    }


    public boolean hasPassword(final Player player) {
        byte[] password = this.getPassword(player);
        boolean result = password != null && password.length > 0;
        Arrays.fill(password, (byte) 0x0);

        return result;
    }

    public void setPassword(final Player player, final byte[] password) {
        final byte[] hashed = this.hash(password);
        StaffPlus.get().storage.setPassword(player, hashed);
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

