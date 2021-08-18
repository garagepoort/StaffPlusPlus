<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
<#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>

<#macro banlore ban actions=[]>
    <Lore>
        <LoreLine>
            &bId: &6${ban.id}
        </LoreLine>

        <LoreLine if="${$config.get("server-sync-module.ban-sync")?c}">
            &bServer: &6${ban.serverName}
        </LoreLine>

        <LoreLine>
            &bBanned player: &6${ban.targetName}
        </LoreLine>

        <LoreLine>
            &bIssuer: &6${ban.issuerName}
        </LoreLine>

        <LoreLine>
            &bIssued on: &6${GuiUtils.parseTimestamp(ban.creationTimestamp, $config.get("timestamp-format"))}
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

<#macro ipbanlorelines ipban>
    <LoreLine if="${$config.get("server-sync-module.ban-sync")?c}">
        &bServer: &6${ipban.serverName}
    </LoreLine>

    <LoreLine>
        &bIp rule: &6${ipban.ip}
    </LoreLine>

    <LoreLine>
        &bIssuer: &6${ipban.issuerName}
    </LoreLine>

    <LoreLine>
        &bIssued on: &6${GuiUtils.parseTimestamp(ipban.creationDate, $config.get("timestamp-format"))}
    </LoreLine>

    <#if ipban.endTimestamp.isPresent()>
        <LoreLine>&bTime Left: &6${ipban.humanReadableDuration}</LoreLine>
    </#if>
    <LoreLine>
        <#if ipban.endTimestamp.isPresent()>&6TEMPORARY<#else>&CPERMANENT</#if>
    </LoreLine>
</#macro>