<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#macro pageFooter currentAction page backAction="" backSlot=49 previousSlots=[45,46,47] nextSlots=[51,52,53]>
    <#if page gt 0 >
        <#list previousSlots as slot>
            <GuiItem slot="${slot}"
                     onLeftClick="${GuiUtils.getPreviousPage(currentAction, page)}"
                     name="Previous Page"
                     material="RED_GLAZED_TERRACOTTA"/>
        </#list>
    </#if>
    <#list nextSlots as slot>
        <GuiItem slot="${slot}"
                 onLeftClick="${GuiUtils.getNextPage(currentAction, page)}"
                 name="Next Page"
                 material="GREEN_GLAZED_TERRACOTTA"/>
    </#list>

    <@backButton action=backAction backSlot=backSlot/>
</#macro>

<#macro backButton action backSlot=49>
    <#if action?has_content>
        <GuiItem slot="${backSlot}"
                 onLeftClick="${action}"
                 name="Back"
                 material="IRON_DOOR"/>
    </#if>
</#macro>