//package net.shortninja.staffplus.server.data.file;
//
//import org.bukkit.configuration.InvalidConfigurationException;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.util.Base64;
//
//public final class EncodedDataFile extends DataFile {
//
//    private static final Base64.Encoder encoder = Base64.getEncoder();
//    private static final Base64.Decoder decoder = Base64.getDecoder();
//    private final File encodedFile;
//
//    public EncodedDataFile(String name) {
//        super(name);
//
//        this.encodedFile = new File(name.substring(0, name.lastIndexOf('.')) + ".bin");
//        this.load();
//    }
//
//    @Override
//    public void save() {
//        if (!encodedFile.exists()) {
//            try {
//                encodedFile.createNewFile();
//            } catch (IOException e) {
//                throw new IllegalStateException("Could not create file.", e);
//            }
//        }
//
//        try (FileOutputStream fos = new FileOutputStream(encodedFile)) {
//            fos.write(encoder.encode(super.getConfiguration().saveToString().getBytes(StandardCharsets.UTF_8)));
//            fos.flush();
//        } catch (IOException e) {
//            throw new IllegalStateException("Could not save file.", e);
//        }
//    }
//
//    @Override
//    public void load() {
//        if (!encodedFile.exists()) {
//            return;
//        }
//
//        try {
//            byte[] encoded = Files.readAllBytes(encodedFile.toPath());
//            byte[] decoded = decoder.decode(new String(encoded));
//
//            super.getConfiguration().loadFromString(new String(decoded));
//        } catch (IOException e) {
//            throw new IllegalStateException("Could not load file.", e);
//        } catch (InvalidConfigurationException e) {
//            throw new IllegalStateException("Could not parse configuration.", e);
//        }
//    }
//
//    public File getEncodedFile() {
//        return encodedFile;
//    }
//}
