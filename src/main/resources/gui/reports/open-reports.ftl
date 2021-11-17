<#import "report-commons.ftl" as reportcommon/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="open-reports-overview">
    <title class="gui-title">${title}</title>

    <#list reports as report>
        <@reportcommon.reportitem
        itemId="report-info-${report?index}"
        slot="${report?index}"
        report=report
        onLeftClick="manage-reports/accept?reportId=${report.id}&backAction=${URLEncoder.encode(currentAction)}"
        onRightClick="manage-reports/accept-and-resolve?reportId=${report.id}&backAction=${URLEncoder.encode(currentAction)}"
        onMiddleClick="manage-reports/accept-and-reject?reportId=${report.id}&backAction=${URLEncoder.encode(currentAction)}"
        actions=["&7Left click to &6accept", "&7Right click to &2resolve", "&7Middle click to &Creject"]
        />
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" backAction="${backAction!}" page=page />
</TubingGui>