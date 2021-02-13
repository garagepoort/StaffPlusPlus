# Changelog Staff++ | v1.14

### V1.14.39

### Features
- Add warning appeal system
- Add warning actions + rollback system
- Staffmode item-change property is now split up into item-drop & item-pickup

### Bugs
- Staff item Endereye no longer gets dropped when spammed

### V1.14.38

### Features
- Timestamp format can now be configured
- Add ID to the guis more consistently
- Add serverName to the guis (only if server sync enabled)
- infractions-top command can be passed an optional type parameter.
- GUI item materials for the infractions GUI can now be configured

### Bugs
- Fix luckperms context error when new players join
- Fix luckperms context not being set correctly

### V1.14.37
### Features
- Rework miner GUI to be more consistent with the other GUIs, pagination + back button
- Add notify warnings message to the lang files
- Add infractions-top command to list players with most infractions.

### Bugs
- Adding notes through the examine gui was broken.
- Adding warnings through the examine gui was broken.
- Custom-modules got broken in the previous release.


### V1.14.36
Migration guide:
https://app.gitbook.com/@staffplusplus-minecraft/s/staffplusplus/migration-guides/migrating-to-greater-than-v1.16.43
### Features
- Added LuckPerms integration for staffmode context
- Reload command now also reloads lang files
- Multi server support 
  - Keep staff mode enabled when switching servers
  - Keep vanish enabled when switching servers
  - ban/mute/warn players across the network
- No join/leave message is displayed when a player is in vanish mode

### Bugs
- Using a non staff item while in staff mode was throwing an exception.
- One of the lang files had a formatting error.

### V1.14.35
### Features
- Reworked chest interaction
- Added inventory & enderchest interaction to examine GUI
- Added permission for using the vanish command

### Bugs
- inventory interaction was not working even when correct permission were given
- Location inside the gui was showing a question mark character.

### V1.14.33
### Features
- mute/unmute permission split up into 2 different permissions
- ban/unban permission split up into 2 different permissions
- Examine mode now available for offline players
- Implemented enderchest interaction online and offline
- Added custom GUI for Staff Mode. https://staffplusplus-minecraft.gitbook.io/staffplusplus/features/staff-mode/custom-gui

#### Bugs
- Permission for the /reports command was not working properly

### V1.14.32
### Features
- Added mute/temp mute functionality
- Added infractions gui menu.
- Added kick command.
- Update discord integration plugin for kicks and mutes.

#### Bugs
- Time unit four HOUR/h was not working
- Fix duration if expired

### V1.14.30
#### Breaking
- Implemented fine-grained permissions for reports. Permissions have changed, check [reports wiki](https://github.com/garagepoort/StaffPlusPlus/wiki/Reporting#report-permission)! 

#### Features
- Added manage-reports command to open the GUI without being in staff mode
- Add Xray configuration to allow amount/time based notifications. Check [wiki](https://github.com/garagepoort/StaffPlusPlus/wiki/Alerting-System#x-ray) 

#### Bugs
- Added missing "clear inventory bypass" permission to the default config file.
- Fix reports sometimes not showing last line from reason
- Fix `/help staffplus` command

### V1.14.29
#### Bugs
- Nullpointer fix on alt detection
- Create a new permission to ensure not everyone gets report updates

### V1.14.28
#### Features
- Severity levels now working for warnings issued through examine mode
- Ability to interact with the players inventory in examine mode.
- Add reload configuration ability (experimental, see wiki)

#### Bugs
- Unban command logged the kick issuer instead of the unban issuer
- Fix nullpointer OnInventoryOpen
- Fix alt account detection, do not detect same player
- Fix Inventory item duplication on rejoin

### V1.14.26
#### Features
- rename 'teleport' command to 'teleportToLocation'
- Add 'teleportHere' command
- Add vanish indicator in the staff list gui
- Add kick reason placeholder to use for the kick message

#### Bugs
- StaffChat not turning off when using the handle
- Staff items triggered by pressure plates
- Better error messages for tempban

### V1.14.24
#### Features
- Teleport players back to their original location

#### Bugs
- Freezing not working through the gui or staff item
- Custom items enchantments not working
- Staff items only worked when clicking on air

### V1.14.23
#### Features
- Notify users when they have open reports
- Notify users when a report of them changes status
- User Reports overview ui
- Deletion of reports
- Notify users when they have warnings
- User Warnings overview ui

#### Bugs
- Fix reset of armor when exiting Staff mode
- Fix commands plugin prefix

### V1.14.22
#### Bugs
- Wrong titles for reports ui
- Closing reason message displayed even when closing-reason-disabled
- Closing reason not working if other plugins intercept the event

### V1.14.21
#### Features
- Implement kick & tempban system (https://github.com/garagepoort/StaffPlusPlus/issues/106)
- Implement kick discord integration
- Implement Alt account detection system
- Implement Alt account detection discord integration
- Add option to disable bungee network sync for staffchat (https://github.com/garagepoort/StaffPlusPlus/issues/111)

#### Bugs
- Fix miner gui
- Fix prevent inventory open in protected areas
- Fix ui color choosing
- Fix performance when autocompleting offline players (https://github.com/garagepoort/StaffPlusPlus/issues/111)

### V1.14.20
#### Features
- Trello integration added for reports
#### Bugs
- Report resolved event was sent when a report was actually rejected

### V1.14.19
#### Features
- Protect player added
- Protect area added
- "Vanish enabled" action bar displayed for staff members
- Now able to add worlds to the location configuration

### V1.14.18
#### Features
- Broadcast messages over the bungee network
#### Bugs
- Older version of 1.14.x were not working

### V1.14.16
#### Features
- Report closing reason support
- Authme integration for supporting staff login
- Trace log feature added

### V1.14.15
#### Features
- Warning discord integration

### V1.14.14
#### Features
- BungeeCord support. StaffChat will now be synced over all servers inside the bungee network.
- Update notifier implemented

### V1.14.13
#### Bugs
- Players could always bypass clear, freeze and teleport commands
- Resolved issue: https://github.com/garagepoort/StaffPlusPlus/issues/91
Items got removed when inspecting a chest. 

### V1.14.12
#### Features
- Add clear inventory bypass permission

#### Bugs
- Plugins directory was wrongly named which made it crash on Linux machines 

### V1.14.10
#### Features
- Delay argument has been added
- Implemented automatic update for the config file 

#### Bugs
- teleport command was not present in the configuration file

### V1.14.9
#### Bugs
- DELAY Action type not working when users are online

### V1.14.8
#### Features
- Teleport command added

#### Breaking
- Added new warning system

### V1.14.6
#### Features
- Implement new reportEvents
- Added Discord Integration for reporting events

#### Bugs
- Use ZonedDateTimeFor Reporting
- Remove silent sql failure

### V1.14.5
#### Features
- Added HEALTH argument
- New reporting system implemented

#### Bugs
- Fix bug when opening different inventories (BrewingStand, Furnace, Dispenser, ...)

#### Breaking
- Dropped support for flatfiles
- Implemented Sqlite, Mysql databases

### V1.14.4
#### Features
- New arguments system implemented
- Added strip argument. Example: `/freeze player -S`
- Added wiki to github
- Autocompletion for offline players
- Autocompletion for arguments

### V1.14.3
#### Features
- Allow reporting/warning offline users
- Locations implemented
- Freeze command now has the ability to take a teleportLocation parameter `/freeze myplayer -Tspawn`

### V1.14.1
#### Bugfix
- Logout commands on freeze can be disabled configuring: `logout-commands: ''`

