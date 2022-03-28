<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#include "/gui/commons/translate.ftl"/>
<#macro pageFooter currentAction page backSlot=49 previousSlots=[45,46,47] nextSlots=[51,52,53]>
    <#if page gt 0 >
        <#list previousSlots as slot>
            <GuiItem slot="${slot}"
                     id="previous-page-${slot?index}"
                     onLeftClick="${GuiUtils.getPreviousPage(currentAction, page)}"
                     class="previous-page"
                     material="RED_GLAZED_TERRACOTTA">
                <name class="item-name"><@translate key="gui.previous-page"/></name>
            </GuiItem>
        </#list>
    </#if>
    <#list nextSlots as slot>
        <GuiItem slot="${slot}"
                 id="next-page-${slot?index}"
                 onLeftClick="${GuiUtils.getNextPage(currentAction, page)}"
                 class="next-page"
                 material="GREEN_GLAZED_TERRACOTTA">
            <name class="item-name"><@translate key="gui.next-page"/></name>
        </GuiItem>
    </#list>

    <@backButton backSlot=backSlot/>
</#macro>

<#macro backButton backSlot=49>
    <GuiItem slot="${backSlot}"
             id="back-button"
             class="back-button"
             onLeftClick="$$back"
             material="IRON_DOOR">
        <name class="item-name"><@translate key="gui.back"/></name>
    </GuiItem>
</#macro>
<#macro line>
    <LoreLine>
        <t color="&7" class="line-separator">---------------</t>
    </LoreLine>
</#macro>