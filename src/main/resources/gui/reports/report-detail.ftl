<#import "report-commons.ftl" as reportcommon/>
<#import "/gui/commons/commons.ftl" as commons/>
<#import "/gui/evidence/evidence-commons.ftl" as evidenceCommons/>
<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>

<TubingGui size="54">
    <title>Report by: ${report.reporterName}</title>

    <@reportcommon.reportitem slot=13 report=report/>

    <#if report.reportStatus.name() == 'IN_PROGRESS'>
        <#if player.uniqueId == report.staffUuid && GuiUtils.hasPermission(player, .data_model["permissions:reports.manage.resolve"])>
            <#list [34,35,43,44] as slot>
                <GuiItem slot="${slot}"
                         material="GREEN_GLAZED_TERRACOTTA"
                         name="Resolve report"
                         onLeftClick="manage-reports/resolve?reportId=${report.id}">
                    <Lore>
                        <LoreLine>Click to mark this report as resolved</LoreLine>
                    </Lore>
                </GuiItem>
            </#list>
        </#if>

        <#if player.uniqueId == report.staffUuid || GuiUtils.hasPermission(player, .data_model["permissions:reports.manage.reopen-other"])>
            <#list [27,28,36,37] as slot>
                <GuiItem slot="${slot}"
                         material="THIN_GLASS"
                         name="Unassign"
                         onLeftClick="manage-reports/reopen?reportId=${report.id}">
                    <Lore>
                        <LoreLine>Click to unassign yourself from this report</LoreLine>
                    </Lore>
                </GuiItem>
            </#list>
        </#if>

        <#if player.uniqueId == report.staffUuid && GuiUtils.hasPermission(player, .data_model["permissions:reports.manage.reject"])>
            <#list [30,31,32,39,40,41] as slot>
                <GuiItem slot="${slot}"
                         material="RED_GLAZED_TERRACOTTA"
                         name="Reject report"
                         onLeftClick="manage-reports/reject?reportId=${report.id}">
                    <Lore>
                        <LoreLine>Click to mark this report as rejected</LoreLine>
                    </Lore>
                </GuiItem>
            </#list>
        </#if>
    </#if>

    <#if player.uniqueId == report.staffUuid && GuiUtils.hasPermission(player, .data_model["permissions:reports.manage.delete"])>
        <GuiItem slot="8"
                 material="REDSTONE_BLOCK"
                 name="Delete"
                 onLeftClick="manage-reports/delete?reportId=${report.id}">
            <Lore>
                <LoreLine>Click to delete this report</LoreLine>
            </Lore>
        </GuiItem>
    </#if>

    <@evidenceCommons.evidenceButton slot=14 evidence=report backAction=currentAction />
    <@commons.backButton action=backAction/>

    <#if GuiUtils.hasPermission(player, .data_model["permissions:reports.manage.teleport"])>
        <GuiItem slot="0"
                 material="ORANGE_GLAZED_TERRACOTTA"
                 name="Teleport"
                 onLeftClick="manage-reports/teleport?reportId=${report.id}">
            <Lore>
                <LoreLine>Click to teleport to where this report was created</LoreLine>
            </Lore>
        </GuiItem>
    </#if>
</TubingGui>