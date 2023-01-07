<#import "report-commons.ftl" as reportcommon/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<#include "/gui/commons/translate.ftl"/>
<TubingGui size="54" id="open-reports-overview">
    <title class="gui-title"><@translate key="gui.reports.open-reports.title"/></title>

    <#list reports as report>
        <@reportcommon.reportitem
        itemId="report-info-${report?index}"
        slot="${report?index}"
        report=report
        onLeftClick="manage-reports/accept?reportId=${report.id}"
        onRightClick="manage-reports/accept-and-resolve?reportId=${report.id}"
        onLeftShiftClick="manage-reports/accept-and-reject?reportId=${report.id}"
        actions=["gui.reports.open-reports.left-click-to-accept", "gui.reports.open-reports.right-click-to-resolve", "gui.reports.open-reports.left-shift-click-to-reject"]
        />
    </#list>

    <@commons.pageFooter currentAction="${currentAction}"  page=page />
</TubingGui>