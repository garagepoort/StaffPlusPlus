<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="9" id="manage-reports-overview">
    <title class="gui-title">Manage reports</title>
    <GuiItem id="unresolved-reports" slot="0" material="PAPER" onLeftClick="manage-reports/view/open?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name">Open reports</name>
        <Lore>
            <LoreLine>
                <t id="unresolve-reports-label">Show all unresolved reports</t>
            </LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem id="my-reports" slot="1" material="PAPER" onLeftClick="manage-reports/view/my-assigned?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name">Reports assigned to me</name>
        <Lore>
            <LoreLine>
                <t id="assigned-to-me-label">Show all reports assigned to me</t>
            </LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem id="assigned-reports" slot="2" material="PAPER" onLeftClick="manage-reports/view/assigned?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name">Reports in progress</name>
        <Lore>
            <LoreLine>
                <t id="assigned-label">Show all assigned</t>
            </LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem id="closed-reports" slot="3" material="PAPER" onLeftClick="manage-reports/view/closed?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name">Closed reports</name>
        <Lore>
            <LoreLine>
                <t id="closed-reports-label">Show all closed reports</t>
            </LoreLine>
        </Lore>
    </GuiItem>
</TubingGui>