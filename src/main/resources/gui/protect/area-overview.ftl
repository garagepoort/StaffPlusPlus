<#import "/gui/protect/protected-areas-commons.ftl" as areacommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="protected-areas-overview">
    <title class="gui-title">${title}</title>

    <#list areas as area>
        <@areacommons.areaitem
        itemId="protected-area-info-${area?index}"
        slot="${area?index}"
        area=area
        onLeftClick="protected-areas/view/detail?areaId=${area.id}&backAction=${URLEncoder.encode(currentAction)}" />
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" backAction="${backAction!}" page=page />
</TubingGui>