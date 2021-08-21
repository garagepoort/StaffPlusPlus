package net.shortninja.staffplus.core.common.gui;

import be.garagepoort.mcioc.IocContainer;
import be.garagepoort.mcioc.common.TubingPluginProvider;
import be.garagepoort.mcioc.gui.GuiActionService;
import be.garagepoort.mcioc.gui.TubingGui;
import be.garagepoort.mcioc.gui.actionquery.ActionQueryParser;
import be.garagepoort.mcioc.gui.templates.ChatTemplateResolver;
import be.garagepoort.mcioc.gui.templates.GuiTemplateResolver;
import be.garagepoort.mcioc.gui.templates.TemplateConfigResolver;
import be.garagepoort.mcioc.gui.templates.xml.TubingGuiXmlParser;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.TubingBukkitUtilStub;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
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
    protected StaffPlus staffPlus;
    @Mock
    protected IocContainer iocContainer;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private TubingGui tubingGui;

    @BeforeEach
    public void setUp() {
        Object guiController = getGuiController();
        when(tubingPluginProvider.getPlugin()).thenReturn(staffPlus);
        when(staffPlus.getIocContainer()).thenReturn(iocContainer);
        doReturn(guiController).when(iocContainer).get(guiController.getClass());
        when(tubingGuiXmlParser.parseHtml(eq(player), any())).thenReturn(tubingGui);

        GuiTemplateResolver guiTemplateResolver = new GuiTemplateResolver(tubingPluginProvider, templateConfigResolver, permissionHandler, tubingGuiXmlParser);
        guiActionService = new GuiActionService(tubingPluginProvider, guiTemplateResolver, chatTemplateResolver, new ActionQueryParser(), new TubingBukkitUtilStub());
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

}