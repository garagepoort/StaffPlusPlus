<#import "report-commons.ftl" as reportcommon/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="my-reports-overview">
    <title class="gui-title">My reports</title>

    <#list reports as report>
        <@reportcommon.reportitem itemId="report-info-${report?index}" slot="${report?index}" report=report />
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" page=page />
</TubingGui>