package net.shortninja.staffplus.core.stafflocations.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.tubinggui.GuiActionBuilder;
import be.garagepoort.mcioc.tubinggui.GuiActionService;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.stafflocations.gui.StaffLocationFiltersMapper;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Command(
    command = "commands:staff-locations.view",
    permissions = "permissions:staff-locations.view",
    description = "Open staff locations GUI."
)
@IocBean(conditionalOnProperty = "staff-locations-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class ViewStaffLocationsCmd extends AbstractCmd {

    private final GuiActionService guiActionService;
    private final StaffLocationFiltersMapper staffLocationFiltersMapper;

    public ViewStaffLocationsCmd(Messages messages, PermissionHandler permissionHandler, CommandService commandService, GuiActionService guiActionService, StaffLocationFiltersMapper staffLocationFiltersMapper) {
        super(messages, permissionHandler, commandService);
        this.guiActionService = guiActionService;
        this.staffLocationFiltersMapper = staffLocationFiltersMapper;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        Player staff = validateIsPlayer(sender);
        if(optionalParameters.isEmpty()) {
            guiActionService.executeAction(staff, "staff-locations/view");
        }else {
            GuiActionBuilder guiActionBuilder = new GuiActionBuilder();
            guiActionBuilder.action("staff-locations/view/find-locations");

            optionalParameters.forEach((k, v) -> guiActionBuilder.param(k.substring(1), v));
            guiActionService.executeAction(staff, guiActionBuilder.build());
        }
        return true;
    }

    @Override
    protected List<String> getOptionalParameters() {
        return staffLocationFiltersMapper.getFilterKeys().stream()
            .map(k -> "-" + k)
            .collect(Collectors.toList());
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

    @Override
    protected List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {
        return staffLocationFiltersMapper.getFilterKeys().stream()
            .map(k -> "-" + k + "=")
            .collect(Collectors.toList());
    }
}
