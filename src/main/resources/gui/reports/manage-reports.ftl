<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGUi size="9">
    <title>Manage reports</title>
    <GuiItem slot="0" material="PAPER" name="Open reports" onLeftClick="manage-reports/view/open?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>Show all unresolved reports</LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem slot="1" material="PAPER" name="Reports assigned to me" onLeftClick="manage-reports/view/my-assigned?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>Show all reports assigned to me</LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem slot="2" material="PAPER" name="Open reports" onLeftClick="manage-reports/view/assigned?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>Show all assigned</LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem slot="3" material="PAPER" name="Closed reports" onLeftClick="manage-reports/view/closed?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>Show all closed reports</LoreLine>
        </Lore>
    </GuiItem>
</TubingGUi>