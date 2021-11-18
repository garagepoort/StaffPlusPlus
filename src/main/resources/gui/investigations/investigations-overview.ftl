<#import "investigation-commons.ftl" as investigationCommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="investigations-overview">
    <title class="gui-title">Investigations</title>

    <#list investigations as investigation>
        <@investigationCommons.investigationItem itemId="investigation-info-${investigation?index}"
        slot="${investigation?index}"
        investigation=investigation
        onLeftClick="manage-investigations/view/detail?investigationId=${investigation.id}&backAction=${URLEncoder.encode(currentAction)}"/>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" page=page />
</TubingGui>