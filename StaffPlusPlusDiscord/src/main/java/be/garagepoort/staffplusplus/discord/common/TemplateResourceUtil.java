package be.garagepoort.staffplusplus.discord.common;

import be.garagepoort.staffplusplus.discord.StaffPlusPlusDiscord;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.io.File.separator;

class TemplateResourceUtil {
    private static final String DISCORDTEMPLATES_DIR = "discordtemplates";

    private static final Logger logger = StaffPlusPlusDiscord.get().getLogger();

    private TemplateResourceUtil() {}

    static void saveTemplate(String templatePack, String templateFilePath, boolean replace) {
        File dataFolder = StaffPlusPlusDiscord.get().getDataFolder();
        String fullTemplatePath = (DISCORDTEMPLATES_DIR + separator + templatePack + separator + templateFilePath).replace('\\', '/');
        String fullDefaultTemplatePath = (DISCORDTEMPLATES_DIR + separator + "default" + separator + templateFilePath).replace('\\', '/');

        InputStream in = getResource(fullTemplatePath);
        if (in == null) {
            in = getResource(fullDefaultTemplatePath);
        }
        if(in == null) {
            logger.log(Level.SEVERE, "Could not find template " + fullTemplatePath);
            return;
        }

        File outFile = new File(dataFolder, fullTemplatePath);
        int lastIndex = fullTemplatePath.lastIndexOf(47);
        File outDir = new File(dataFolder, fullTemplatePath.substring(0, Math.max(lastIndex, 0)));
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                try (OutputStream out = new FileOutputStream(outFile)) {
                    byte[] buf = new byte[1024];

                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
                in.close();
            }
        } catch (IOException var10) {
            logger.log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, var10);
        }
    }

    private static InputStream getResource(@NotNull String filename) {
        try {
            URL url = TemplateResourceUtil.class.getClassLoader().getResource(filename);
            if (url == null) {
                return null;
            } else {
                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            }
        } catch (IOException var4) {
            return null;
        }
    }

    static String getFullTemplatePath(String templatePack, TemplateFile templateFile) {
        return StaffPlusPlusDiscord.get().getDataFolder() + separator + DISCORDTEMPLATES_DIR + separator + templatePack + separator + templateFile.getFilePath();
    }
}
