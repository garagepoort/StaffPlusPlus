<#import "/gui/staff-locations/staff-locations-commons.ftl" as stafflocationcommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<#assign actions = []>

<#assign actions = actions + [ '&7Right click to view notes and options' ] />
<#if $permissions.has(player, $config.get("permissions:staff-locations.teleport"))>
    <#assign actions = actions + [ '&7Left click to &6teleport to location' ] />
</#if>
<#if $permissions.has(player, $config.get("permissions:staff-locations.delete"))>
    <#assign actions = actions + [ '&7Shift click to &Cdelete this location' ] />
</#if>

<TubingGui size="54" id="staff-locations-overview">
    <title class="gui-title">Saved locations</title>
    <#list locations as location>
        <@stafflocationcommons.stafflocationsitem
        itemId="staff-location-info-${location?index}"
        slot="${location?index}"
        location=location
        onLeftClick=$permissions.has(player, $config.get("permissions:staff-locations.teleport"))?then("staff-locations/teleport?locationId=" + location.id, "$NOOP")
        onLeftShiftClick=$permissions.has(player, $config.get("permissions:staff-locations.delete"))?then("staff-locations/view/delete-confirmation?locationId=" + location.id + "&locationName=" + URLEncoder.encode(location.name), "$NOOP")
        onRightClick="staff-locations/view/detail?locationId=${location.id}"
        actions=actions/>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}"  page=page />

    <#if $permissions.has(player, $config.get("permissions:staff-locations.create"))>
        <GuiItem
            id="staff-location-create"
            slot="50"
            onLeftClick="staff-locations/create-flow/select-name"
            material="BOOK"
        >
            <name class="item-name" color="&7">Save this location</name>
        </GuiItem>
    </#if>
</TubingGui>