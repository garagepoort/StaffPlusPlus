<#import "report-commons.ftl" as reportcommon/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGUi size="54">
    <title>Reports assigned to you</title>

    <#list reports as report>
        <@reportcommon.reportitem slot="${report?index}"
        report=report
        onLeftClick="manage-reports/view/detail?reportId=${report.id}&backAction=${URLEncoder.encode(currentAction)}"
        onRightClick="manage-reports/resolve?reportId=${report.id}&backAction=${URLEncoder.encode(currentAction)}"
        onMiddleClick="manage-reports/reject?reportId=${report.id}&backAction=${URLEncoder.encode(currentAction)}"
        actions=["&7Left click to &6view detail", "&7Right click to &2resolve", "&7Middle click to &Creject"]
        />
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" backAction="${backAction!}" page=page />
</TubingGUi>