<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#macro areaitem slot area itemId="protected-area-info" onRightClick="$NOOP" onLeftClick="$NOOP" onLeftShiftClick="$NOOP" actions=[]>
    <#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
    <#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>
    <GuiItem
        id="${itemId}"
        class="protected-area-info"
        slot="${slot}"
        onLeftClick="${onLeftClick}"
        onRightClick="${onRightClick}"
        onLeftShiftClick="${onLeftShiftClick}"
        material="PAPER">
        <name class="item-name" color="&3">${area.name}</name>
        <Lore>
            <LoreLine>
                <t color="&b" id="id-label" class="detail-label">Id: </t>
                <t color="&6" id="id-value" class="detail-value">${area.id}</t>
            </LoreLine>
            <LoreLine>
                <t color="&b" id="name-label" class="detail-label">Name: </t>
                <t color="&6" id="name-value" class="detail-value">${area.name}</t>
            </LoreLine>
            <LoreLine>
                <t color="&b" id="world-label" class="detail-label">World: </t>
                <t color="&6" id="world-value" class="detail-value">${area.cornerPoint1.world.name}</t>
            </LoreLine>
            <LoreLine>
                <t color="&b" id="cornerpoint1-label" class="detail-label">Corner point 1: </t>
                <t color="&6" id="cornerpoint1-value" class="detail-value">${GuiUtils.parseLocation(area.cornerPoint1)}</t>
            </LoreLine>
            <LoreLine>
                <t color="&b" id="cornerpoint2-label" class="detail-label">Corner point 2: </t>
                <t color="&6" id="cornerpoint2-value" class="detail-value">${GuiUtils.parseLocation(area.cornerPoint2)}</t>
            </LoreLine>
        </Lore>
    </GuiItem>
</#macro>