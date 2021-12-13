<#import "report-commons.ftl" as reportcommon/>
<#import "/gui/commons/commons.ftl" as commons/>
<#import "/gui/evidence/evidence-commons.ftl" as evidenceCommons/>
<#include "/gui/commons/translate.ftl"/>

<TubingGui size="54" id="report-detail">
    <title class="gui-title"><@translate key="gui.reports.detail.title"/>${report.reporterName}</title>

    <@reportcommon.reportitem  itemId="report-info" slot=13 report=report/>

    <#if report.reportStatus.name() == 'IN_PROGRESS'>
        <#if player.uniqueId == report.staffUuid && $permissions.has(player, $config.get("permissions:reports.manage.resolve"))>
            <#list [34,35,43,44] as slot>
                <GuiItem slot="${slot}"
                         id="resolve-${slot?index}"
                         class="report-resolve"
                         material="GREEN_GLAZED_TERRACOTTA"
                         onLeftClick="manage-reports/resolve?reportId=${report.id}">
                    <name class="item-name"><@translate key="gui.reports.detail.resolve.title"/></name>
                    <Lore>
                        <LoreLine><@translate key="gui.reports.detail.resolve.lore"/></LoreLine>
                    </Lore>
                </GuiItem>
            </#list>
        </#if>

        <#if player.uniqueId == report.staffUuid || $permissions.has(player, $config.get("permissions:reports.manage.reopen-other"))>
            <#list [27,28,36,37] as slot>
                <GuiItem slot="${slot}"
                         id="unassign-${slot?index}"
                         class="report-unassign"
                         material="THIN_GLASS"
                         onLeftClick="manage-reports/reopen?reportId=${report.id}">
                    <name class="item-name"><@translate key="gui.reports.detail.unassign.title"/></name>
                    <Lore>
                        <LoreLine><@translate key="gui.reports.detail.unassign.lore"/></LoreLine>
                    </Lore>
                </GuiItem>
            </#list>
        </#if>

        <#if player.uniqueId == report.staffUuid && $permissions.has(player, $config.get("permissions:reports.manage.reject"))>
            <#list [30,31,32,39,40,41] as slot>
                <GuiItem slot="${slot}"
                         id="reject-${slot?index}"
                         material="RED_GLAZED_TERRACOTTA"
                         class="report-reject"
                         onLeftClick="manage-reports/reject?reportId=${report.id}">
                    <name class="item-name"><@translate key="gui.reports.detail.reject.title"/></name>
                    <Lore>
                        <LoreLine><@translate key="gui.reports.detail.reject.lore"/></LoreLine>
                    </Lore>
                </GuiItem>
            </#list>
        </#if>
    </#if>

    <#if $permissions.has(player, $config.get("permissions:reports.manage.delete"))>
        <GuiItem slot="8"
                 id="delete"
                 class="report-delete"
                 material="REDSTONE_BLOCK"
                 onLeftClick="manage-reports/delete?reportId=${report.id}">
            <name class="item-name"><@translate key="gui.reports.detail.delete.title"/></name>
            <Lore>
                <LoreLine><@translate key="gui.reports.detail.delete.lore"/></LoreLine>
            </Lore>
        </GuiItem>
    </#if>

    <GuiItem slot="0"
             id="teleport"
             permission="config|permissions:reports.manage.teleport"
             material="ORANGE_GLAZED_TERRACOTTA"
             class="report-teleport"
             onLeftClick="manage-reports/teleport?reportId=${report.id}">
        <name class="item-name"><@translate key="gui.reports.detail.teleport.title"/></name>
        <Lore>
            <LoreLine><@translate key="gui.reports.detail.teleport.lore"/></LoreLine>
        </Lore>
    </GuiItem>

    <@evidenceCommons.evidenceButton slot=14 evidence=report backAction=currentAction />
    <@commons.backButton action=backAction/>
</TubingGui>