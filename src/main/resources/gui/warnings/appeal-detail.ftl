<#import "/gui/commons/commons.ftl" as commons/>
<#import "/gui/appeals/appeal-commons.ftl" as appealcommons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<#include "/gui/commons/translate.ftl"/>
<TubingGui size="54" id="warning-appeal-detail">
    <title class="gui-title">Manage appeal</title>

    <@appealcommons.appealinfoitem slot="13" appeal=appeal action="$NOOP"/>

    <#if $permissions.has(player, $config.get("permissions:warnings.appeals.approve"))>
        <#list [34,35,43,44] as slot>
            <GuiItem slot="${slot}"
                     id="warning-appeal-approve-${slot?index}"
                     material="GREEN_GLAZED_TERRACOTTA"
                     class="warning-appeal-approve appeal-approve"
                     onLeftClick="manage-warning-appeals/approve?appealId=${appeal.id}">
                <name class="item-name"><@translate key="gui.warnings.appeal-detail.approve.title"/></name>
                <Lore>
                    <LoreLine><@translate key="gui.warnings.appeal-detail.approve.lore"/></LoreLine>
                </Lore>
                <#if rollbackCommands?size != 0>
                    <@commons.line />
                    <LoreLine>
                        <t id="rollback-actions-label" color="&6">Rollback actions:</t>
                    </LoreLine>
                    <#list rollbackCommands as command>
                        <LoreLine><t id="command-label"> - ${command}</t></LoreLine>
                    </#list>
                </#if>
            </GuiItem>
        </#list>
    </#if>
    <#if $permissions.has(player, $config.get("permissions:warnings.appeals.reject"))>
        <#list [30,31,32,39,40,41] as slot>
            <GuiItem slot="${slot}"
                     id="warning-appeal-reject-${slot?index}"
                     material="RED_GLAZED_TERRACOTTA"
                     class="warning-appeal-reject appeal-reject"
                     onLeftClick="manage-warning-appeals/reject?appealId=${appeal.id}">
                <name class="item-name"><@translate key="gui.warnings.appeal-detail.reject.title"/></name>
                <Lore>
                    <LoreLine><@translate key="gui.warnings.appeal-detail.reject.lore"/></LoreLine>
                </Lore>
            </GuiItem>
        </#list>
    </#if>

    <#if backAction??>
        <@commons.backButton action=backAction/>
    </#if>
</TubingGui>