<#import "mute-commons.ftl" as muteCommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="${guiId}">
    <title class="gui-title">${title}</title>

    <#list mutes as mute>
        <GuiItem id="mute-info-${mute?index}"
                 class="mute-info"
                 slot="${mute?index}"
                 onLeftClick="manage-mutes/view/detail?muteId=${mute.id}&backAction=${URLEncoder.encode(currentAction)}"
                 material="PLAYER_HEAD"
        >
            <name class="item-name" color="&3">Mute</name>
            <Lore>
                <@muteCommons.mutelorelines mute=mute />
            </Lore>
        </GuiItem>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" backAction="${backAction!}" page=page />
</TubingGui>