<#import "report-commons.ftl" as reportcommon/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<#include "/gui/commons/translate.ftl"/>
<TubingGui size="54" id="find-reports-overview">
    <title class="gui-title"><@translate key="gui.reports.find-reports.title"/></title>

    <#list reports as report>
        <@reportcommon.reportitem
        itemId="report-info-${report?index}"
        slot="${report?index}" report=report onLeftClick="manage-reports/view/detail?reportId=${report.id}&backAction=${URLEncoder.encode(currentAction)}" />
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" backAction=backAction page=page />
</TubingGui>