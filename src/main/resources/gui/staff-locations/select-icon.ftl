<#import "/gui/staff-locations/staff-locations-commons.ftl" as stafflocationcommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>

<TubingGui size="54" id="staff-locations-overview">
    <title class="gui-title">Select location Icon</title>
    <#list icons as icon>
        <GuiItem
            itemId="staff-location-item-select-${icon?index}"
            slot="${icon?index}"
            class="staff-location-item-select"
            onLeftClick="staff-locations/create-flow/save?locationName=${URLEncoder.encode(locationName)}&locationIcon=${icon.material}"
            material="${icon.material}">
            <name class="item-name" color="&3">${icon.material}</name>
            <Lore>
                <#if icon.iconText??>
                    <LoreLine><t color="&6">${icon.iconText}</t></LoreLine>
                    <LoreLine></LoreLine>
                </#if>
                <LoreLine>
                    Click to select this icon
                </LoreLine>
            </Lore>
        </GuiItem>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}"  page=page />
</TubingGui>