<#import "protected-areas-commons.ftl" as areacommon/>
<#import "/gui/commons/commons.ftl" as commons/>

<TubingGui size="54" id="area-detail">
    <title class="gui-title">Protected area: ${area.name}</title>

    <@areacommon.areaitem slot=13 area=area/>

    <#list [30,31,32,39,40,41] as slot>
        <GuiItem slot="${slot}"
                 id="delete-${slot}"
                 class="protected-area-delete"
                 material="RED_GLAZED_TERRACOTTA"
                 onLeftClick="protected-areas/delete?areaId=${area.id}">
            <name class="item-name">Delete protected area</name>
            <Lore>
                <LoreLine>Click to delete this area</LoreLine>
            </Lore>
        </GuiItem>
    </#list>

    <#list [34,35,43,44] as slot>
        <GuiItem slot="${slot}"
                 id="teleport-${slot}"
                 class="protected-area-teleport"
                 material="ORANGE_GLAZED_TERRACOTTA"
                 onLeftClick="protected-areas/teleport?areaId=${area.id}">
            <name class="item-name">Teleport</name>
            <Lore>
                <LoreLine>Click to teleport to where this area was created</LoreLine>
            </Lore>
        </GuiItem>
    </#list>

    <#if backAction??>
        <@commons.backButton action=backAction/>
    </#if>
</TubingGui>