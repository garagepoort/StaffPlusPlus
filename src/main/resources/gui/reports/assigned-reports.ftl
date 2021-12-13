<#import "report-commons.ftl" as reportcommon/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<#include "/gui/commons/translate.ftl"/>
<TubingGui size="54" id="assigned-reports-overview">
    <title class="gui-title"><@translate key="reports.gui.assigned-reports.title"/></title>

    <#list reports as report>
        <@reportcommon.reportitem slot="${report?index}"
        itemId="report-info-${report?index}"
        report=report
        onLeftClick="manage-reports/view/detail?reportId=${report.id}&backAction=${URLEncoder.encode(currentAction)}"/>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" backAction="${backAction!}" page=page />
</TubingGui>