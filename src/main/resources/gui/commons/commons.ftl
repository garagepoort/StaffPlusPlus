<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#macro pageFooter currentAction page backAction="" backSlot=49 previousSlots=[45,46,47] nextSlots=[51,52,53]>
    <#if page gt 0 >
        <#list previousSlots as slot>
            <GuiItem slot="${slot}"
                     id="previous-page-${slot?index}"
                     onLeftClick="${GuiUtils.getPreviousPage(currentAction, page)}"
                     class="previous-page"
                     material="RED_GLAZED_TERRACOTTA">
                <name class="item-name">Previous Page</name>
            </GuiItem>
        </#list>
    </#if>
    <#list nextSlots as slot>
        <GuiItem slot="${slot}"
                 id="next-page-${slot?index}"
                 onLeftClick="${GuiUtils.getNextPage(currentAction, page)}"
                 class="next-page"
                 material="GREEN_GLAZED_TERRACOTTA">
            <name class="item-name">Next Page</name>
        </GuiItem>
    </#list>

    <@backButton action=backAction backSlot=backSlot/>
</#macro>

<#macro backButton action backSlot=49>
    <#if action?has_content>
        <GuiItem slot="${backSlot}"
                 id="back-button"
                 class="back-button"
                 onLeftClick="${action}"
                 material="IRON_DOOR">
            <name class="item-name">Back</name>
        </GuiItem>
    </#if>
</#macro>
<#macro line>
    <LoreLine>
        <t color="&7" class="line-separator">---------------</t>
    </LoreLine>
</#macro>