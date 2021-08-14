<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
<#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>

<#macro banlore ban actions=[]>
    <Lore>
        <LoreLine>
            &bId: &6${ban.id}
        </LoreLine>

        <LoreLine if="${.data_model["server-sync-module.ban-sync"]?c}">
            &bServer: &6${ban.serverName}
        </LoreLine>

        <LoreLine>
            &bBanned player: &6${ban.targetName}
        </LoreLine>

        <LoreLine>
            &bIssuer: &6${ban.issuerName}
        </LoreLine>

        <LoreLine>
            &bIssued on: &6${GuiUtils.parseTimestamp(ban.creationTimestamp, .data_model["timestamp-format"])}
        </LoreLine>

        <LoreLine>
            &bReason: &6${ban.reason}
        </LoreLine>

        <#if ban.endTimestamp??>
            <LoreLine>&bTime Left: &6${ban.humanReadableDuration}</LoreLine>
        </#if>

        <LoreLine></LoreLine>
        <LoreLine>
            <#if ban.endTimestamp??>&6TEMPORARY<#else>&CPERMANENT</#if>
        </LoreLine>
    </Lore>
</#macro>