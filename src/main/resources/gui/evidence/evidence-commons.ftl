<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<#macro evidenceButton slot evidence backAction>
    <#if $config.get("investigations-module.enabled")>
        <GuiItem slot="${slot}"
                 id="add-evidence-item"
                 class="add-evidence-item"
                 onLeftClick="manage-investigation-evidence/view/investigation-link?backAction=${URLEncoder.encode(backAction)}&evidenceId=${evidence.id}&evidenceType=${evidence.evidenceType}&evidenceDescription=${evidence.description}"
                 material="ANVIL">
            <name class="item-name">Add this as evidence to investigation</name>
            <Lore>
                <LoreLine>
                    <t class="link-evidence-label">Click to link evidence to investigation</t>
                </LoreLine>
            </Lore>
        </GuiItem>
    </#if>
</#macro>