package net.shortninja.staffplus.server.data.file;

import org.bukkit.configuration.InvalidConfigurationException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class EncodedDataFile extends DataFile {

    private final BASE64Encoder encoder = new BASE64Encoder();
    private final BASE64Decoder decoder = new BASE64Decoder();
    private final File encodedFile;

    public EncodedDataFile(String name) {
        super(name);

        this.encodedFile = new File(name.substring(0, name.lastIndexOf('.')) + ".bin");
        this.load();
    }

    @Override
    public void save() {
        if (!encodedFile.exists()) {
            try {
                encodedFile.createNewFile();
            } catch (IOException e) {
                throw new IllegalStateException("Could not create file.", e);
            }
        }

        try (FileWriter writer = new FileWriter(encodedFile)) {
            writer.write(encoder.encode(super.getConfiguration().saveToString().getBytes(StandardCharsets.UTF_8)));
            writer.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Could not save file.", e);
        }
    }

    @Override
    public void load() {
        if (!encodedFile.exists()) {
            return;
        }

        try {
            byte[] encoded = Files.readAllBytes(encodedFile.toPath());
            byte[] decoded = decoder.decodeBuffer(new String(encoded));

            super.getConfiguration().loadFromString(new String(decoded));
        } catch (IOException e) {
            throw new IllegalStateException("Could not load file.", e);
        } catch (InvalidConfigurationException e) {
            throw new IllegalStateException("Could not parse configuration.", e);
        }
    }

    public File getEncodedFile() {
        return encodedFile;
    }
}
