<#import "/gui/commons/commons.ftl" as commons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="report-reason-select">
    <title class="gui-title">Select reason for report</title>

    <#list reasons as reason >
        <GuiItem id="report-reason-${reason?index}"
                 class="report-reason report-reason-${reason.reason}"
                 slot="${reason?index}"
                 material="${reason.material}"

                <#if reason.reportType?has_content && culprit?has_content>
                    onLeftClick="reports/create?reason=${URLEncoder.encode(reason.reason)}&type=${reason.reportType}&culprit=${culprit}"
                <#elseif reason.reportType?has_content && !culprit?has_content>
                    onLeftClick="reports/create?reason=${URLEncoder.encode(reason.reason)}&type=${reason.reportType}"
                <#elseif !reason.reportType?has_content && culprit?has_content>
                    onLeftClick="reports/create?reason=${URLEncoder.encode(reason.reason)}&culprit=${culprit}"
                <#else >
                    onLeftClick="reports/create?reason=${URLEncoder.encode(reason.reason)}"
                </#if>
        >
            <name class="item-name">${reason.reason}</name>
            <Lore>
                <LoreLine>
                    <t id="report-reason-label">${reason.lore}</t>
                </LoreLine>
            </Lore>
        </GuiItem>
    </#list>
</TubingGui>