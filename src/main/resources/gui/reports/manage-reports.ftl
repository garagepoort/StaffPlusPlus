<#assign URLEncoder=statics['java.net.URLEncoder']>
<#include "/gui/commons/translate.ftl"/>
<TubingGui size="9" id="manage-reports-overview">
    <title class="gui-title"><@translate key="gui.reports.manage-reports.title"/></title>
    <GuiItem id="unresolved-reports" slot="0" material="PAPER" onLeftClick="manage-reports/view/open?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name"><@translate key="gui.reports.manage-reports.show-unresolved.title" /></name>
        <Lore>
            <LoreLine>
                <t id="unresolve-reports-label"><@translate key="gui.reports.manage-reports.show-unresolved.lore" /></t>
            </LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem id="my-reports" slot="1" material="PAPER" onLeftClick="manage-reports/view/my-assigned?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name"><@translate key="gui.reports.manage-reports.assigned-to-me.title" /></name>
        <Lore>
            <LoreLine>
                <t id="assigned-to-me-label"><@translate key="gui.reports.manage-reports.assigned-to-me.lore" /></t>
            </LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem id="assigned-reports" slot="2" material="PAPER" onLeftClick="manage-reports/view/assigned?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name"><@translate key="gui.reports.manage-reports.in-progress.title" /></name>
        <Lore>
            <LoreLine>
                <t id="assigned-label"><@translate key="gui.reports.manage-reports.in-progress.lore" /></t>
            </LoreLine>
        </Lore>
    </GuiItem>
    <GuiItem id="closed-reports" slot="3" material="PAPER" onLeftClick="manage-reports/view/closed?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name"><@translate key="gui.reports.manage-reports.closed.title" /></name>
        <Lore>
            <LoreLine>
                <t id="closed-reports-label"><@translate key="gui.reports.manage-reports.closed.lore" /></t>
            </LoreLine>
        </Lore>
    </GuiItem>
</TubingGui>