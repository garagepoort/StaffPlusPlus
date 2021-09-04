<#import "note-commons.ftl" as noteCommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGUi size="54">
    <title>${title}</title>

    <#list notes as note>
        <#assign canDelete=note.privateNote || $permissions.has(player, $config.get("permissions:player-notes.delete-other")) || ($permissions.has(player, $config.get("permissions:player-notes.delete")) && note.notedByUuid == player.uniqueId)/>

        <GuiItem slot="${note?index}"
                 name="&3Note"
                <#if canDelete>
                    onRightClick="player-notes/delete?noteId=${note.id}&backAction=${URLEncoder.encode(currentAction)}"
                </#if>
                 material="PAPER"
        >
            <Lore>
                <@noteCommons.notelorelines note=note />
                <#if canDelete>
                    <LoreLine></LoreLine>
                    <LoreLine>&7Right click to &6delete note</LoreLine>
                </#if>
            </Lore>
        </GuiItem>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" backAction="${backAction!}" page=page />
</TubingGUi>