<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>
<TubingGui size="54" id="staff-location-notes-overview">
    <title class="gui-title">Staff location notes</title>

    <#list notes as note>
        <GuiItem
            id="staff-location-note-info-${note?index}"
            class="staff-location-note-info"
            slot="${note?index}"
            <#if $permissions.has(player, $config.get("permissions:staff-locations.delete-note"))>
                onRightClick="staff-location-notes/view/delete?noteId=${note.id}&locationId=${locationId}"
            </#if>
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

                <LoreLine permission="config|permissions:staff-locations.delete-note">
                    <t id="delete-action-label" color="&C">Right click to delete note</t>
                </LoreLine>
            </Lore>
        </GuiItem>
    </#list>

    <@commons.pageFooter currentAction="${currentAction}" page=page />
    <GuiItem
        id="staff-location-note-create"
        slot="50"
        onLeftClick="staff-location-notes/create?locationId=${locationId}"
        material="BOOK"
    >
        <name class="item-name" color="&7">Create a new note</name>
    </GuiItem>
</TubingGui>