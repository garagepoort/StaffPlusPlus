<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
<#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>

<#macro muteitem slot mute itemId="mute-info" onRightClick="$NOOP" onLeftClick="$NOOP" onMiddleClick="$NOOP" actions=[]>
    <#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
    <#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>
    <GuiItem
        id="${itemId}"
        class="mute-info"
        slot="${slot}"
        onLeftClick="${onLeftClick}"
        onRightClick="${onRightClick}"
        onMiddleClick="${onMiddleClick}"
        material="SKULL_ITEM">
        <name class="item-name" color="&3">Mute</name>
        <Lore>
            <@mutelorelines mute=mute/>
        </Lore>
    </GuiItem>
</#macro>

<#macro mutelorelines mute>
        <LoreLine>
            <t color="&b" id="id-label" class="detail-label">Id: </t>
            <t color="&6" id="id-value" class="detail-value">${mute.id}</t>
        </LoreLine>

        <LoreLine if="${$config.get("server-sync-module.mute-sync")?has_content?c}">
            <t color="&b" id="server-label" class="detail-label">Server: </t>
            <t color="&6" id="server-value" class="detail-value">${mute.serverName}</t>
        </LoreLine>

        <LoreLine>
            <t color="&b" id="muted-player-label" class="detail-label">Muted player: </t>
            <t color="&6" id="muted-player-value" class="detail-value">${mute.targetName}</t>
        </LoreLine>

        <LoreLine>
            <t color="&b" id="issuer-label" class="detail-label">Issuer: </t>
            <t color="&6" id="issuer-value" class="detail-value">${mute.issuerName}</t>
        </LoreLine>

        <LoreLine>
            <t color="&b" id="issued-on-label" class="detail-label">Issued on: </t>
            <t color="&6" id="issued-on-value" class="detail-value">${GuiUtils.parseTimestamp(mute.creationTimestamp, $config.get("timestamp-format"))}</t>
        </LoreLine>
        <#if mute.hasEnded()>
            <LoreLine>
                <t color="&b" id="ended-on-label" class="detail-label">Ended on: </t>
                <t color="&6" id="ended-on-value" class="detail-value">${GuiUtils.parseTimestamp(mute.endTimestamp, $config.get("timestamp-format"))}</t>
            </LoreLine>
        </#if>
        <LoreLine>
            <t color="&b" id="reason-label" class="detail-label">Reason: </t>
            <t color="&6" id="reason-value" class="detail-value">${mute.reason}</t>
        </LoreLine>

        <#if mute.endTimestamp??>
            <LoreLine>
                <t color="&b" id="time-left-label" class="detail-label">Time Left: </t>
                <t color="&6" id="time-left-value" class="detail-value">${mute.humanReadableDuration}</t>
            </LoreLine>
        </#if>

        <LoreLine></LoreLine>
        <LoreLine>
            <#if mute.endTimestamp??>
                <t color="&6" id="temporary-value">TEMPORARY</t>
            <#else>
                <t color="&C" id="permanent-value">PERMANENT</t>
            </#if>
        </LoreLine>
</#macro>