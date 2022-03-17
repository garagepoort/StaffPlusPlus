package net.shortninja.staffplus.core.application.config.migrators;

import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PhraseDetectionToGroupsMigrator implements StaffPlusPlusConfigMigrator {

    @Override
    public void migrate(List<ConfigurationFile> configs) {
        FileConfiguration config = getConfig(configs, "config");

        if (config.contains("chat-module.detection.phrases")) {
            List<String> phrases = config.getStringList("chat-module.detection.phrases");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("phrases", phrases);
            linkedHashMap.put("actions", new ArrayList<>());

            List<LinkedHashMap<String, Object>> groups =new ArrayList<>();
            groups.add(linkedHashMap);
            config.set("chat-module.detection.phrase-groups", groups);
            config.set("chat-module.detection.phrases", null);
        }
    }
}
