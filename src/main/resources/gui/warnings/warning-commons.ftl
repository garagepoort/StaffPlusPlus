<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>

<#macro warninglorelines warning>
    <#assign appealApproved=$config.get("warnings-module.appeals.enabled") && warning.appeal.isPresent() && warning.appeal.get().status.name() == 'APPROVED'/>
    <#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>
    <LoreLine>
        <t color="&b" id="id-label" class="detail-label">Id: </t>
        <t color="&6" id="id-value" class="detail-value">${warning.id}</t>
    </LoreLine>

    <LoreLine if="${$config.get("server-sync-module.warning-sync")?has_content?c}">
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
    </LoreLine>
    <#list JavaUtils.formatLines(warning.reason, 30) as reasonLine>
        <LoreLine>
            <t color="&6" id="reason-value" class="detail-value">${reasonLine}</t>
        </LoreLine>
    </#list>

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