<#import "report-commons.ftl" as reportcommon/>
<#import "/gui/commons/commons.ftl" as commons/>
<#include "/gui/commons/translate.ftl"/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="my-reports-overview">
    <title class="gui-title"><@translate key="gui.reports.my-reports.title"/></title>

    <#list reports as report>
        <@reportcommon.reportitem itemId="report-info-${report?index}" slot="${report?index}" report=report />
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" page=page />
</TubingGui>