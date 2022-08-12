<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign BukkitUtils=statics['net.shortninja.staffplus.core.common.utils.BukkitUtils']>
<#import "/gui/commons/commons.ftl" as commons/>
<#include "/gui/commons/translate.ftl"/>
<#macro playerhead slot sppPlayer itemId="player-info" onRightClick="$NOOP" onLeftClick="$NOOP" onLeftShiftClick="$NOOP" actions=[]>
    <#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
    <#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>
    <#assign session=GuiUtils.getSession(sppPlayer)/>
    <GuiItem id="${itemId}"
             class="player-info"
             slot="${slot}"
             onLeftClick="${onLeftClick}"
             onRightClick="${onRightClick}"
             onLeftShiftClick="${onLeftShiftClick}"
             material="PLAYER_HEAD">
        <name class="item-name" color="&5">${sppPlayer.username}</name>
        <Lore>
            <#if sppPlayer.online || $permissions.has(player, $config.get("permissions:playerView.detail.uuid"))>
                <@commons.line />
            </#if>

            <LoreLine permission="config|permissions:playerView.detail.uuid">
                <t id="uuid-label" class="detail-label" color="&b"><@translate key="gui_player-info_uuid-label" /></t>
                <t id="uuid-value" class="detail-value" color="&6">${sppPlayer.id}</t>
            </LoreLine>

            <#if sppPlayer.online>
                <LoreLine permission="config|permissions:playerView.detail.ip">
                    <t id="ip-label" class="detail-label" color="&b"><@translate key="gui_player-info_ip-label" /></t>
                    <t id="ip-value" class="detail-value" color="&6">${BukkitUtils.getIpFromPlayer(sppPlayer.player)}</t>
                </LoreLine>
                <LoreLine permission="config|permissions:playerView.detail.gamemode">
                    <t id="gamemode-label" class="detail-label" color="&b"><@translate key="gui_player-info_gamemode-label" /></t>
                    <t id="gamemode-value" class="detail-value" color="&6">${sppPlayer.player.gameMode}</t>
                </LoreLine>
                <LoreLine permission="config|permissions:playerView.detail.food">
                    <t id="food-label" class="detail-label" color="&b"><@translate key="gui_player-info_food-label" /></t>
                    <t id="food-value" class="detail-value" color="&6">${sppPlayer.player.foodLevel}</t>
                </LoreLine>
                <LoreLine permission="config|permissions:playerView.detail.location">
                    <t id="location-label" class="detail-label" color="&b"><@translate key="gui_player-info_location-label" /></t>
                    <t id="location-value" class="detail-value" color="&6">${GuiUtils.parseLocation(sppPlayer.player.location)}</t>
                </LoreLine>
                <LoreLine permission="config|permissions:playerView.detail.timeplayed">
                    <t id="time-played-label" class="detail-label" color="&b"><@translate key="gui_player-info_time-played-label" /></t>
                    <t id="time-played-value" class="detail-value" color="&6">${GuiUtils.getTimePlayed(sppPlayer.player)}</t>
                </LoreLine>
            </#if>

            <#if session.isPresent()>
                <@commons.line />
                <LoreLine if="${session.get().frozen?c}"
                          permission="config|permissions:playerView.detail.frozen">
                    <t id="frozen-label" color="&b"><@translate key="gui_player-info_frozen-label" /></t>
                </LoreLine>
                <LoreLine if="${session.get().protected?c}"
                          permission="config|permissions:playerView.detail.protected">
                    <t id="protected-label" color="&2"><@translate key="gui_player-info_protected-label" /></t>
                </LoreLine>
                <LoreLine if="${session.get().underInvestigation?c}"
                          permission="config|permissions:playerView.detail.investigation">
                    <t id="investigation-label" color="&6"><@translate key="gui_player-info_investigation-label" /></t>
                </LoreLine>
                <LoreLine if="${session.get().muted?c}"
                          permission="config|permissions:playerView.detail.muted">
                    <t id="muted-label" color="&C"><@translate key="gui_player-info_muted-label" /></t>
                </LoreLine>
                <LoreLine if="${session.get().vanished?c}"
                          permission="config|permissions:playerView.detail.vanished">
                    <t id="vanished-label" color="&9"><@translate key="gui_player-info_vanished-label" /></t>
                </LoreLine>
            </#if>

            <@commons.line />
            <LoreLine if="${sppPlayer.online?c}">
                <t id="online-label" color="&2"><@translate key="gui_player-info_online-label" /></t>
            </LoreLine>
            <LoreLine if="${(!sppPlayer.online)?c}">
                <t id="offline-label" color="&C"><@translate key="gui_player-info_offline-label" /></t>
            </LoreLine>
            <#if actions?has_content >
                <LoreLine></LoreLine>
                <#list actions as actionLine>
                    <LoreLine>${actionLine}</LoreLine>
                </#list>
            </#if>
        </Lore>
    </GuiItem>
</#macro>