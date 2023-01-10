<#import "investigation-commons.ftl" as investigationCommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="investigations-resume-select">
    <title class="gui-title">Select investigation to resume</title>

    <#list investigations as investigation>
        <@investigationCommons.investigationItem itemId="investigation-info-${investigation?index}"
        slot="${investigation?index}"
        investigation=investigation
        onLeftClick="manage-investigations/resume?investigationId=${investigation.id}"/>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" page=page />
</TubingGui>