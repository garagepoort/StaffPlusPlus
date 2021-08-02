<#import "report-commons.ftl" as reportcommon/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGUi size="54">
    <title>My reports</title>

    <#list reports as report>
        <@reportcommon.reportitem slot="${report?index}" report=report />
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" page=page />
</TubingGUi>