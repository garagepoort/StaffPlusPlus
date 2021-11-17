<#assign URLEncoder=statics['java.net.URLEncoder']>
<TubingGui size="27" id="hub">
    <title class="gui-title">${$config.get("staffmode-modules:modules.gui-module.name")}</title>

    <#list [0,8,9,17,18,26] as slot>
        <GuiItem slot="${slot}"
                 id="color-select-${slot?index}"
                 material="${settings.glassColor}"
                 onLeftClick="hub/view/color-select">
            <name class="item-name" color="&b">Color #${settings.glassColor}</name>
            <Lore>
                <LoreLine>
                    <t id="change-color-label" color="&7">Click to change your GUI color!</t>
                </LoreLine>
            </Lore>
        </GuiItem>
    </#list>

    <#if $config.get("staffmode-modules:modules.gui-module.reports-gui") && $permissions.has(player, $config.get("permissions:reports.manage.view"))>
        <GuiItem slot="1"
                 id="open-reports"
                 material="PAPER"
                 onLeftClick="manage-reports/view/open?backAction=${URLEncoder.encode(currentAction)}">
            <name class="item-name" color="&b">${$config.get("staffmode-modules:modules.gui-module.reports-title")}</name>
            <Lore>
                <LoreLine>
                    <t id="reports-lore" color="&7">${$config.get("staffmode-modules:modules.gui-module.reports-lore")}</t>
                </LoreLine>
            </Lore>
        </GuiItem>
        <GuiItem id="my-reports"
                 slot="2"
                 material="PAPER"
                 onLeftClick="manage-reports/view/my-assigned?backAction=${URLEncoder.encode(currentAction)}">
            <name class="item-name" color="&b">${$config.get("staffmode-modules:modules.gui-module.my-reports-title")}</name>
            <Lore>
                <LoreLine>
                    <t id="my-reports-lore" color="&7">${$config.get("staffmode-modules:modules.gui-module.my-reports-lore")}</t>
                </LoreLine>
            </Lore>
        </GuiItem>
        <GuiItem id="assigned-reports"
                 slot="3"
                 material="PAPER"
                 onLeftClick="manage-reports/view/assigned?backAction=${URLEncoder.encode(currentAction)}">
            <name class="item-name" color="&b">${$config.get("staffmode-modules:modules.gui-module.assigned-reports-title")}</name>
            <Lore>
                <LoreLine>
                    <t id="assigned-reports-lore" color="&7">${$config.get("staffmode-modules:modules.gui-module.assigned-reports-lore")}</t>
                </LoreLine>
            </Lore>
        </GuiItem>
        <GuiItem id="closed-reports"
                 slot="4"
                 material="PAPER"
                 onLeftClick="manage-reports/view/closed?backAction=${URLEncoder.encode(currentAction)}">
            <name class="item-name" color="&b">${$config.get("staffmode-modules:modules.gui-module.closed-reports-title")}</name>
            <Lore>
                <LoreLine>
                    <t id="closed-reports-lore" color="&7">${$config.get("staffmode-modules:modules.gui-module.closed-reports-lore")}</t>
                </LoreLine>
            </Lore>
        </GuiItem>
    </#if>


    <GuiItem id="miner"
             slot="10"
             if="config|staffmode-modules:modules.gui-module.miner-gui"
             material="STONE_PICKAXE"
             onLeftClick="miners/view?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name" color="&b">${$config.get("staffmode-modules:modules.gui-module.miner-name")}</name>
        <Lore>
            <LoreLine>
                <t id="miner-lore" color="&7">${$config.get("staffmode-modules:modules.gui-module.miner-lore")}</t>
            </LoreLine>
        </Lore>
    </GuiItem>


    <GuiItem id="protected-areas"
             slot="19"
             if="config|staffmode-modules:modules.gui-module.protected-areas-gui"
             material="SHIELD"
             onLeftClick="protected-areas/view?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name" color="&b">${$config.get("staffmode-modules:modules.gui-module.protected-areas-name")}</name>
        <Lore>
            <LoreLine>
                <t id="protected-areas-lore" color="&7">${$config.get("staffmode-modules:modules.gui-module.protected-areas-lore")}</t>
            </LoreLine>
        </Lore>
    </GuiItem>


    <GuiItem id="bans"
             slot="7"
             if="config|staffmode-modules:modules.gui-module.ban-gui"
             material="PLAYER_HEAD"
             onLeftClick="manage-bans/view/overview?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name" color="&b">${$config.get("staffmode-modules:modules.gui-module.ban-name")}</name>
        <Lore>
            <LoreLine>
                <t id="bans-lore" color="&7">${$config.get("staffmode-modules:modules.gui-module.ban-lore")}</t>
            </LoreLine>
        </Lore>
    </GuiItem>


    <GuiItem id="mutes"
             slot="16"
             if="config|staffmode-modules:modules.gui-module.mute-gui"
             material="SIGN"
             onLeftClick="manage-mutes/view/all-active?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name" color="&b">${$config.get("staffmode-modules:modules.gui-module.mute-name")}</name>
        <Lore>
            <LoreLine>
                <t id="mutes-lore" color="&7">${$config.get("staffmode-modules:modules.gui-module.mute-lore")}</t>
            </LoreLine>
        </Lore>
    </GuiItem>

    <GuiItem id="investigations"
             slot="25"
             if="config|staffmode-modules:modules.gui-module.investigation-gui"
             material="BOOK"
             onLeftClick="manage-investigations/view/overview?backAction=${URLEncoder.encode(currentAction)}">
        <name class="item-name" color="&b">${$config.get("staffmode-modules:modules.gui-module.investigation-name")}</name>
        <Lore>
            <LoreLine>
                <t id="investigations-lore" color="&7">${$config.get("staffmode-modules:modules.gui-module.investigation-lore")}</t>
            </LoreLine>
        </Lore>
    </GuiItem>

</TubingGui>