<#import "note-commons.ftl" as noteCommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="note-overview">
    <title class="gui-title">${title}</title>

    <#list notes as note>
        <#assign isCreator=note.notedByUuid == player.uniqueId/>
        <#assign canDelete=
        (note.privateNote && isCreator)
        || $permissions.has(player, $config.get("permissions:player-notes.delete-other"))
        || ($permissions.has(player, $config.get("permissions:player-notes.delete")) && isCreator)/>

        <GuiItem id="note-info-${note?index}"
                 class="note-info"
                 slot="${note?index}"
                <#if canDelete>
                    onRightClick="player-notes/delete?noteId=${note.id}&backAction=${URLEncoder.encode(currentAction)}"
                </#if>
                 material="PAPER"
        >
            <name class="item-name" color="&3">Note</name>
            <Lore>
                <@noteCommons.notelorelines note=note />
                <#if canDelete>
                    <LoreLine></LoreLine>
                    <LoreLine>&7Right click to &Cdelete note</LoreLine>
                </#if>
            </Lore>
        </GuiItem>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" backAction="${backAction!}" page=page />
</TubingGui>