<#import "report-commons.ftl" as reportcommon/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGUi size="54">
    <title>${title}</title>

    <#list reports as report>
        <@reportcommon.reportitem slot="${report?index}" report=report action="manage-reports/view/detail?reportId=${report.id}&backAction=${URLEncoder.encode(currentAction)}" />
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" page=page />
</TubingGUi>