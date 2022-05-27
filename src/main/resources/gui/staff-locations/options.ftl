<#import "/gui/commons/commons.ftl" as commons/>
<TubingGui size="27" id="staff-location-options-overview">
    <title class="gui-title">Staff location options</title>
    <GuiItem
        id="staff-location-edit-name"
        slot="2"
        onLeftClick="staff-locations/edit/name?locationId=${locationId}"
        material="SIGN">
            <name class="item-name" color="&7">Edit location name</name>
    </GuiItem>
    <GuiItem
        id="staff-location-edit-icon"
        slot="4"
        onLeftClick="staff-locations/edit-flow/select-icon?locationId=${locationId}"
        material="DIRT">
            <name class="item-name" color="&7">Edit Icon</name>
    </GuiItem>
    <GuiItem
        id="staff-location-edit-location"
        slot="6"
        onLeftClick="staff-locations/edit/location?locationId=${locationId}"
        material="ENDER_PEARL">
            <name class="item-name" color="&7">Set location to here</name>
    </GuiItem>
    <@commons.backButton backSlot=22/>
</TubingGui>