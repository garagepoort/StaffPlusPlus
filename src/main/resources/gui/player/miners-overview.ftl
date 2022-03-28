<#import "player-commons.ftl" as playercommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="miners-overview">
    <title class="gui-title">${$config.get("staffmode-modules:modules.gui-module.miner-title")}</title>

    <#list players as p>
        <@playercommons.playerhead itemId="player-info-${p?index}" slot="${p?index}" sppPlayer=p
        onLeftClick="miners/teleport?to=${p.id}"
        actions=['&7Left click to &6teleport to player'] />
    </#list>

    <@commons.pageFooter currentAction="${currentAction}"  page=page />
</TubingGui>