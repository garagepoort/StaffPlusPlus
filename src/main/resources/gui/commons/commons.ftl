<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#macro pageFooter currentAction page backAction="" backSlot=49 previousSlots=[45,46,47] nextSlots=[51,52,53]>
    <#if page gt 0 >
        <#list previousSlots as slot>
            <GuiItem slot="${slot}"
                     onLeftClick="${GuiUtils.getPreviousPage(currentAction, page)}"
                     name="Previous Page"
                     material="RED_STAINED_GLASS_PANE"/>
        </#list>
    </#if>
    <#list nextSlots as slot>
        <GuiItem slot="${slot}"
                 onLeftClick="${GuiUtils.getNextPage(currentAction, page)}"
                 name="Next Page"
                 material="GREEN_STAINED_GLASS_PANE"/>
    </#list>

    <@backButton action=backAction backSlot=backSlot/>
</#macro>

<#macro backButton action backSlot=49>
    <#if action?has_content>
        <GuiItem slot="${backSlot}"
                 onLeftClick="${action}"
                 name="Back"
                 material="SPRUCE_DOOR"/>
    </#if>
</#macro>
<#macro line>
    <LoreLine>&7---------------</LoreLine>
</#macro>