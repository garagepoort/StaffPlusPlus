<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign BukkitUtils=statics['net.shortninja.staffplus.core.common.utils.BukkitUtils']>
<#import "/gui/commons/commons.ftl" as commons/>
<#macro playerhead slot sppPlayer itemId="player-info" onRightClick="$NOOP" onLeftClick="$NOOP" onMiddleClick="$NOOP" actions=[]>
    <#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
    <#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>
    <#assign session=GuiUtils.getSession(sppPlayer)/>
    <GuiItem id="${itemId}"
             class="player-info"
             slot="${slot}"
             onLeftClick="${onLeftClick}"
             onRightClick="${onRightClick}"
             onMiddleClick="${onMiddleClick}"
             material="SKULL_ITEM">
        <name class="item-name" color="&5">${sppPlayer.username}</name>
        <Lore>
            <#if sppPlayer.online || $permissions.has(player, $config.get("permissions:playerView.detail.uuid"))>
                <@commons.line />
            </#if>

            <LoreLine permission="config|permissions:playerView.detail.uuid">
                <t id="uuid-label" class="detail-label" color="&b">UUID: </t>
                <t id="uuid-value" class="detail-value" color="&6">${sppPlayer.id}</t>
            </LoreLine>

            <#if sppPlayer.online>
                <LoreLine permission="config|permissions:playerView.detail.ip">
                    <t id="ip-label" class="detail-label" color="&b">Ip: </t>
                    <t id="ip-value" class="detail-value" color="&6">${BukkitUtils.getIpFromPlayer(sppPlayer.player)}</t>
                </LoreLine>
                <LoreLine permission="config|permissions:playerView.detail.gamemode">
                    <t id="gamemode-label" class="detail-label" color="&b">Gamemode: </t>
                    <t id="gamemode-value" class="detail-value" color="&6">${sppPlayer.player.gameMode}</t>
                </LoreLine>
                <LoreLine permission="config|permissions:playerView.detail.food">
                    <t id="food-label" class="detail-label" color="&b">Food: </t>
                    <t id="food-value" class="detail-value" color="&6">${sppPlayer.player.foodLevel}</t>
                </LoreLine>
                <LoreLine permission="config|permissions:playerView.detail.location">
                    <t id="location-label" class="detail-label" color="&b">Location: </t>
                    <t id="location-value" class="detail-value" color="&6">${GuiUtils.parseLocation(sppPlayer.player.location)}</t>
                </LoreLine>
                <LoreLine permission="config|permissions:playerView.detail.timeplayed">
                    <t id="time-played-label" class="detail-label" color="&b">Time played: </t>
                    <t id="time-played-value" class="detail-value" color="&6">${GuiUtils.getTimePlayed(sppPlayer.player)}</t>
                </LoreLine>
            </#if>

            <#if session.isPresent()>
                <@commons.line />
                <LoreLine if="${session.get().frozen?c}"
                          permission="config|permissions:playerView.detail.frozen">
                    <t id="frozen-label" color="&b">Frozen</t>
                </LoreLine>
                <LoreLine if="${session.get().protected?c}"
                          permission="config|permissions:playerView.detail.protected">
                    <t id="protected-label" color="&2">Protected</t>
                </LoreLine>
                <LoreLine if="${session.get().underInvestigation?c}"
                          permission="config|permissions:playerView.detail.investigation">
                    <t id="investigation-label" color="&6">Under investigation</t>
                </LoreLine>
                <LoreLine if="${session.get().muted?c}"
                          permission="config|permissions:playerView.detail.muted">
                    <t id="vanished-label" color="&C">Muted</t>
                </LoreLine>
                <LoreLine if="${session.get().vanished?c}"
                          permission="config|permissions:playerView.detail.vanished">
                    <t id="vanished-label" color="&9">Vanished</t>
                </LoreLine>
            </#if>

            <@commons.line />
            <LoreLine if="${sppPlayer.online?c}">
                <t id="online-label" color="&2">ONLINE</t>
            </LoreLine>
            <LoreLine if="${(!sppPlayer.online)?c}">
                <t id="offline-label" color="&C">OFFLINE</t>
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