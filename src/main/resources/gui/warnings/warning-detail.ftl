<#import "warning-commons.ftl" as warningcommons/>
<#import "/gui/commons/commons.ftl" as commons/>
<#import "/gui/evidence/evidence-commons.ftl" as evidenceCommons/>
<#assign GuiUtils=statics['net.shortninja.staffplus.core.common.gui.GuiUtils']>
<#assign URLEncoder=statics['java.net.URLEncoder']>

<#macro appealbutton action>
    <GuiItem id="add-appeal"
             slot="31"
             material="BOOK"
             onLeftClick="${action}">
        <name class="item-name">Add appeal</name>
        <Lore>
            <LoreLine>
                <t id="add-appeal-label">Click to appeal this warning</t>
            </LoreLine>
        </Lore>
    </GuiItem>
</#macro>

<TubingGui size="54" id="warning-detail">
    <title class="gui-title">Warning for: ${warning.targetName}</title>

    <GuiItem slot="13"
             id="warning-info"
             class="warning-info"
             material="PLAYER_HEAD">
        <name class="item-name">Warning</name>
        <Lore>
            <@warningcommons.warninglorelines warning=warning/>
        </Lore>
    </GuiItem>

    <GuiItem slot="8"
             id="delete"
             permission="config|permissions:warnings.manage.delete"
             material="REDSTONE_BLOCK"
             onLeftClick="manage-warnings/delete?warningId=${warning.id}">
        <name class="item-name">Delete</name>
        <Lore>
            <LoreLine>
                <t id="delete-action-label">Click to delete this warning</t>
            </LoreLine>
            <#if rollbackCommands?size != 0>
                <@commons.line />
                <LoreLine>
                    <t id="rollback-actions-label" color="&6">Rollback actions:</t>
                </LoreLine>
                <#list rollbackCommands as command>
                    <LoreLine><t id="command-label"> - ${command}</t></LoreLine>
                </#list>
            </#if>
        </Lore>
    </GuiItem>

    <#if !warning.expired && !warning.hasApprovedAppeal()>
        <GuiItem slot="17"
                 id="expire"
                 permission="config|permissions:warnings.manage.expire"
                 material="ORANGE_STAINED_GLASS_PANE"
                 onLeftClick="manage-warnings/expire?warningId=${warning.id}">
            <name class="item-name">Expire</name>
            <Lore>
                <LoreLine>
                    <t id="expire-action-label">Click to expire this warning</t>
                </LoreLine>
            </Lore>
        </GuiItem>
    </#if>

    <#if $config.get("warnings-module.appeals.enabled")>
        <#assign hasAppealPermission=$permissions.has(player, $config.get("permissions:warnings.appeals.create"))/>
        <#assign hasOtherAppealPermission=$permissions.has(player, $config.get("permissions:warnings.appeals.create-others"))/>
        <#assign canAppeal=(warning.appeal.isPresent() == false) && ((warning.targetUuid == player.uniqueId && hasAppealPermission) || hasOtherAppealPermission)/>

        <#if canAppeal>
            <#if $config.get("warnings-module.appeals.fixed-reason")>
                <@appealbutton action="manage-warning-appeals/view/create/reason-select?warningId=${warning.id}&backAction=${URLEncoder.encode(currentAction)}" />
            <#else>
                <@appealbutton action="manage-warning-appeals/view/create/reason-chat?warningId=${warning.id}&backAction=${URLEncoder.encode(currentAction)}" />
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