<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>

<#macro warninglorelines warning>
    <#assign appealApproved=$config.get("warnings-module.appeals.enabled") && warning.appeal.isPresent() && warning.appeal.get().status.name() == 'APPROVED'/>
    <LoreLine>
        <t color="&b" id="id-label" class="detail-label">Id: </t>
        <t color="&6" id="id-value" class="detail-value">${warning.id}</t>
    </LoreLine>

    <LoreLine if="config|server-sync-module.warning-sync">
        <t color="&b" id="server-label" class="detail-label">Server: </t>
        <t color="&6" id="server-value" class="detail-value">${warning.serverName}</t>
    </LoreLine>

    <LoreLine>
        <t color="&b" id="severity-label" class="detail-label">Severity: </t>
        <t color="&6" id="severity-value" class="detail-value">${warning.severity}</t>
    </LoreLine>

    <LoreLine>
        <t color="&b" id="issuer-label" class="detail-label">Issuer: </t>
        <t color="&6" id="issuer-value" class="detail-value">${warning.issuerName}</t>
    </LoreLine>

    <LoreLine>
        <t color="&b" id="culprit-label" class="detail-label">Culprit: </t>
        <t color="&6" id="culprit-value" class="detail-value">${warning.targetName}</t>
    </LoreLine>

    <LoreLine>
        <t color="&b" id="issued-on-label" class="detail-label">Issued on: </t>
        <t color="&6" id="issued-on-value" class="detail-value">${GuiUtils.parseTimestamp(warning.creationTimestamp, $config.get("timestamp-format"))}</t>
    </LoreLine>

    <LoreLine>
        <t color="&b" id="reason-label" class="detail-label">Reason: </t>
        <t color="&6" id="reason-value" class="detail-value">${warning.reason}</t>
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
             id="appeal-info"
             material="WRITABLE_BOOK"
             onLeftClick="${action}">
        <name class="item-name">
            <t id="appeal-status-label">Appeal </t>
            <t id="appeal-status-value" color="&6">${appeal.status.name()}</t>
        </name>
        <Lore>
            <LoreLine>
                <t color="&b" id="appealer-label" class="detail-label">Appealer: </t>
                <t color="&6" id="appealer-value" class="detail-value">${appeal.appealerName}</t>
            </LoreLine>
            <LoreLine>
                <t color="&b" id="timestamp-label" class="detail-label">Timestamp: </t>
                <t color="&6" id="timestamp-value" class="detail-value">${GuiUtils.parseTimestampSeconds(appeal.creationTimestamp, $config.get("timestamp-format"))}</t>
            </LoreLine>
            <LoreLine>
                <t color="&b" id="reason-label" class="detail-label">Reason: </t>
                <t color="&6" id="reason-value" class="detail-value">${appeal.reason}</t>
            </LoreLine>
            <#if appeal.status.name() != "OPEN">
                <LoreLine>
                    <t color="&b" id="resolved-by-label" class="detail-label">Resolved by: </t>
                    <t color="&6" id="resolved-by-value" class="detail-value">${appeal.resolverName}</t>
                </LoreLine>
            </#if>
            <#if appeal.resolveReason.isPresent()>
                <LoreLine>
                    <t color="&b" id="resolve-reason-label" class="detail-label">Resolve reason: </t>
                    <t color="&6" id="resolve-reason-value" class="detail-value">${appeal.resolveReason.get()}</t>
                </LoreLine>
            </#if>
        </Lore>
    </GuiItem>
</#macro>