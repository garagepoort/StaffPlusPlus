package net.shortninja.staffplus.application.data;

import net.shortninja.staffplus.StaffPlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class LanguageFile {
    public static final String[] LANG_FILES =
            {
                    "lang_en", "lang_sv", "lang_de", "lang_nl", "lang_es", "lang_hr", "lang_no", "lang_fr", "lang_hu", "lang_it",
                    "lang_zh", "lang_pt"
            };
    private final String FILE_NAME = StaffPlus.get().getConfig().getString("lang") + ".yml";
    private FileConfiguration lang;
    private File langFile;

    public LanguageFile() {
        for (String fileName : LANG_FILES) {
            try {
                copyFile(fileName);
            } catch (Exception exception) {
                StaffPlus.get().getLogger().severe("Error occured while initializing '" + fileName + "'!");
                StaffPlus.get().getLogger().severe(exception.getMessage());
            }
        }

        setup();
    }

    public void setup() {
        langFile = new File(StaffPlus.get().getDataFolder() + "/lang/", FILE_NAME);
        lang = YamlConfiguration.loadConfiguration(langFile);
    }

    public FileConfiguration get() {
        return lang;
    }

    private void copyFile(String fileName) throws IOException {
        File file = new File(StaffPlus.get().getDataFolder() + "/lang/", fileName + ".yml");
        InputStream in = this.getClass().getResourceAsStream("/lang/" + fileName + ".yml");
        YamlConfiguration newLang = YamlConfiguration.loadConfiguration(new InputStreamReader(in));
        in = this.getClass().getResourceAsStream("/lang/" + fileName + ".yml");
        if (!file.exists()) {
            StaffPlus.get().getDataFolder().mkdirs();
            file.getParentFile().mkdirs();
            file.createNewFile();
            StaffPlus.get().getLogger().info("Creating language file '" + fileName + "'.");
        } else if (YamlConfiguration.loadConfiguration(file).getInt("lang-version") <
                newLang.getInt("lang-version")) {
            YamlConfiguration oldLang = YamlConfiguration.loadConfiguration(file);
            for (String key : newLang.getKeys(false)) {
                if (oldLang.getString(key, "").equals("")) {
                    oldLang.set(key, newLang.get(key));
                    oldLang.save(file);
                }
            }
            oldLang.set("lang-version", newLang.getInt("lang-version"));
            oldLang.save(file);
            in.close();
            return;
        } else
            return;

        OutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int current = 0;

        while ((current = in.read(buffer)) > -1) {
            out.write(buffer, 0, current);
        }

        out.close();
        in.close();
    }
}
