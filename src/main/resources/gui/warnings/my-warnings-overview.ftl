<#import "warning-commons.ftl" as warningCommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#include "/gui/commons/translate.ftl"/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="my-warnings-overview">
    <title class="gui-title"><@translate key="gui.warnings.my-warnings.title"/></title>

    <#list warnings as warning>
        <GuiItem id="warning-info-${warning?index}"
                 class="warning-info"
                 slot="${warning?index}"
                 onLeftClick="manage-warnings/view/detail?warningId=${warning.id}"
                 material="SKULL_ITEM"
        >
            <name class="item-name" color="&3">Warning</name>
            <Lore>
                <@warningCommons.warninglorelines warning=warning />
            </Lore>
        </GuiItem>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}"  page=page />
</TubingGui>