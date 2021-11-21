<TubingGui size="27" id="confirmation">
    <title class="gui-title">${title}</title>

    <GuiItem id="confirmation-info"
             slot="4"
             material="BOOK">
        <name class="item-name" color="&6">Confirm</name>
        <Lore>
            <LoreLine>
                <t id="confirmation-lore">${confirmationMessage}</t>
            </LoreLine>
        </Lore>
    </GuiItem>
    <#list [9,10,18,19] as slot>
        <GuiItem id="cancel-${slot?index}"
                 slot="${slot}"
                 material="RED_GLAZED_TERRACOTTA"
                 onLeftClick="${cancelAction}">
            <name class="item-name" color="&C">Cancel</name>
        </GuiItem>
    </#list>
    <#list [16,17,25,26] as slot>
        <GuiItem id="confirm-${slot?index}"
                 slot="${slot}"
                 material="GREEN_STAINED_GLASS_PANE"
                 onLeftClick="${confirmAction}">
            <name class="item-name" color="&2">Confirm</name>
        </GuiItem>
    </#list>

</TubingGui>