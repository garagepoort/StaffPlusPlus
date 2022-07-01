package net.shortninja.staffplus.core;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.common.TubingConfigurationProvider;
import be.garagepoort.mcioc.configuration.files.ConfigMigrator;
import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import net.shortninja.staffplus.core.application.config.migrators.BlacklistModuleMigrator;
import net.shortninja.staffplus.core.application.config.migrators.CommandsMigrator;
import net.shortninja.staffplus.core.application.config.migrators.CommandsMultipleAliasesMigrator;
import net.shortninja.staffplus.core.application.config.migrators.CommandsV2Migrator;
import net.shortninja.staffplus.core.application.config.migrators.CustomStaffModeModuleCommandMigrator;
import net.shortninja.staffplus.core.application.config.migrators.FreezeModuleMigrator;
import net.shortninja.staffplus.core.application.config.migrators.PermissionsMigrator;
import net.shortninja.staffplus.core.application.config.migrators.PermissionsV2Migrator;
import net.shortninja.staffplus.core.application.config.migrators.PhraseDetectionToGroupsMigrator;
import net.shortninja.staffplus.core.application.config.migrators.ReportMessagesMigrator;
import net.shortninja.staffplus.core.application.config.migrators.ServerSyncBooleanMigrator;
import net.shortninja.staffplus.core.application.config.migrators.SoundOrbPickupMigrator;
import net.shortninja.staffplus.core.application.config.migrators.StaffChatChannelMigrator;
import net.shortninja.staffplus.core.application.config.migrators.StaffChatMessageFormatMigrator;
import net.shortninja.staffplus.core.application.config.migrators.StaffCustomModulesCommandMigrator;
import net.shortninja.staffplus.core.application.config.migrators.StaffModeCommandMigrator;
import net.shortninja.staffplus.core.application.config.migrators.StaffModeModulesMigrator;
import net.shortninja.staffplus.core.application.config.migrators.StaffModeNewConfiguredCommandsMigrator;
import net.shortninja.staffplus.core.application.config.migrators.StaffModesMigrator;
import net.shortninja.staffplus.core.application.config.migrators.ThresholdCommandsMigrator;
import net.shortninja.staffplus.core.application.config.migrators.WarningCommandsMigrator;

import java.util.Arrays;
import java.util.List;

@IocBean
public class StaffPlusPlusConfigurationProvider implements TubingConfigurationProvider {
    @Override
    public List<ConfigMigrator> getConfigurationMigrators() {
        return Arrays.asList(
            new StaffModeCommandMigrator(),
            new StaffModeModulesMigrator(),
            new PermissionsMigrator(),
            new CommandsMigrator(),
            new StaffModesMigrator(),
            new StaffChatChannelMigrator(),
            new StaffChatMessageFormatMigrator(),
            new ReportMessagesMigrator(),
            new PermissionsV2Migrator(),
            new CommandsV2Migrator(),
            new CustomStaffModeModuleCommandMigrator(),
            new StaffCustomModulesCommandMigrator(),
            new WarningCommandsMigrator(),
            new StaffModeNewConfiguredCommandsMigrator(),
            new ThresholdCommandsMigrator(),
            new FreezeModuleMigrator(),
            new ServerSyncBooleanMigrator(),
            new PhraseDetectionToGroupsMigrator(),
            new CommandsMultipleAliasesMigrator(),
            new SoundOrbPickupMigrator(),
            new BlacklistModuleMigrator());
    }

    @Override
    public List<ConfigurationFile> getConfigurationFiles() {
        return Arrays.asList(
            new ConfigurationFile("config.yml"),
            new ConfigurationFile("configuration/permissions.yml", "permissions"),
            new ConfigurationFile("configuration/commands.yml", "commands"),
            new ConfigurationFile("configuration/staffmode/modules.yml", "staffmode-modules"),
            new ConfigurationFile("configuration/staffmode/custom-modules.yml", "staffmode-custom-modules", true),
            new ConfigurationFile("configuration/staffmode/modes.yml", "staffmode-modes"),
            new ConfigurationFile("lang/lang_de.yml", "lang_de"),
            new ConfigurationFile("lang/lang_en.yml", "lang_en"),
            new ConfigurationFile("lang/lang_es.yml", "lang_es"),
            new ConfigurationFile("lang/lang_fr.yml", "lang_fr"),
            new ConfigurationFile("lang/lang_hr.yml", "lang_hr"),
            new ConfigurationFile("lang/lang_hu.yml", "lang_hu"),
            new ConfigurationFile("lang/lang_it.yml", "lang_it"),
            new ConfigurationFile("lang/lang_nl.yml", "lang_nl"),
            new ConfigurationFile("lang/lang_no.yml", "lang_no"),
            new ConfigurationFile("lang/lang_pt.yml", "lang_pt"),
            new ConfigurationFile("lang/lang_sv.yml", "lang_sv"),
            new ConfigurationFile("lang/lang_zh.yml", "lang_zh"),
            new ConfigurationFile("lang/lang_id.yml", "lang_id"),
            new ConfigurationFile("lang/lang_ru.yml", "lang_ru")
        );
    }
}