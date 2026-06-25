package net.shortninja.staffplus.core.domain.staff.ban.ipbans.gui;

import be.garagepoort.mcioc.tubinggui.GuiAction;
import be.garagepoort.mcioc.tubinggui.GuiActionBuilder;
import be.garagepoort.mcioc.tubinggui.GuiController;
import be.garagepoort.mcioc.tubinggui.GuiParam;
import be.garagepoort.mcioc.tubinggui.model.TubingGuiActions;
import be.garagepoort.mcioc.tubinggui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.domain.player.ip.PlayerIpRecord;
import net.shortninja.staffplus.core.domain.player.ip.PlayerIpService;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBanService;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static be.garagepoort.mcioc.tubinggui.templates.GuiTemplate.template;

@GuiController
public class IpBanGuiController {

    private final IpBanService banService;
    private final PlayerIpService playerIpService;
    private final Messages messages;

    public IpBanGuiController(IpBanService banService, PlayerIpService playerIpService, Messages messages) {
        this.banService = banService;
        this.playerIpService = playerIpService;
        this.messages = messages;
    }

    @GuiAction("ipbans/ban/confirm")
    public GuiTemplate showBanConfirmationGui(@GuiParam("ipAddress") String ipAddress,
                                              @GuiParam("templateName") String templateName,
                                              @GuiParam(value = "silent", defaultValue = "false") boolean silent) {
        String confirmAction = GuiActionBuilder.builder()
            .action("ipbans/ban")
            .param("ipAddress", ipAddress)
            .param("templateName", templateName)
            .param("silent", String.valueOf(silent))
            .build();

        HashMap<String, Object> params = new HashMap<>();
        params.put("confirmationMessageLines", buildConfirmationMessage(ipAddress));
        params.put("title", "Ban IP?");
        params.put("confirmAction", confirmAction);
        params.put("cancelAction", TubingGuiActions.BACK);
        return template("gui/commons/confirmation.ftl", params);
    }

    @GuiAction("ipbans/ban")
    public void ban(Player sender,
                    @GuiParam("ipAddress") String ipAddress,
                    @GuiParam("templateName") String templateName,
                    @GuiParam(value = "silent", defaultValue = "false") boolean silent) {
        banService.banIp(sender, ipAddress, templateName, silent);
    }

    @GuiAction("ipbans/temp-ban/confirm")
    public GuiTemplate showTempBanConfirmationGui(@GuiParam("ipAddress") String ipAddress,
                                              @GuiParam("templateName") String templateName,
                                              @GuiParam("duration") long duration,
                                              @GuiParam(value = "silent", defaultValue = "false") boolean silent) {
        String confirmAction = GuiActionBuilder.builder()
            .action("ipbans/temp-ban")
            .param("ipAddress", ipAddress)
            .param("templateName", templateName)
            .param("duration", String.valueOf(duration))
            .param("silent", String.valueOf(silent))
            .build();

        HashMap<String, Object> params = new HashMap<>();
        params.put("confirmationMessageLines", buildConfirmationMessage(ipAddress));
        params.put("title", "Temp Ban IP?");
        params.put("confirmAction", confirmAction);
        params.put("cancelAction", TubingGuiActions.BACK);
        return template("gui/commons/confirmation.ftl", params);
    }

    @NotNull
    private List<String> buildConfirmationMessage(@GuiParam("ipAddress") String ipAddress) {
        List<PlayerIpRecord> players = ipAddress.contains("/") ? playerIpService.getMatchedBySubnet(ipAddress) : playerIpService.getMatchedByIp(ipAddress);
        List<String> messageLines = new ArrayList<>();
        messageLines.add(messages.get("ipbans.ban-confirmation-matching-players"));
        for (int i = 0; i < players.size(); i++) {
            PlayerIpRecord player = players.get(i);
            messageLines.add(messages.get("ipbans.list-entry", "%count%", Integer.toString(i + 1), "%value%", player.getPlayerName()));
        }
        return messageLines;
    }

    @GuiAction("ipbans/temp-ban")
    public void tempBan(Player sender,
                    @GuiParam("ipAddress") String ipAddress,
                    @GuiParam("templateName") String templateName,
                    @GuiParam("duration") long duration,
                    @GuiParam(value = "silent", defaultValue = "false") boolean silent) {
        banService.tempBanIp(sender, ipAddress, templateName, duration, silent);
    }
}
