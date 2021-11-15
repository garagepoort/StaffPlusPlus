<#import "mute-commons.ftl" as muteCommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54">
    <title>${title}</title>

    <#list mutes as mute>
        <GuiItem slot="${mute?index}"
                 name="&3Mute"
                 onLeftClick="manage-mutes/view/detail?muteId=${mute.id}&backAction=${URLEncoder.encode(currentAction)}"
                 material="PLAYER_HEAD"
        >
            <Lore>
                <@muteCommons.mutelorelines mute=mute />
            </Lore>
        </GuiItem>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" backAction="${backAction!}" page=page />
</TubingGui>