<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>

<#macro notelorelines note>
        <LoreLine>
            &bId: &6${note.id}
        </LoreLine>

        <LoreLine if="${$config.get("server-sync-module.notes-sync")?c}">
            &bServer: &6${note.serverName}
        </LoreLine>

        <LoreLine>
            &bPlayer: &6${note.targetName}
        </LoreLine>

        <LoreLine>
            &bCreated By: &6${note.notedByName}
        </LoreLine>

        <LoreLine>
            &bCreated on: &6${GuiUtils.parseTimestamp(note.creationTimestamp, $config.get("timestamp-format"))}
        </LoreLine>
        <LoreLine>
            &bNote:
        </LoreLine>
        <#list JavaUtils.formatLines(note.note, 30) as reasonLine>
            <LoreLine>${"&7   " + reasonLine}</LoreLine>
        </#list>

        <LoreLine></LoreLine>
        <LoreLine if="${note.privateNote?c}">&ePrivate Note</LoreLine>
</#macro>