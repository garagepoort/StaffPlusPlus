<#import "/gui/commons/commons.ftl" as commons/>
<#import "/gui/evidence/evidence-commons.ftl" as evidenceCommons/>
<#import "/gui/mutes/mute-commons.ftl" as muteCommons/>
<#import "/gui/appeals/appeal-commons.ftl" as appealcommons/>
<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="54" id="manage-mute">
    <title class="gui-title">Player: ${mute.targetName}</title>
    <GuiItem id="info"
             slot="13"
             onLeftClick="$NOOP"
             material="PLAYER_HEAD"
    >
        <name class="item-name" color="&3">Mute</name>
        <@muteCommons.mutelorelines mute=mute />
    </GuiItem>
    <@evidenceCommons.evidenceButton slot=14 evidence=mute backAction=currentAction />
    <#list [30,31,32,39,40,41] as slot>
        <GuiItem id="unmute-${slot?index}"
                 slot="${slot}"
                 onLeftClick="manage-mutes/unmute?muteId=${mute.id}"
                 material="RED_STAINED_GLASS_PANE">
            <name class="item-name">Unmute player</name>
            <Lore>
                <LoreLine>
                    <t id="unmute-label">Click to unmute this player</t>
                </LoreLine>
            </Lore>
        </GuiItem>
    </#list>


    <#if $config.get("mute-module.appeals.enabled")>
        <#assign hasAppealPermission=$permissions.has(player, $config.get("permissions:mutes.appeals.create"))/>
        <#assign hasOtherAppealPermission=$permissions.has(player, $config.get("permissions:mutes.appeals.create-others"))/>
        <#assign canAppeal=(mute.appeal.isPresent() == false) && ((mute.targetUuid == player.uniqueId && hasAppealPermission) || hasOtherAppealPermission)/>

        <#if canAppeal>
            <#if $config.get("mute-module.appeals.fixed-reason")>
                <@appealcommons.appealbutton slot="22" action="manage-mute-appeals/view/create/reason-select?muteId=${mute.id}&backAction=${URLEncoder.encode(currentAction)}" />
            <#else>
                <@appealcommons.appealbutton slot="22" action="manage-mute-appeals/view/create/reason-chat?muteId=${mute.id}&backAction=${URLEncoder.encode(currentAction)}" />
            </#if>
        <#elseif mute.appeal.isPresent()>
            <#if ($permissions.has(player, $config.get("permissions:mutes.appeals.approve"))
            || $permission.has(player, $config.get("permissions:mutes.appeals.reject")))
            && mute.appeal.get().status.name() == "OPEN">
                <@appealcommons.appealinfoitem
                slot="22"
                appeal=mute.appeal.get()
                action="manage-mute-appeals/view/detail?appealId=${mute.appeal.get().id}&backAction=${URLEncoder.encode(currentAction)}"/>
            <#else>
                <@appealcommons.appealinfoitem slot="22" appeal=mute.appeal.get() action="$NOOP"/>
            </#if>
        </#if>
    </#if>

    <#if backAction??>
        <@commons.backButton action=backAction backSlot=49/>
    </#if>
</TubingGui>