package net.shortninja.staffplus.core.common.gui;

import be.garagepoort.mcioc.IocContainer;
import be.garagepoort.mcioc.TubingPlugin;
import be.garagepoort.mcioc.common.TubingConfigurationProvider;
import be.garagepoort.mcioc.common.TubingPluginProvider;
import be.garagepoort.mcioc.gui.GuiActionService;
import be.garagepoort.mcioc.gui.actionquery.ActionQueryParser;
import be.garagepoort.mcioc.gui.model.InventoryMapper;
import be.garagepoort.mcioc.gui.model.TubingGui;
import be.garagepoort.mcioc.gui.style.TubingGuiStyleIdViewProvider;
import be.garagepoort.mcioc.gui.templates.ChatTemplateResolver;
import be.garagepoort.mcioc.gui.templates.GuiTemplateResolver;
import be.garagepoort.mcioc.gui.templates.TemplateConfigResolver;
import be.garagepoort.mcioc.gui.templates.TubingGuiTemplateParser;
import be.garagepoort.mcioc.gui.templates.xml.TubingGuiXmlParser;
import be.garagepoort.mcioc.gui.templates.xml.style.TubingGuiStyleParser;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.TubingBukkitUtilStub;
import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractGuiTemplateTest {
    protected static final UUID PLAYER_UUID = UUID.fromString("3723136f-40dd-4f27-8505-8fa880f14e95");
    protected GuiActionService guiActionService;

    @Mock
    protected Player player;
    @Mock
    protected TubingConfigurationProvider tubingConfigurationProvider;
    @Mock
    protected ChatTemplateResolver chatTemplateResolver;
    @Mock
    protected PermissionHandler permissionHandler;
    @Mock
    protected TubingPluginProvider tubingPluginProvider;
    @Mock
    protected TubingGuiXmlParser tubingGuiXmlParser;
    @Mock
    protected TubingGuiStyleParser tubingGuiStyleParser;
    @Mock
    protected InventoryMapper inventoryMapper;
    @Mock
    protected TubingGuiStyleIdViewProvider tubingGuiStyleIdViewProvider;
    @Mock
    protected StaffPlus staffPlus;
    @Mock
    protected IocContainer iocContainer;
    protected TemplateConfigResolver templateConfigResolverSpy;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private TubingGui tubingGui;
    private static MockedStatic<TubingPlugin> tubingPluginMockedStatic;

    @BeforeAll
    public static void allSetUp() {
        TubingPlugin tubingPlugin = mock(TubingPlugin.class);
        when(tubingPlugin.getName()).thenReturn("staffplus");

        tubingPluginMockedStatic = mockStatic(TubingPlugin.class);
        tubingPluginMockedStatic.when(TubingPlugin::getPlugin).thenReturn(tubingPlugin);
    }

    @AfterAll
    public static void closeMocks() {
        tubingPluginMockedStatic.close();
    }

    @BeforeEach
    public void setUp() {
        List<ConfigurationFile> configurationFiles = Arrays.asList(
            new ConfigurationFile("config.yml", loadConfig("/config.yml")),
            new ConfigurationFile("configuration/permissions.yml", loadConfig("/configuration/permissions.yml")),
            new ConfigurationFile("configuration/commands.yml", loadConfig("/configuration/commands.yml")),
            new ConfigurationFile("configuration/staffmode/modules.yml", loadConfig("/configuration/staffmode/modules.yml")),
            new ConfigurationFile("configuration/staffmode/custom-modules.yml", loadConfig("/configuration/staffmode/custom-modules.yml")),
            new ConfigurationFile("configuration/staffmode/modes.yml", loadConfig("/configuration/staffmode/modes.yml")),
            new ConfigurationFile("lang/lang_en.yml", loadConfig("/lang/lang_en.yml"))
        );

        Object guiController = getGuiController();
        Map<String, FileConfiguration> collect = configurationFiles.stream()
            .collect(Collectors.toMap(ConfigurationFile::getIdentifier, ConfigurationFile::getFileConfiguration, (a, b) -> a));
        when(tubingConfigurationProvider.getConfigurations()).thenReturn(collect);

        when(tubingPluginProvider.getPlugin()).thenReturn(staffPlus);
        when(staffPlus.getIocContainer()).thenReturn(iocContainer);
        doReturn(guiController).when(iocContainer).get(guiController.getClass());

        when(tubingGuiXmlParser.parseHtml(eq(player), any())).thenReturn(tubingGui);
        when(player.getUniqueId()).thenReturn(PLAYER_UUID);

        TemplateConfigResolver templateConfigResolver = new TemplateConfigResolver(tubingConfigurationProvider);
        templateConfigResolverSpy = spy(templateConfigResolver);

        GuiTemplateResolver guiTemplateResolver = new GuiTemplateResolver(tubingPluginProvider,
            this.templateConfigResolverSpy,
            permissionHandler,
            tubingGuiXmlParser,
            new TubingGuiTemplateParser(this.templateConfigResolverSpy),
            tubingGuiStyleParser);

        guiActionService = new GuiActionService(tubingPluginProvider,
            guiTemplateResolver,
            chatTemplateResolver,
            new ActionQueryParser(),
            new TubingBukkitUtilStub(),
            inventoryMapper,
            tubingGuiStyleIdViewProvider);

        guiActionService.loadGuiController(guiController.getClass());
    }

    public abstract Object getGuiController();

    public void validateMaterials(String guiXml) {
        Document document = Jsoup.parse(guiXml);
        Elements materialElements = document.getElementsByAttribute("material");
        for (Element materialElement : materialElements) {
            String materialString = materialElement.attr("material");
            Material.valueOf(materialString);
        }
    }

    public void validateXml(String xml, String pathToXml) throws URISyntaxException, IOException {
        java.net.URL url = AbstractGuiTemplateTest.class.getResource(pathToXml);
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        String templateXml = new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
        assertThat(xml).isEqualToNormalizingWhitespace(templateXml);
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
