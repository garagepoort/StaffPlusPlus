package net.shortninja.staffplus.core.common.gui;

import be.garagepoort.mcioc.TubingPlugin;
import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.tubingbukkit.permissions.TubingPermissionService;
import be.garagepoort.mcioc.tubinggui.test.TubingGuiTemplateTest;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;

@MockitoSettings(strictness = Strictness.LENIENT)
public abstract class AbstractTubingGuiTemplateTest extends TubingGuiTemplateTest {

    @Mock
    protected PermissionHandler permissionHandler;

    @Override
    public Class<? extends TubingPlugin> getPluginClass() {
        return StaffPlusPlus.class;
    }

    @Override
    public TubingPermissionService getTubingPermissionService() {
        return permissionHandler;
    }

    @Override
    public List<ConfigurationFile> getConfigurationFiles() {
        return Arrays.asList(
            new ConfigurationFile("config.yml"),
            new ConfigurationFile("configuration/permissions.yml", "permissions"),
            new ConfigurationFile("configuration/commands.yml", "commands"),
            new ConfigurationFile("configuration/staffmode/modules.yml", "staffmode-modules"),
            new ConfigurationFile("configuration/staffmode/custom-modules.yml", "staffmode-custom-modules"),
            new ConfigurationFile("configuration/staffmode/modes.yml", "staffmode-modes"),
            new ConfigurationFile("lang/lang_en.yml", "lang_en")
        );
    }
}
