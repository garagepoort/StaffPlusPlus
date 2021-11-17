<#import "report-commons.ftl" as reportcommon/>
<#import "/gui/commons/commons.ftl" as commons/>
<#import "/gui/evidence/evidence-commons.ftl" as evidenceCommons/>

<TubingGui size="54" id="report-detail">
    <title class="gui-title">Report by: ${report.reporterName}</title>

    <@reportcommon.reportitem  itemId="report-info" slot=13 report=report/>

    <#if report.reportStatus.name() == 'IN_PROGRESS'>
        <#if player.uniqueId == report.staffUuid && $permissions.has(player, $config.get("permissions:reports.manage.resolve"))>
            <#list [34,35,43,44] as slot>
                <GuiItem slot="${slot}"
                         id="resolve-${slot?index}"
                         material="GREEN_STAINED_GLASS_PANE"
                         onLeftClick="manage-reports/resolve?reportId=${report.id}">
                    <name class="item-name">Resolve report</name>
                    <Lore>
                        <LoreLine>Click to mark this report as resolved</LoreLine>
                    </Lore>
                </GuiItem>
            </#list>
        </#if>

        <#if player.uniqueId == report.staffUuid || $permissions.has(player, $config.get("permissions:reports.manage.reopen-other"))>
            <#list [27,28,36,37] as slot>
                <GuiItem slot="${slot}"
                         id="unassign-${slot?index}"
                         material="WHITE_STAINED_GLASS_PANE"
                         onLeftClick="manage-reports/reopen?reportId=${report.id}">
                    <name class="item-name">Unassign</name>
                    <Lore>
                        <LoreLine>Click to unassign yourself from this report</LoreLine>
                    </Lore>
                </GuiItem>
            </#list>
        </#if>

        <#if player.uniqueId == report.staffUuid && $permissions.has(player, $config.get("permissions:reports.manage.reject"))>
            <#list [30,31,32,39,40,41] as slot>
                <GuiItem slot="${slot}"
                         id="reject-${slot?index}"
                         material="RED_STAINED_GLASS_PANE"
                         onLeftClick="manage-reports/reject?reportId=${report.id}">
                    <name class="item-name">Reject report</name>
                    <Lore>
                        <LoreLine>Click to mark this report as rejected</LoreLine>
                    </Lore>
                </GuiItem>
            </#list>
        </#if>
    </#if>

    <#if $permissions.has(player, $config.get("permissions:reports.manage.delete"))>
        <GuiItem slot="8"
                 id="delete"
                 material="REDSTONE_BLOCK"
                 onLeftClick="manage-reports/delete?reportId=${report.id}">
            <name class="item-name">Delete</name>
            <Lore>
                <LoreLine>Click to delete this report</LoreLine>
            </Lore>
        </GuiItem>
    </#if>

    <GuiItem slot="0"
             id="teleport"
             permission="config|permissions:reports.manage.teleport"
             material="ORANGE_STAINED_GLASS_PANE"
             onLeftClick="manage-reports/teleport?reportId=${report.id}">
        <name class="item-name">Teleport</name>
        <Lore>
            <LoreLine>Click to teleport to where this report was created</LoreLine>
        </Lore>
    </GuiItem>

    <@evidenceCommons.evidenceButton slot=14 evidence=report backAction=currentAction />
    <@commons.backButton action=backAction/>
</TubingGui>