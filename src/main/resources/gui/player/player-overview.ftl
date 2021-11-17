<#import "player-commons.ftl" as playercommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="players-overview">
    <title class="gui-title">${title}</title>

    <#list players as p>
        <@playercommons.playerhead itemId="player-info-${p?index}" slot="${p?index}" sppPlayer=p
        onLeftClick="players/view/detail?targetPlayerName=${p.username}&backAction=${URLEncoder.encode(currentAction)}"
        actions=['&7Left click to &6view players details'] />
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" backAction="${backAction!}" page=page />
</TubingGui>