<#import "mute-commons.ftl" as muteCommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="mutes-overview">
    <title class="gui-title">Appealed mutes</title>

    <#list mutes as mute>
        <@muteCommons.muteitem itemId="mute-info-${mute?index}"
        slot="${mute?index}"
        mute=mute
        onLeftClick="manage-mutes/view/detail?muteId=${mute.id}&backAction=${URLEncoder.encode(currentAction)}"/>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" page=page />
</TubingGui>