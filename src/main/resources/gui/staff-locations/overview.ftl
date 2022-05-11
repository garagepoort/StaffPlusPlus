<#import "/gui/staff-locations/staff-locations-commons.ftl" as stafflocationcommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<#assign actions = []>

<#if $permissions.has(player, $config.get("permissions:staff-locations.view-notes"))>
    <#assign actions = actions + [ '&7Left click to view notes' ] />
</#if>
<#if $permissions.has(player, $config.get("permissions:staff-locations.teleport"))>
    <#assign actions = actions + [ '&7Right click to &6teleport to location' ] />
</#if>
<#if $permissions.has(player, $config.get("permissions:staff-locations.delete"))>
    <#assign actions = actions + [ '&7Middle click to &Cdelete this location' ] />
</#if>

<TubingGui size="54" id="staff-locations-overview">
    <title class="gui-title">Saved locations</title>
    <#list locations as location>
        <@stafflocationcommons.stafflocationsitem
        itemId="staff-location-info-${location?index}"
        slot="${location?index}"
        location=location
        onMiddleClick=$permissions.has(player, $config.get("permissions:staff-locations.delete"))?then("staff-locations/view/delete?locationId=" + location.id + "&locationName=" + location.name, "$NOOP")
        onRightClick=$permissions.has(player, $config.get("permissions:staff-locations.teleport"))?then("staff-locations/teleport?locationId=" + location.id, "$NOOP")
        onLeftClick=$permissions.has(player, $config.get("permissions:staff-locations.view-notes"))?then("staff-location-notes/view?locationId=" + location.id, "$NOOP")
        actions=actions/>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}"  page=page />
</TubingGui>