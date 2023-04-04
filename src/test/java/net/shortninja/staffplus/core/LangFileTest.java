package net.shortninja.staffplus.core;

import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.FileConfiguration;
import be.garagepoort.mcioc.configuration.yaml.configuration.file.YamlConfiguration;
import be.garagepoort.mcioc.tubinggui.exceptions.TubingGuiException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LangFileTest {

    @Test
    public void checkLangFilesContainSameKeys() {
        List<ConfigurationFile> configurationFiles = Arrays.asList(
            new ConfigurationFile("lang/lang_de.yml"),
            new ConfigurationFile("lang/lang_en.yml"),
            new ConfigurationFile("lang/lang_es.yml"),
            new ConfigurationFile("lang/lang_fr.yml"),
            new ConfigurationFile("lang/lang_hr.yml"),
            new ConfigurationFile("lang/lang_hu.yml"),
            new ConfigurationFile("lang/lang_it.yml"),
            new ConfigurationFile("lang/lang_lt.yml"),
            new ConfigurationFile("lang/lang_nl.yml"),
            new ConfigurationFile("lang/lang_no.yml"),
            new ConfigurationFile("lang/lang_pt.yml"),
            new ConfigurationFile("lang/lang_sv.yml"),
            new ConfigurationFile("lang/lang_zh.yml"),
            new ConfigurationFile("lang/lang_id.yml"),
            new ConfigurationFile("lang/lang_ru.yml")
        );
        Map<String, FileConfiguration> collect = configurationFiles.stream()
            .collect(Collectors.toMap(ConfigurationFile::getIdentifier, c -> loadConfig(c.getPath()), (a, b) -> a));

        for (String key : collect.keySet()) {
            FileConfiguration fileConfiguration = collect.get(key);
            Collection<String> keys = fileConfiguration.getKeys(true);

            collect.keySet()
                .forEach(checkKey -> {
                    System.out.println("Validating language file ["+key+"] with ["+checkKey+"]");
                    assertThat(keys).containsExactlyInAnyOrderElementsOf(collect.get(checkKey).getKeys(true));
                });
        }
    }

    public FileConfiguration loadConfig(String path) {
        InputStream resource = this.getClass().getResourceAsStream("/" + path);

        Validate.notNull(resource, "File cannot be null");
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(IOUtils.toString(resource));
        } catch (Exception e) {
            throw new TubingGuiException("Cannot load " + path, e);
        }

        return config;
    }
}
