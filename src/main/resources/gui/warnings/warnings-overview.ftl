<#import "warning-commons.ftl" as warningCommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="warnings-overview">
    <title class="gui-title">Warnings</title>

    <#list warnings as warning>
        <GuiItem id="warning-info-${warning?index}"
                 class="warning-info"
                 slot="${warning?index}"
                 onLeftClick="manage-warnings/view/detail?warningId=${warning.id}&backAction=${URLEncoder.encode(currentAction)}"
                 material="PLAYER_HEAD"
        >
            <name class="item-name" color="&3">Warning</name>
            <Lore>
                <@warningCommons.warninglorelines warning=warning />
            </Lore>
        </GuiItem>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" backAction="${backAction!}" page=page />
</TubingGui>