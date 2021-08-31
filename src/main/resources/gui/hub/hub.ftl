<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="27">
    <title>${$config.get("staffmode-modules:modules.gui-module.name")}</title>

    <#list [0,8,9,17,18,26] as slot>
        <GuiItem slot="${slot}"
                 material="STAINED_GLASS_PANE"
                 name="&bColor #STAINED_GLASS_PANE"
                 onLeftClick="$NOOP">
        </GuiItem>
    </#list>

    <#if $config.get("staffmode-modules:modules.gui-module.reports-gui") && $permissions.has(player, $config.get("permissions:reports.manage.view"))>
        <GuiItem slot="1"
                 material="PAPER"
                 name="config|staffmode-modules:modules.gui-module.reports-title"
                 onLeftClick="manage-reports/view/open?backAction=${URLEncoder.encode(currentAction)}">
            <Lore>
                <LoreLine>${$config.get("staffmode-modules:modules.gui-module.reports-lore")}</LoreLine>
            </Lore>
        </GuiItem>
        <GuiItem slot="2"
                 material="PAPER"
                 name="config|staffmode-modules:modules.gui-module.my-reports-title"
                 onLeftClick="manage-reports/view/my-assigned?backAction=${URLEncoder.encode(currentAction)}">
            <Lore>
                <LoreLine>${$config.get("staffmode-modules:modules.gui-module.my-reports-lore")}</LoreLine>
            </Lore>
        </GuiItem>
        <GuiItem slot="3"
                 material="PAPER"
                 name="config|staffmode-modules:modules.gui-module.assigned-reports-title"
                 onLeftClick="manage-reports/view/assigned?backAction=${URLEncoder.encode(currentAction)}">
            <Lore>
                <LoreLine>${$config.get("staffmode-modules:modules.gui-module.assigned-reports-lore")}</LoreLine>
            </Lore>
        </GuiItem>
        <GuiItem slot="4"
                 material="PAPER"
                 name="config|staffmode-modules:modules.gui-module.closed-reports-title"
                 onLeftClick="manage-reports/view/closed?backAction=${URLEncoder.encode(currentAction)}">
            <Lore>
                <LoreLine>${$config.get("staffmode-modules:modules.gui-module.closed-reports-lore")}</LoreLine>
            </Lore>
        </GuiItem>
    </#if>


    <GuiItem slot="10"
             if="config|staffmode-modules:modules.gui-module.miner-gui"
             material="STONE_PICKAXE"
             name="config|staffmode-modules:modules.gui-module.miner-name"
             onLeftClick="miners/view?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>${$config.get("staffmode-modules:modules.gui-module.miner-lore")}</LoreLine>
        </Lore>
    </GuiItem>


    <GuiItem slot="19"
             if="config|staffmode-modules:modules.gui-module.protected-areas-gui"
             material="SHIELD"
             name="config|staffmode-modules:modules.gui-module.protected-areas-name"
             onLeftClick="protected-areas/view?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>${$config.get("staffmode-modules:modules.gui-module.protected-areas-lore")}</LoreLine>
        </Lore>
    </GuiItem>


    <GuiItem slot="7"
             if="config|staffmode-modules:modules.gui-module.ban-gui"
             material="BANNER"
             name="config|staffmode-modules:modules.gui-module.ban-name"
             onLeftClick="manage-bans/view/overview?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>${$config.get("staffmode-modules:modules.gui-module.ban-lore")}</LoreLine>
        </Lore>
    </GuiItem>


    <GuiItem slot="16"
             if="config|staffmode-modules:modules.gui-module.mute-gui"
             material="SIGN"
             name="config|staffmode-modules:modules.gui-module.mute-name"
             onLeftClick="manage-mutes/view/all-active?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>${$config.get("staffmode-modules:modules.gui-module.mute-lore")}</LoreLine>
        </Lore>
    </GuiItem>

    <GuiItem slot="25"
             if="config|staffmode-modules:modules.gui-module.investigation-gui"
             material="BOOK"
             name="config|staffmode-modules:modules.gui-module.investigation-name"
             onLeftClick="manage-investigations/view/overview?backAction=${URLEncoder.encode(currentAction)}">
        <Lore>
            <LoreLine>${$config.get("staffmode-modules:modules.gui-module.investigation-lore")}</LoreLine>
        </Lore>
    </GuiItem>

</TubingGui>