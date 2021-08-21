<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
<#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>

<#macro mutelorelines mute actions=[]>
        <LoreLine>
            &bId: &6${mute.id}
        </LoreLine>

        <LoreLine if="${$config.get("server-sync-module.mute-sync")?c}">
            &bServer: &6${mute.serverName}
        </LoreLine>

        <LoreLine>
            &bMuted player: &6${mute.targetName}
        </LoreLine>

        <LoreLine>
            &bIssuer: &6${mute.issuerName}
        </LoreLine>

        <LoreLine>
            &bIssued on: &6${GuiUtils.parseTimestamp(mute.creationTimestamp, $config.get("timestamp-format"))}
        </LoreLine>
        <#if mute.hasEnded()>
            <LoreLine>
                &bEnded on: &6${GuiUtils.parseTimestamp(mute.endTimestamp, $config.get("timestamp-format"))}
            </LoreLine>
        </#if>
        <LoreLine>
            &bReason: &6${mute.reason}
        </LoreLine>

        <#if mute.endTimestamp??>
            <LoreLine>&bTime Left: &6${mute.humanReadableDuration}</LoreLine>
        </#if>

        <LoreLine></LoreLine>
        <LoreLine>
            <#if mute.endTimestamp??>&6TEMPORARY<#else>&CPERMANENT</#if>
        </LoreLine>
</#macro>