<#import "/gui/commons/commons.ftl" as commons/>
<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="kick-overview">
    <title class="gui-title">Kicks</title>

    <#list kicks as kick>
        <GuiItem
            id="kick-info-${kick?index}"
            class="kick-info"
            slot="${kick?index}"
            material="PLAYER_HEAD">
        <name class="item-name" color="&6">Kick</name>
        <Lore>
            <LoreLine>
            <t color="&b" id="id-label" class="detail-label">Id: </t>
            <t color="&6" id="id-value" class="detail-value">${kick.id}</t>
            </LoreLine>

            <LoreLine if="${$config.get("server-sync-module.kick-sync")?has_content?c}">
                <t color="&b" id="server-label" class="detail-label">Server: </t>
                <t color="&6" id="server-value" class="detail-value">${kick.serverName}</t>
            </LoreLine>

            <LoreLine>
                <t color="&b" id="kickned-label" class="detail-label">Kicked player: </t>
                <t color="&6" id="kickned-value" class="detail-value">${kick.targetName}</t>
            </LoreLine>

            <LoreLine>
                <t color="&b" id="issuer-label" class="detail-label">Issuer: </t>
                <t color="&6" id="issuer-value" class="detail-value">${kick.issuerName}</t>
            </LoreLine>

            <LoreLine>
                <t color="&b" id="issued-on-label" class="detail-label">Kicked on: </t>
                <t color="&6" id="issued-on-value"
                   class="detail-value">${GuiUtils.parseTimestamp(kick.creationTimestamp, $config.get("timestamp-format"))}</t>
            </LoreLine>

            <LoreLine>
                <t color="&b" id="reason-label" class="detail-label">Reason: </t>
                <t color="&6" id="reason-value" class="detail-value">${kick.reason}</t>
            </LoreLine>
        </Lore>
    </GuiItem>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" page=page />
</TubingGui>