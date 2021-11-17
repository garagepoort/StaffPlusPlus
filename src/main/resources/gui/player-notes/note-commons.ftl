<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>

<#macro notelorelines note>
        <LoreLine>
            <t color="&b" id="id-label" class="detail-label">Id: </t>
            <t color="&6" id="id-value" class="detail-value">${note.id}</t>
        </LoreLine>

        <LoreLine if="${$config.get("server-sync-module.notes-sync")?c}">
            <t color="&b" id="server-label" class="detail-label">Server: </t>
            <t color="&6" id="server-value" class="detail-value">${note.serverName}</t>
        </LoreLine>

        <LoreLine>
            <t color="&b" id="player-label" class="detail-label">Player: </t>
            <t color="&6" id="player-value" class="detail-value">${note.targetName}</t>
        </LoreLine>

        <LoreLine>
            <t color="&b" id="created-by-label" class="detail-label">Created by: </t>
            <t color="&6" id="created-by-value" class="detail-value">${note.notedByName}</t>
        </LoreLine>

        <LoreLine>
            <t color="&b" id="created-on-label" class="detail-label">Created on: </t>
            <t color="&6" id="created-on-value" class="detail-value">${GuiUtils.parseTimestamp(note.creationTimestamp, $config.get("timestamp-format"))}</t>
        </LoreLine>
        <LoreLine>
            <t color="&b" id="note-label" class="detail-label">Note:</t>
        </LoreLine>
        <#list JavaUtils.formatLines(note.note, 30) as reasonLine>
            <LoreLine>
                <t color="&7" id="note-value" class="detail-value">   ${reasonLine}</t>
            </LoreLine>
        </#list>

        <LoreLine></LoreLine>
        <LoreLine if="${note.privateNote?c}">
            <t id="private-label" color="&e" class>Private Note</t>
        </LoreLine>
</#macro>