<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>

<#macro warninglorelines warning>
    <#assign appealApproved=$config.get("warnings-module.appeals.enabled") && warning.appeal.isPresent() && warning.appeal.get().status.name() == 'APPROVED'/>
    <LoreLine>
        <t color="&b" class="detail-label">Id: </t>
        <t color="&6" class="detail-value">${warning.id}</t>
    </LoreLine>

    <LoreLine if="config|server-sync-module.warning-sync">
        &bServer: &6${warning.serverName}
    </LoreLine>

    <LoreLine>
        <t color="&b" id="severity-label" class="detail-label">Severity: </t>
        <t color="&6" id="severity-value" class="detail-value">${warning.severity}</t>
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
        <LoreLine>
            <t id="appeal-approved-label" class="detail-label">Appeal </t>
            <t id="appeal-approved-value" color="&2">approved</t>
        </LoreLine>
    </#if>

    <#if warning.expired && !appealApproved>
        <LoreLine>
            <t id="expired" color="&C">Expired</t>
        </LoreLine>
    </#if>
</#macro>

<#macro appealinfoitem appeal action>
    <GuiItem slot="31"
             name="Appeal &6${appeal.status.name()}"
             material="WRITABLE_BOOK"
             onLeftClick="${action}">
        <Lore>
            <LoreLine>
                &bAppealer: &6${appeal.appealerName}
            </LoreLine>
            <LoreLine>
                &bTimestamp:
                &6${GuiUtils.parseTimestampSeconds(appeal.creationTimestamp, $config.get("timestamp-format"))}
            </LoreLine>
            <LoreLine>
                &bReason: &6${appeal.reason}
            </LoreLine>
            <#if appeal.status.name() != "OPEN">
                <LoreLine>
                    &bResolved by: &6${appeal.resolverName}
                </LoreLine>
            </#if>
            <#if appeal.resolveReason.isPresent()>
                <LoreLine>
                    &bResolve Reason: &6${appeal.resolveReason.get()}
                </LoreLine>
            </#if>
        </Lore>
    </GuiItem>
</#macro>