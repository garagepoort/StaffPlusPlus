package net.shortninja.staffplus.core;

import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LangFileTest {

    @Test
    public void checkLangFilesContainSameKeys() {
        List<ConfigurationFile> configurationFiles = Arrays.asList(
            new ConfigurationFile("lang/lang_de.yml", loadConfig("/lang/lang_de.yml")),
            new ConfigurationFile("lang/lang_en.yml", loadConfig("/lang/lang_en.yml")),
            new ConfigurationFile("lang/lang_es.yml", loadConfig("/lang/lang_es.yml")),
            new ConfigurationFile("lang/lang_fr.yml", loadConfig("/lang/lang_fr.yml")),
            new ConfigurationFile("lang/lang_hr.yml", loadConfig("/lang/lang_hr.yml")),
            new ConfigurationFile("lang/lang_hu.yml", loadConfig("/lang/lang_hu.yml")),
            new ConfigurationFile("lang/lang_it.yml", loadConfig("/lang/lang_it.yml")),
            new ConfigurationFile("lang/lang_nl.yml", loadConfig("/lang/lang_nl.yml")),
            new ConfigurationFile("lang/lang_no.yml", loadConfig("/lang/lang_no.yml")),
            new ConfigurationFile("lang/lang_pt.yml", loadConfig("/lang/lang_pt.yml")),
            new ConfigurationFile("lang/lang_sv.yml", loadConfig("/lang/lang_sv.yml")),
            new ConfigurationFile("lang/lang_zh.yml", loadConfig("/lang/lang_zh.yml")),
            new ConfigurationFile("lang/lang_id.yml", loadConfig("/lang/lang_id.yml")),
            new ConfigurationFile("lang/lang_ru.yml", loadConfig("/lang/lang_ru.yml"))
        );
        Map<String, FileConfiguration> collect = configurationFiles.stream()
            .collect(Collectors.toMap(ConfigurationFile::getIdentifier, ConfigurationFile::getFileConfiguration, (a, b) -> a));

        for (String key : collect.keySet()) {
            FileConfiguration fileConfiguration = collect.get(key);
            Set<String> keys = fileConfiguration.getKeys(true);

            collect.keySet()
                .forEach(checkKey -> {
                    System.out.println("Validating language file ["+key+"] with ["+checkKey+"]");
                    assertThat(keys).containsExactlyInAnyOrderElementsOf(collect.get(checkKey).getKeys(true));
                });
        }
    }

    public FileConfiguration loadConfig(String path) {
        InputStream resource = this.getClass().getResourceAsStream(path);

        Validate.notNull(resource, "File cannot be null");
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(IOUtils.toString(resource, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new ConfigurationException("Cannot load " + path, e);
        }

        return config;
    }
}
