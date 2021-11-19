<#import "investigation-commons.ftl" as investigationCommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>
<TubingGui size="54" id="investigation-notes-overview">
    <title class="gui-title">Investigation notes</title>

    <#list notes as note>
        <GuiItem
            id="investigation-note-info-${note?index}"
            class="investigation-note-info"
            slot="${note?index}"
            onRightClick="manage-investigation-notes/view/delete?noteId=${note.id}&investigationId=${investigationId}&backAction=${URLEncoder.encode(currentAction)}"
            material="PAPER">
            <name class="item-name" color="&C">NOTE: ${note.id}</name>
            <Lore>
                <LoreLine>
                    <t color="&b" id="id-label" class="detail-label">Id: </t>
                    <t color="&6" id="id-value" class="detail-value">${note.id}</t>
                </LoreLine>
                <LoreLine>
                    <t color="&b" id="noted-by-label" class="detail-label">Noted by: </t>
                    <t color="&6" id="noted-by-value" class="detail-value">${note.notedByName}</t>
                </LoreLine>
                <LoreLine>
                    <t color="&b" id="noted-on-label" class="detail-label">Noted on: </t>
                    <t color="&6" id="noted-on-value"
                       class="detail-value">${GuiUtils.parseTimestampSeconds(note.creationTimestamp, $config.get("timestamp-format"))}</t>
                </LoreLine>
                <LoreLine>
                    <t color="&b" id="note-label" class="detail-label">Note: </t>
                </LoreLine>
                <#list JavaUtils.formatLines(note.note, 30) as reasonLine>
                    <LoreLine>
                        <t color="&6" id="note-value" class="detail-value">   ${reasonLine}</t>
                    </LoreLine>
                </#list>

                <LoreLine></LoreLine>

                <LoreLine>
                    <t id="delete-action-label" color="&C">Right click to delete note</t>
                </LoreLine>
            </Lore>
        </GuiItem>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}"  backAction="${backAction!}" page=page />
</TubingGui>