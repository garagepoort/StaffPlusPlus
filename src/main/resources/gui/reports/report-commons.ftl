<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#include "/gui/commons/translate.ftl"/>
<#macro reportitem slot report itemId="report-info" onRightClick="$NOOP" onLeftClick="$NOOP" onMiddleClick="$NOOP" actions=[]>
    <#assign DateTimeFormatter=statics['java.time.format.DateTimeFormatter']>
    <#assign JavaUtils=statics['net.shortninja.staffplus.core.common.JavaUtils']>
    <GuiItem
        id="${itemId}"
        class="report-info"
        slot="${slot}"
        onLeftClick="${onLeftClick}"
        onRightClick="${onRightClick}"
        onMiddleClick="${onMiddleClick}"
        material="PAPER">
        <name class="item-name" color="&5">
            Report
        </name>
        <Lore>
            <LoreLine>
                <t color="&b" id="id-label" class="detail-label"><@translate key="gui.id"/>: </t>
                <t color="&7" id="id-value" class="detail-value">${report.id}</t>
            </LoreLine>

            <LoreLine>
                <t color="&b" id="status-label" class="detail-label"><@translate key="gui.status"/>: </t>
                <t color="&7" id="status-value" class="detail-value">${report.reportStatus.name()}</t>
            </LoreLine>

            <#if $config.get("server-sync-module.report-sync")?has_content >
                <LoreLine>
                    <t color="&b" id="server-label" class="detail-label"><@translate key="gui.server"/>: </t>
                    <t color="&7" id="server-value" class="detail-value">${report.serverName}</t>
                </LoreLine>
            </#if>

            <#if report.reportType.isPresent() == true >
                <LoreLine>
                    <t color="&b" id="type-label" class="detail-label"><@translate key="gui.type"/>: </t>
                    <t color="&7" id="type-value" class="detail-value">${report.reportType.get()}</t>
                </LoreLine>
            </#if>
            <#if report.reportStatus.name() != "OPEN" >
                <LoreLine>
                    <t color="&b" id="assignee-label" class="detail-label"><@translate key="gui.reports.assignee"/>: </t>
                    <t color="&7" id="assignee-value" class="detail-value">${report.staffName}</t>
                </LoreLine>
            </#if>

            <LoreLine>
            <#if report.culpritName?? >
                <t color="&b" id="culprit-label" class="detail-label"><@translate key="gui.reports.culprit"/>: </t>
                <t color="&7" id="culprit-value" class="detail-value">${report.culpritName}</t>
            <#else >
                <t color="&b" id="culprit-label" class="detail-label"><@translate key="gui.reports.culprit"/>: </t>
                <t color="&7" id="culprit-value" class="detail-value"><@translate key="gui.unknown"/></t>
            </#if>
            </LoreLine>

            <LoreLine>
                <t color="&b" id="timestamp-label" class="detail-label"><@translate key="gui.timestamp"/>: </t>
                <t color="&7" id="timestamp-value" class="detail-value">${report.creationDate.format(DateTimeFormatter.ofPattern($config.get("timestamp-format")))}</t>
            </LoreLine>

            <#if $config.get("reports-module.show-reporter") >
                <LoreLine>
                    <t color="&b" id="reporter-label" class="detail-label"><@translate key="gui.reports.reporter"/>: </t>
                    <t color="&7" id="reporter-value" class="detail-value">${report.reporterName}</t>
                </LoreLine>
            </#if>

            <LoreLine>
                <t color="&b" id="reason-label" class="detail-label"><@translate key="gui.reason"/>:</t>
            </LoreLine>
            <#list JavaUtils.formatLines(report.reason, 30) as reasonLine>
                <LoreLine>
                    <t color="&7" id="reason-value" class="detail-value">   ${reasonLine}</t>
                </LoreLine>
            </#list>

            <LoreLine><t></t></LoreLine>

            <#if report.closeReason?has_content >
                <LoreLine>
                    <t color="&b" id="close-reason-label" class="detail-label"><@translate key="gui.close-reason"/>:</t>
                </LoreLine>
                <#list JavaUtils.formatLines(report.closeReason, 30) as reasonLine>
                    <LoreLine>
                        <t color="&7" id="close-reason-value" class="detail-value">   ${reasonLine}</t>
                    </LoreLine>
                </#list>
            </#if>

            <LoreLine><t></t></LoreLine>
            <#if report.sppLocation.isPresent() == true >
                <LoreLine>
                    <t color="&b" id="location-label" class="detail-label"><@translate key="gui.location"/>: </t>
                    <t color="&7" id="location-world-value" class="detail-value">${report.sppLocation.get().worldName} </t>
                    <t color="&8" id="location-separator" class="detail-value">| </t>
                    <t color="&7" id="location-block-value" class="detail-value">${JavaUtils.serializeLocation(report.sppLocation.get())}</t>
                </LoreLine>
            <#else >
                <LoreLine>
                    <t color="&b" id="location-label" class="detail-label"><@translate key="gui.location"/>: </t>
                    <t color="&7" id="location-block-value" class="detail-value"><@translate key="gui.unknown"/></t>
                </LoreLine>
            </#if>

            <#if actions?has_content >
                <LoreLine><t></t></LoreLine>
                <#list actions as actionLine>
                    <LoreLine><t>${actionLine}</t></LoreLine>
                </#list>
            </#if>
        </Lore>
    </GuiItem>
</#macro>