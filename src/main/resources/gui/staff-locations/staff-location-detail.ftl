<#import "/gui/commons/commons.ftl" as commons/>
<#import "/gui/staff-locations/staff-locations-commons.ftl" as stafflocationcommons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>

<TubingGui size="54" id="staff-location-detail">
    <title class="gui-title">Manage Staff location</title>

    <@stafflocationcommons.stafflocationsitem  itemId="location-info" slot=13 location=location/>

    <#if $permissions.has(player, $config.get("permissions:staff-locations.view-notes"))>
        <GuiItem slot="11"
                 id="notes-overview"
                 material="PAPER"
                 onLeftClick="staff-location-notes/view?locationId=${location.id}">
            <name class="item-name">Notes</name>
            <Lore>
                <LoreLine>
                    <t id="notes-overview-label">Go to notes overview</t>
                </LoreLine>
            </Lore>
        </GuiItem>
    </#if>

    <#if $permissions.has(player, $config.get("permissions:staff-locations.edit"))>
        <GuiItem
            id="staff-location-options"
            slot="15"
            onLeftClick="staff-locations/view/options?locationId=${location.id}"
            material="NETHER_STAR"
        >
            <name class="item-name" color="&7">Edit</name>
        </GuiItem>
    </#if>

    <#if $permissions.has(player, $config.get("permissions:staff-locations.delete"))>
        <#list [30,31,32,39,40,41] as slot>
            <GuiItem slot="${slot}"
                     id="resume-${slot?index}"
                     material="RED_STAINED_GLASS_PANE"
                     onLeftClick="staff-locations/view/delete-confirmation?locationId=${location.id}&locationName=${URLEncoder.encode(location.name)}">
                <name class="item-name">Delete</name>
                <Lore>
                    <LoreLine>
                        <t id="resume-action-label">Click to delete this location</t>
                    </LoreLine>
                </Lore>
            </GuiItem>
        </#list>
    </#if>

    <@commons.backButton/>
</TubingGui>