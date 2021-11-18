<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
<#macro investigationItem slot investigation itemId="investigation-info" onRightClick="$NOOP" onLeftClick="$NOOP" onMiddleClick="$NOOP">
    <GuiItem
        id="${itemId}"
        class="investigation-info"
        slot="${slot}"
        onLeftClick="${onLeftClick}"
        onRightClick="${onRightClick}"
        onMiddleClick="${onMiddleClick}"
        material="BOOK">
        <name class="item-name" color="&7">
            Investigation
        </name>
        <Lore>
            <LoreLine>
                <t color="&b" id="id-label" class="detail-label">Id: </t>
                <t color="&6" id="id-value" class="detail-value">${investigation.id}</t>
            </LoreLine>

            <LoreLine>
                <t color="&b" id="investigator-label" class="detail-label">Investigator: </t>
                <t color="&6" id="investigator-value" class="detail-value">${investigation.investigatorName}</t>
            </LoreLine>

            <LoreLine>
                <t color="&b" id="status-label" class="detail-label">Status: </t>
                <t color="&6" id="status-value" class="detail-value">${investigation.status.name()}</t>
            </LoreLine>

            <LoreLine>
                <t color="&b" id="starttime-label" class="detail-label">Start time: </t>
                <t color="&6" id="starttime-value" class="detail-value">${GuiUtils.parseTimestamp(investigation.creationTimestamp, $config.get("timestamp-format"))}</t>
            </LoreLine>

            <#if investigation.investigatedName.isPresent() == true >
                <LoreLine>
                    <t color="&b" id="investigated-label" class="detail-label">Investigated: </t>
                    <t color="&6" id="investigated-value" class="detail-value">${investigation.investigatedName.get()}</t>
                </LoreLine>
            <#else>
                <LoreLine>
                    <t color="&b" id="investigated-label" class="detail-label">Investigated: </t>
                    <t color="&6" id="investigated-value" class="detail-value">Unknown</t>
                </LoreLine>
            </#if>
        </Lore>
    </GuiItem>
</#macro>