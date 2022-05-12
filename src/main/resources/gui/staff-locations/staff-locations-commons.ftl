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
            <LoreLine>
                <t color="&b" id="created-on-label" class="detail-label">Created on: </t>
                <t color="&6" id="created-on-value" class="detail-value">${GuiUtils.parseTimestamp(location.creationTimestamp, $config.get("timestamp-format"))}</t>
            </LoreLine>
            <#if location.newestNote.isPresent()>
                <LoreLine>
                    <t color="&b" id="note-label" class="detail-label">Newest note:</t>
                </LoreLine>
                <#list JavaUtils.formatLines(location.newestNote.get().note, 30) as reasonLine>
                    <LoreLine>
                        <t color="&6" id="note-value" class="detail-value">   ${reasonLine}</t>
                    </LoreLine>
                </#list>
            </#if>
            <#if actions?has_content >
                <LoreLine></LoreLine>
                <#list actions as actionLine>
                    <LoreLine>${actionLine}</LoreLine>
                </#list>
            </#if>
        </Lore>
    </GuiItem>
</#macro>