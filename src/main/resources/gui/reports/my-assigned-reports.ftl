<#import "report-commons.ftl" as reportcommon/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<#include "/gui/commons/translate.ftl"/>
<TubingGui size="54" id="my-assigned-reports-overview">
    <title class="gui-title"><@translate key="gui.reports.my-assigned-reports.title"/></title>

    <#list reports as report>
        <@reportcommon.reportitem
        itemId="report-info-${report?index}"
        slot="${report?index}"
        report=report
        onLeftClick="manage-reports/view/detail?reportId=${report.id}&backAction=${URLEncoder.encode(currentAction)}"
        onRightClick="manage-reports/resolve?reportId=${report.id}&backAction=${URLEncoder.encode(currentAction)}"
        onMiddleClick="manage-reports/reject?reportId=${report.id}&backAction=${URLEncoder.encode(currentAction)}"
        actions=["gui.reports.my-assigned-reports.left-click-to-detail", "gui.reports.my-assigned-reports.right-click-to-resolve", "gui.reports.my-assigned-reports.middle-click-to-reject"]
        />
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" backAction="${backAction!}" page=page />
</TubingGui>