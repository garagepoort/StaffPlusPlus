package net.shortninja.staffplus.core.domain.staff.mode.custommodules;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeItemsService;
import net.shortninja.staffplus.core.domain.staff.mode.custommodules.preprocessors.CustomModulePreProcessor;
import net.shortninja.staffplus.core.domain.staff.mode.custommodules.state.CustomModuleStateMachine;
import net.shortninja.staffplus.core.domain.staff.mode.handler.GadgetHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static net.shortninja.staffplus.core.domain.staff.mode.custommodules.CustomModuleConfiguration.ModuleType.COMMAND_DYNAMIC;

@IocBean
public class CustomModuleHandler {

    private final GadgetHandler gadgetHandler;
    private final CustomModuleStateMachine customModuleStateMachine;
    private final StaffModeItemsService staffModeItemsService;
    private final Messages messages;
    private final List<CustomModulePreProcessor> customModulePreProcessors;

    public CustomModuleHandler(GadgetHandler gadgetHandler,
                               CustomModuleStateMachine customModuleStateMachine, StaffModeItemsService staffModeItemsService, Messages messages,
                               @IocMulti(CustomModulePreProcessor.class) List<CustomModulePreProcessor> customModulePreProcessors) {
        this.gadgetHandler = gadgetHandler;
        this.customModuleStateMachine = customModuleStateMachine;
        this.staffModeItemsService = staffModeItemsService;
        this.messages = messages;
        this.customModulePreProcessors = customModulePreProcessors;
    }

    public boolean handleCustomModule(Player player, ItemStack item) {
        Optional<CustomModuleConfiguration> customModuleConfiguration = staffModeItemsService.getCustomModule(item);
        if (!customModuleConfiguration.isPresent()) {
            return false;
        }
        CustomModuleConfiguration moduleConfiguration = customModuleConfiguration.get();

        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("%clicker%", player.getName());
        Player targetPlayer = JavaUtils.getTargetPlayer(player);
        if (targetPlayer != null) {
            placeholders.put("%clicked%", targetPlayer.getName());
        }

        if (moduleConfiguration.getType() == COMMAND_DYNAMIC && targetPlayer == null) {
            messages.send(player, "No target in range", messages.prefixGeneral);
            return true;
        }

        CustomModuleExecutor moduleExecution = (p, pl) -> gadgetHandler.executeModule(p, targetPlayer, moduleConfiguration, pl);
        for (CustomModulePreProcessor customModulePreProcessor : customModulePreProcessors) {
            moduleExecution = customModulePreProcessor.process(moduleExecution, moduleConfiguration, placeholders);
        }
        moduleExecution.execute(player, placeholders);
        if (moduleConfiguration.getEnableState().isPresent() || moduleConfiguration.getDisableState().isPresent()) {
            customModuleStateMachine.switchState(player, moduleConfiguration.getDisableState().orElse(null), moduleConfiguration.getEnableState().orElse(null));
        }
        return true;
    }
}
