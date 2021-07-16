package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.vanish;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@IocBean
public class VanishModeItemLoader extends ModeItemLoader<VanishModeConfiguration> {
    public VanishModeItemLoader(IProtocolService protocolService) {
        super(protocolService);
    }

    @Override
    protected String getModuleName() {
        return "vanish-module";
    }

    @Override
    protected VanishModeConfiguration load() {

        Material modeVanishTypeOff = Options.stringToMaterial(sanitize(staffModeModulesConfig.getString("modules.vanish-module.item-off")));
        short modeVanishDataOff = getMaterialData(staffModeModulesConfig.getString("modules.vanish-module.item-off"));
        String modeVanishName = staffModeModulesConfig.getString("modules.vanish-module.name");
        List<String> modeVanishLore = JavaUtils.stringToList(staffModeModulesConfig.getString("modules.vanish-module.lore"));

        ItemStack modeVanishItemOff = Items.builder().setMaterial(modeVanishTypeOff).setData(modeVanishDataOff).setName(modeVanishName).setLore(modeVanishLore).build();
        modeVanishItemOff = protocolService.getVersionProtocol().addNbtString(modeVanishItemOff, getModuleName());

        VanishModeConfiguration modeItemConfiguration = new VanishModeConfiguration(getModuleName(), modeVanishItemOff);
        return super.loadGeneralConfig(modeItemConfiguration);
    }
}
