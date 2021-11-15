<#import "warning-commons.ftl" as warningcommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#import "/gui/evidence/evidence-commons.ftl" as evidenceCommons/>
<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign URLEncoder=statics['java.net.URLEncoder']>

<#macro appealbutton action>
    <GuiItem slot="31"
             material="BOOK"
             name="Add appeal"
             onLeftClick="${action}">
        <Lore>
            <LoreLine>Click to expire this warning</LoreLine>
        </Lore>
    </GuiItem>
</#macro>

<TubingGui size="54" id="warning-detail">
    <title>Warning for: ${warning.targetName}</title>

    <GuiItem slot="13"
             id="info"
             material="PLAYER_HEAD"
             name="Warning">
        <Lore>
            <@warningcommons.warninglorelines warning=warning/>
        </Lore>
    </GuiItem>

    <GuiItem slot="8"
             permission="config|permissions:warnings.manage.delete"
             material="REDSTONE_BLOCK"
             name="Delete"
             id="delete"
             onLeftClick="manage-warnings/delete?warningId=${warning.id}">
        <Lore>
            <LoreLine>Click to delete this warning</LoreLine>
            <#if rollbackCommands?size != 0>
                <@commons.line />
                <LoreLine>&6Rollback actions:</LoreLine>
                <#list rollbackCommands as command>
                    <LoreLine> - ${command}</LoreLine>
                </#list>
            </#if>
        </Lore>
    </GuiItem>

    <#if !warning.expired && !warning.hasApprovedAppeal()>
        <GuiItem slot="17"
                 id="expire"
                 permission="config|permissions:warnings.manage.expire"
                 material="ORANGE_STAINED_GLASS_PANE"
                 name="Expire"
                 onLeftClick="manage-warnings/expire?warningId=${warning.id}">
            <Lore>
                <LoreLine>Click to expire this warning</LoreLine>
            </Lore>
        </GuiItem>
    </#if>

    <#if $config.get("warnings-module.appeals.enabled")>
        <#assign canAppeal=!warning.appeal.isPresent()
        && ($permissions.has(player, $config.get("permissions:warnings.appeals.create-others"))
        || ($permissions.has(player, $config.get("permissions:warnings.appeals.create")) && warning.targetUuid == player.uniqueId))/>

        <#if canAppeal>
            <#if $config.get("warnings-module.appeals.fixed-reason")>
                <@appealbutton action="manage-warning-appeals/view/create/reason-select?warningId=${warning.id}" />
            <#else>
                <@appealbutton action="manage-warning-appeals/view/create/reason-chat?warningId=${warning.id}" />
            </#if>
        <#elseif warning.appeal.isPresent()>
            <#if ($permissions.has(player, $config.get("permissions:warnings.appeals.approve"))
                || $permission.has(player, $config.get("permissions:warnings.appeals.reject")))
                && warning.appeal.get().status.name() == "OPEN">
                <@warningcommons.appealinfoitem
                appeal=warning.appeal.get()
                action="manage-warning-appeals/view/detail?appealId=${warning.appeal.get().id}&backAction=${URLEncoder.encode(currentAction)}"/>
            <#else>
                <@warningcommons.appealinfoitem appeal=warning.appeal.get() action="$NOOP"/>
            </#if>
        </#if>
    </#if>

    <@evidenceCommons.evidenceButton slot=14 evidence=warning backAction=currentAction />
    <@commons.backButton action=backAction/>
</TubingGui>