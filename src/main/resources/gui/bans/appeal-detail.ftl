<#import "/gui/commons/commons.ftl" as commons/>
<#import "/gui/appeals/appeal-commons.ftl" as appealcommons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<#include "/gui/commons/translate.ftl"/>
<TubingGui size="54" id="ban-appeal-detail">
    <title class="gui-title">Manage appeal</title>

    <@appealcommons.appealinfoitem slot="13" appeal=appeal action="$NOOP"/>

    <#if $permissions.has(player, $config.get("permissions:bans.appeals.approve"))>
        <#list [34,35,43,44] as slot>
            <GuiItem slot="${slot}"
                     id="ban-appeal-approve-${slot?index}"
                     material="GREEN_GLAZED_TERRACOTTA"
                     class="ban-appeal-approve appeal-approve"
                     onLeftClick="manage-ban-appeals/approve?appealId=${appeal.id}">
                <name class="item-name"><@translate key="gui.bans.appeal-detail.approve.title"/></name>
                <Lore>
                    <LoreLine><@translate key="gui.bans.appeal-detail.approve.lore"/></LoreLine>
                </Lore>
            </GuiItem>
        </#list>
    </#if>
    <#if $permissions.has(player, $config.get("permissions:bans.appeals.reject"))>
        <#list [30,31,32,39,40,41] as slot>
            <GuiItem slot="${slot}"
                     id="ban-appeal-reject-${slot?index}"
                     material="RED_GLAZED_TERRACOTTA"
                     class="ban-appeal-reject appeal-reject"
                     onLeftClick="manage-ban-appeals/reject?appealId=${appeal.id}">
                <name class="item-name"><@translate key="gui.bans.appeal-detail.reject.title"/></name>
                <Lore>
                    <LoreLine><@translate key="gui.bans.appeal-detail.reject.lore"/></LoreLine>
                </Lore>
            </GuiItem>
        </#list>
    </#if>

    <#if backAction??>
        <@commons.backButton action=backAction/>
    </#if>
</TubingGui>