<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#macro stafflocationsitem slot location itemId="staff-location-info" onRightClick="$NOOP" onLeftClick="$NOOP" onMiddleClick="$NOOP" actions=[]>
    <#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
    <#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>
    <GuiItem
        id="${itemId}"
        class="staff-location-info"
        slot="${slot}"
        onLeftClick="${onLeftClick}"
        onRightClick="${onRightClick}"
        onMiddleClick="${onMiddleClick}"
        material="PAPER">
        <name class="item-name" color="&3">${location.name}</name>
        <Lore>
            <LoreLine>
                <t color="&b" id="id-label" class="detail-label">Id: </t>
                <t color="&6" id="id-value" class="detail-value">${location.id}</t>
            </LoreLine>
            <LoreLine>
                <t color="&b" id="name-label" class="detail-label">Name: </t>
                <t color="&6" id="name-value" class="detail-value">${location.name}</t>
            </LoreLine>
            <LoreLine>
                <t color="&b" id="world-label" class="detail-label">Location: </t>
                <t color="&6" id="world-value" class="detail-value">${GuiUtils.parseLocation(location.location)}</t>
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