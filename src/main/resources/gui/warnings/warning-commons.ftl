<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>

<#macro warninglorelines warning>
    <#assign appealApproved=$config.get("warnings-module.appeals.enabled") && warning.appeal.isPresent() && warning.appeal.get().status.name() == 'APPROVED'/>
        <LoreLine>
            &bId: &6${warning.id}
        </LoreLine>

        <LoreLine if="config|server-sync-module.warning-sync">
            &bServer: &6${warning.serverName}
        </LoreLine>

        <LoreLine>
            &bSeverity: &6${warning.severity}
        </LoreLine>

        <LoreLine>
            &bIssuer: &6${warning.issuerName}
        </LoreLine>

        <LoreLine>
            &bCulprit: &6${warning.targetName}
        </LoreLine>

        <LoreLine>
            &bIssued on: &6${GuiUtils.parseTimestamp(warning.creationTimestamp, $config.get("timestamp-format"))}
        </LoreLine>

        <LoreLine>
            &bReason: &6${warning.reason}
        </LoreLine>

        <LoreLine></LoreLine>

        <#if appealApproved>
            <LoreLine>Appeal &2approved</LoreLine>
        </#if>

        <#if warning.expired && !appealApproved>
            <LoreLine>&cExpired</LoreLine>
        </#if>
</#macro>