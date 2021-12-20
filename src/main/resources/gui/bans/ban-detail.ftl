<#import "/gui/commons/commons.ftl" as commons/>
<#import "/gui/evidence/evidence-commons.ftl" as evidenceCommons/>
<#import "/gui/bans/ban-commons.ftl" as banCommons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<#import "/gui/appeals/appeal-commons.ftl" as appealcommons/>

<TubingGui size="54" id="investigation-detail">
    <title class="gui-title">Player: ${ban.targetName}</title>

    <@banCommons.banitem itemId="ban-info" slot=13 ban=ban/>

    <#list [30,31,32,39,40,41] as slot>
        <GuiItem slot="${slot}"
                 id="unban-${slot?index}"
                 material="RED_STAINED_GLASS_PANE"
                 onLeftClick="manage-bans/unban?banId=${ban.id}">
            <name class="item-name">Unban player</name>
            <Lore>
                <LoreLine>
                    <t id="unban-action-label">Click to unban this player</t>
                </LoreLine>
            </Lore>
        </GuiItem>
    </#list>

    <#if $config.get("ban-module.appeals.enabled")>
        <#assign hasAppealPermission=$permissions.has(player, $config.get("permissions:bans.appeals.create"))/>
        <#assign hasOtherAppealPermission=$permissions.has(player, $config.get("permissions:bans.appeals.create-others"))/>
        <#assign canAppeal=(ban.appeal.isPresent() == false) && ((ban.targetUuid == player.uniqueId && hasAppealPermission) || hasOtherAppealPermission)/>

        <#if canAppeal>
            <#if $config.get("ban-module.appeals.fixed-reason")>
                <@appealcommons.appealbutton action="manage-ban-appeals/view/create/reason-select?banId=${ban.id}&backAction=${URLEncoder.encode(currentAction)}" />
            <#else>
                <@appealcommons.appealbutton action="manage-ban-appeals/view/create/reason-chat?banId=${ban.id}&backAction=${URLEncoder.encode(currentAction)}" />
            </#if>
        <#elseif ban.appeal.isPresent()>
            <#if ($permissions.has(player, $config.get("permissions:bans.appeals.approve"))
            || $permission.has(player, $config.get("permissions:bans.appeals.reject")))
            && ban.appeal.get().status.name() == "OPEN">
                <@appealcommons.appealinfoitem
                appeal=ban.appeal.get()
                action="manage-ban-appeals/view/detail?appealId=${ban.appeal.get().id}&backAction=${URLEncoder.encode(currentAction)}"/>
            <#else>
                <@appealcommons.appealinfoitem appeal=ban.appeal.get() action="$NOOP"/>
            </#if>
        </#if>
    </#if>

    <#if backAction??>
        <@commons.backButton action=backAction/>
    </#if>
</TubingGui>