package net.shortninja.staffplus.core.examine.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;

import java.util.List;

@IocBean
public class ExamineConfiguration {

    @ConfigProperty("permissions:examine-inventory-interaction.online")
    public String permissionExamineInventoryInteraction;
    @ConfigProperty("permissions:examine-inventory-interaction.offline")
    public String permissionExamineInventoryInteractionOffline;
    @ConfigProperty("permissions:examine-view-inventory.online")
    public String permissionExamineViewInventory;
    @ConfigProperty("permissions:examine-view-inventory.offline")
    public String permissionExamineViewInventoryOffline;
    @ConfigProperty("commands:examine")
    public List<String> commandExamine;
    @ConfigProperty("examine-module.gui.info-line.food")
    @ConfigTransformer(ExamineModeItemLocationConfigTransformer.class)
    public int modeExamineFood;
    @ConfigProperty("examine-module.gui.info-line.food")
    @ConfigTransformer(ExamineModeItemLocationConfigTransformer.class)
    public int modeExamineIp;
    @ConfigProperty("examine-module.gui.info-line.gamemode")
    @ConfigTransformer(ExamineModeItemLocationConfigTransformer.class)
    public int modeExamineGamemode;
    @ConfigProperty("examine-module.gui.info-line.infractions")
    @ConfigTransformer(ExamineModeItemLocationConfigTransformer.class)
    public int modeExamineInfractions;
    @ConfigProperty("examine-module.gui.info-line.location")
    @ConfigTransformer(ExamineModeItemLocationConfigTransformer.class)
    public int modeExamineLocation;
    @ConfigProperty("examine-module.gui.info-line.notes")
    @ConfigTransformer(ExamineModeItemLocationConfigTransformer.class)
    public int modeExamineNotes;
    @ConfigProperty("examine-module.gui.info-line.freeze")
    @ConfigTransformer(ExamineModeItemLocationConfigTransformer.class)
    public int modeExamineFreeze;
    @ConfigProperty("examine-module.gui.info-line.warn")
    @ConfigTransformer(ExamineModeItemLocationConfigTransformer.class)
    public int modeExamineWarn;

    @ConfigProperty("examine-module.gui.title")
    public String examineGuiTitle;

    public String getCommandExamine() {
        return commandExamine.get(0);
    }


}
