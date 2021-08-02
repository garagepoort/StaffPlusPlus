<#import "report-commons.ftl" as reportcommon/>
<#import "/gui/commons/commons.ftl" as commons/>
<TubingGUi size="54">
    <title>${title}</title>

    <#list reports as report>
        <@reportcommon.reportitem slot="${report?index}" report=report action="manage-reports/accept?reportId=${report.id}" />
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" backAction="${backAction}" page=page />
</TubingGUi>