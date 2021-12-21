<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#include "/gui/commons/translate.ftl"/>
<#import "/gui/commons/commons.ftl" as commons/>

<#macro appealbutton action slot="31">
    <GuiItem id="add-appeal"
             slot="${slot}"
             material="BOOK"
             onLeftClick="${action}">
        <name class="item-name">Add appeal</name>
        <Lore>
            <LoreLine>
                <t id="add-appeal-label">Click to appeal</t>
            </LoreLine>
        </Lore>
    </GuiItem>
</#macro>
<#macro appealreasonselect reasons action backAction>
    <TubingGui size="54" id="appeal-reason-select">
        <title class="gui-title">Select appeal reason</title>

        <#list reasons as reason >
            <GuiItem id="appeal-reason-${reason?index}"
                     class="appeal-reason appeal-reason-${reason}"
                     slot="${reason?index}"
                     material="PAPER"
                     onLeftClick="${action}">
                <name class="item-name">${reason}</name>
            </GuiItem>
        </#list>

        <#if backAction??>
            <@commons.backButton action=backAction/>
        </#if>
    </TubingGui>
</#macro>
<#macro appealinfoitem appeal action slot="31">
    <GuiItem slot="${slot}"
             id="appeal-info"
             material="WRITABLE_BOOK"
             onLeftClick="${action}">
        <name class="item-name">
            <t id="appeal-status-label">Appeal </t>
            <t id="appeal-status-value" color="&6">${appeal.status.name()}</t>
        </name>
        <Lore>
            <LoreLine>
                <t color="&b" id="appealer-label" class="detail-label">Appealer: </t>
                <t color="&6" id="appealer-value" class="detail-value">${appeal.appealerName}</t>
            </LoreLine>
            <LoreLine>
                <t color="&b" id="timestamp-label" class="detail-label">Timestamp: </t>
                <t color="&6" id="timestamp-value" class="detail-value">${GuiUtils.parseTimestampSeconds(appeal.creationTimestamp, $config.get("timestamp-format"))}</t>
            </LoreLine>
            <LoreLine>
                <t color="&b" id="reason-label" class="detail-label">Reason: </t>
                <t color="&6" id="reason-value" class="detail-value">${appeal.reason}</t>
            </LoreLine>
            <#if appeal.status.name() != "OPEN">
                <LoreLine>
                    <t color="&b" id="resolved-by-label" class="detail-label">Resolved by: </t>
                    <t color="&6" id="resolved-by-value" class="detail-value">${appeal.resolverName}</t>
                </LoreLine>
            </#if>
            <#if appeal.resolveReason.isPresent()>
                <LoreLine>
                    <t color="&b" id="resolve-reason-label" class="detail-label">Resolve reason: </t>
                    <t color="&6" id="resolve-reason-value" class="detail-value">${appeal.resolveReason.get()}</t>
                </LoreLine>
            </#if>
        </Lore>
    </GuiItem>
</#macro>