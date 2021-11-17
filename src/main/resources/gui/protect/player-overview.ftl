<#import "/gui/player/player-commons.ftl" as playercommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="protected-players-overview">
    <title class="gui-title">Protected players</title>

    <#list players as p>
        <@playercommons.playerhead itemId="player-info-${p?index}" slot="${p?index}" sppPlayer=p onLeftClick="players/view/detail?targetPlayerName=${p.username}&backAction=${URLEncoder.encode(currentAction)}" />
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" backAction="${backAction!}" page=page />
</TubingGui>