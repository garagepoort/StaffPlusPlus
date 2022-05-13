package net.shortninja.staffplus.core.common.gui;

import be.garagepoort.mcioc.TubingPlugin;
import be.garagepoort.mcioc.configuration.files.ConfigurationFile;
import be.garagepoort.mcioc.permissions.TubingPermissionService;
import be.garagepoort.mcioc.tubinggui.test.TubingGuiTemplateTest;
import net.shortninja.staffplus.core.StaffPlus;
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
        return StaffPlus.class;
    }

    @Override
    public TubingPermissionService getTubingPermissionService() {
        return permissionHandler;
    }

    @Override
    public List<ConfigurationFile> getConfigurationFiles() {
        return Arrays.asList(
            new ConfigurationFile("config.yml", loadConfig("/config.yml")),
            new ConfigurationFile("configuration/permissions.yml", "permissions", loadConfig("/configuration/permissions.yml")),
            new ConfigurationFile("configuration/commands.yml", "commands", loadConfig("/configuration/commands.yml")),
            new ConfigurationFile("configuration/staffmode/modules.yml", "staffmode-modules", loadConfig("/configuration/staffmode/modules.yml")),
            new ConfigurationFile("configuration/staffmode/custom-modules.yml", "staffmode-custom-modules", loadConfig("/configuration/staffmode/custom-modules.yml")),
            new ConfigurationFile("configuration/staffmode/modes.yml", "staffmode-modes", loadConfig("/configuration/staffmode/modes.yml")),
            new ConfigurationFile("lang/lang_en.yml", "lang_en", loadConfig("/lang/lang_en.yml"))
        );
    }
}
