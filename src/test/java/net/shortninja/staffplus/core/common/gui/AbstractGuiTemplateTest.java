package net.shortninja.staffplus.core.common.gui;

import be.garagepoort.mcioc.IocContainer;
import be.garagepoort.mcioc.TubingPlugin;
import be.garagepoort.mcioc.common.TubingPluginProvider;
import be.garagepoort.mcioc.gui.GuiActionService;
import be.garagepoort.mcioc.gui.model.InventoryMapper;
import be.garagepoort.mcioc.gui.model.TubingGui;
import be.garagepoort.mcioc.gui.actionquery.ActionQueryParser;
import be.garagepoort.mcioc.gui.style.TubingGuiStyleIdViewProvider;
import be.garagepoort.mcioc.gui.templates.ChatTemplateResolver;
import be.garagepoort.mcioc.gui.templates.GuiTemplateResolver;
import be.garagepoort.mcioc.gui.templates.TemplateConfigResolver;
import be.garagepoort.mcioc.gui.templates.TubingGuiTemplateParser;
import be.garagepoort.mcioc.gui.templates.xml.TubingGuiXmlParser;
import be.garagepoort.mcioc.gui.templates.xml.style.TubingGuiStyleParser;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.TubingBukkitUtilStub;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import org.bukkit.Material;
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

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractGuiTemplateTest {
    protected GuiActionService guiActionService;

    @Mock
    protected Player player;
    @Mock
    protected TemplateConfigResolver templateConfigResolver;
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
        Object guiController = getGuiController();
        when(tubingPluginProvider.getPlugin()).thenReturn(staffPlus);
        when(staffPlus.getIocContainer()).thenReturn(iocContainer);
        doReturn(guiController).when(iocContainer).get(guiController.getClass());
        when(tubingGuiXmlParser.parseHtml(eq(player), any())).thenReturn(tubingGui);

        GuiTemplateResolver guiTemplateResolver = new GuiTemplateResolver(tubingPluginProvider, templateConfigResolver, permissionHandler, tubingGuiXmlParser, new TubingGuiTemplateParser(templateConfigResolver), tubingGuiStyleParser);
        guiActionService = new GuiActionService(tubingPluginProvider, guiTemplateResolver, chatTemplateResolver, new ActionQueryParser(), new TubingBukkitUtilStub(), inventoryMapper, tubingGuiStyleIdViewProvider);
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
}
