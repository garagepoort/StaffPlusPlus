<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
<#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>
<#macro banitem slot ban itemId="ban-info" onRightClick="$NOOP" onLeftClick="$NOOP" onMiddleClick="$NOOP" actions=[]>
    <#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
    <#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>
    <GuiItem
        id="${itemId}"
        class="ban-info"
        slot="${slot}"
        onLeftClick="${onLeftClick}"
        onRightClick="${onRightClick}"
        onMiddleClick="${onMiddleClick}"
        material="PLAYER_HEAD">
        <name class="item-name" color="&C">Ban</name>
        <Lore>
            <@banlorelines ban=ban/>
        </Lore>
    </GuiItem>
</#macro>

<#macro banlorelines ban actions=[]>
    <LoreLine>
        <t color="&b" id="id-label" class="detail-label">Id: </t>
        <t color="&6" id="id-value" class="detail-value">${ban.id}</t>
    </LoreLine>

    <LoreLine if="${$config.get("server-sync-module.ban-sync")?has_content?c}">
        <t color="&b" id="server-label" class="detail-label">Server: </t>
        <t color="&6" id="server-value" class="detail-value">${ban.serverName}</t>
    </LoreLine>

    <LoreLine>
        <t color="&b" id="banned-label" class="detail-label">Banned player: </t>
        <t color="&6" id="banned-value" class="detail-value">${ban.targetName}</t>
    </LoreLine>

    <LoreLine>
        <t color="&b" id="issuer-label" class="detail-label">Issuer: </t>
        <t color="&6" id="issuer-value" class="detail-value">${ban.issuerName}</t>
    </LoreLine>

    <LoreLine>
        <t color="&b" id="issued-on-label" class="detail-label">Issued on: </t>
        <t color="&6" id="issued-on-value" class="detail-value">${GuiUtils.parseTimestamp(ban.creationTimestamp, $config.get("timestamp-format"))}</t>
    </LoreLine>

    <#if ban.hasEnded()>
        <LoreLine>
            <t color="&b" id="ended-on-label" class="detail-label">Ended on: </t>
            <t color="&6" id="ended-on-value" class="detail-value">${GuiUtils.parseTimestamp(ban.endTimestamp, $config.get("timestamp-format"))}</t>
        </LoreLine>
    </#if>

    <LoreLine>
        <t color="&b" id="reason-label" class="detail-label">Reason: </t>
        <t color="&6" id="reason-value" class="detail-value">${ban.reason}</t>
    </LoreLine>

    <#if ban.endTimestamp??>
        <LoreLine>
            <t color="&b" id="time-left-label" class="detail-label">Time left: </t>
            <t color="&6" id="time-left-value" class="detail-value">${ban.humanReadableDuration}</t>
        </LoreLine>
    </#if>

    <LoreLine></LoreLine>
    <LoreLine>
        <#if ban.endTimestamp??>
            <t color="&6" id="temporary-value">TEMPORARY</t>
        <#else>
            <t color="&C" id="permanent-value">PERMANENT</t>
        </#if>
    </LoreLine>
</#macro>

<#macro ipbanlorelines ipban>
    <LoreLine if="${$config.get("server-sync-module.ban-sync")?has_content?c}">
        &bServer: &6${ipban.serverName}
        <t color="&b" id="server-label" class="detail-label">Server: </t>
        <t color="&6" id="server-value" class="detail-value">${ipban.serverName}</t>
    </LoreLine>

    <LoreLine>
        <t color="&b" id="ip-rule-label" class="detail-label">Ip rule: </t>
        <t color="&6" id="ip-rule-value" class="detail-value">${ipban.ip}</t>
    </LoreLine>

    <LoreLine>
        <t color="&b" id="issuer-label" class="detail-label">Issuer: </t>
        <t color="&6" id="issuer-value" class="detail-value">${ipban.issuerName}</t>
    </LoreLine>

    <LoreLine>
        <t color="&b" id="issued-on-label" class="detail-label">Issued on: </t>
        <t color="&6" id="issued-on-value" class="detail-value">${GuiUtils.parseTimestamp(ipban.creationDate, $config.get("timestamp-format"))}</t>
    </LoreLine>

    <#if ipban.endTimestamp.isPresent()>
        <LoreLine>
            <t color="&b" id="time-left-label" class="detail-label">Time left: </t>
            <t color="&6" id="time-left-value" class="detail-value">${ipban.humanReadableDuration}</t>
        </LoreLine>
    </#if>
    <LoreLine>
        <#if ipban.endTimestamp.isPresent()>
            <t color="&6" id="temporary-value">TEMPORARY</t>
        <#else>
            <t color="&C" id="permanent-value">PERMANENT</t>
        </#if>
    </LoreLine>
</#macro>