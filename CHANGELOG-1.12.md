# Changelog Staff++ | v1.12

### V1.12.24
### Features
- Rework miner GUI to be more consistent with the other GUIs, pagination + back button
- Add notify warnings message to the lang files
- Add infractions-top command to list players with most infractions.

### Bugs
- Adding notes through the examine gui was broken.
- Adding warnings through the examine gui was broken.
- Custom-modules got broken in the previous release.


### V1.12.23
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

### V1.12.22
### Features
- Reworked chest interaction
- Added inventory & enderchest interaction to examine GUI
- Added permission for using the vanish command

### Bugs
- inventory interaction was not working even when correct permission were given
- Location inside the gui was showing a question mark character.

### V1.12.20
### Features
- mute/unmute permission split up into 2 different permissions
- ban/unban permission split up into 2 different permissions
- Examine mode now available for offline players
- Implemented enderchest interaction online and offline
- Added custom GUI for Staff Mode. https://staffplusplus-minecraft.gitbook.io/staffplusplus/features/staff-mode/custom-gui

#### Bugs
- Permission for the /reports command was not working properly

### V1.12.19
### Features
- Added mute/temp mute functionality
- Added infractions gui menu.
- Added kick command.
- Update discord integration plugin for kicks and mutes.

#### Bugs
- Time unit four HOUR/h was not working
- Fix duration if expired

### V1.12.16
#### Breaking
- Implemented fine-grained permissions for reports. Permissions have changed, check [reports wiki](https://github.com/garagepoort/StaffPlusPlus/wiki/Reporting#report-permission)! 

#### Features
- Added manage-reports command to open the GUI without being in staff mode
- Add Xray configuration to allow amount/time based notifications. Check [wiki](https://github.com/garagepoort/StaffPlusPlus/wiki/Alerting-System#x-ray) 

#### Bugs
- Added missing "clear inventory bypass" permission to the default config file.
- Fix reports sometimes not showing last line from reason
- Fix `/help staffplus` command

### V1.12.15
#### Bugs
- Nullpointer fix on alt detection
- Create a new permission to ensure not everyone gets report updates

### V1.12.14
#### Features
- Severity levels now working for warnings issued through examine mode
- Ability to interact with the players inventory in examine mode.
- Add reload configuration ability (experimental, see wiki)

#### Bugs
- Unban command logged the kick issuer instead of the unban issuer
- Fix nullpointer OnInventoryOpen
- Fix alt account detection, do not detect same player
- Fix Inventory item duplication on rejoin

### V1.12.12
#### Features
- rename 'teleport' command to 'teleportToLocation'
- Add 'teleportHere' command
- Add vanish indicator in the staff list gui
- Add kick reason placeholder to use for the kick message

#### Bugs
- StaffChat not turning off when using the handle
- Staff items triggered by pressure plates
- Better error messages for tempban

### V1.12.10
#### Features
- Teleport players back to their original location

#### Bugs
- Freezing not working through the gui or staff item
- Staff items only worked when clicking on air

#### Bugs
- Fix reset of armor when exiting Staff mode
- Fix commands plugin prefix
### V1.12.9
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

### V1.12.8
#### Bugs
- Wrong titles for reports ui
- Closing reason message displayed even when closing-reason-disabled
- Closing reason not working if other plugins intercept the event

### V1.12.7
#### Features
- Implement kick & tempban system (https://github.com/garagepoort/StaffPlusPlus/issues/106)
- Implement kick discord integration
- Implement Alt account detection system
- Implement Alt account detection discord integration
- Add option to disable bungee network sync for staffchat (https://github.com/garagepoort/StaffPlusPlus/issues/111)

#### Bugs
- Fix miner gui
- Fix prevent inventory open in protected areas
- Fix performance when autocompleting offline players (https://github.com/garagepoort/StaffPlusPlus/issues/111)

### V1.12.6
#### Features
- Trello integration added for reports
#### Bugs
- Report resolved event was sent when a report was actually rejected

### V1.12.5
#### Features
- Protect player added
- Protect area added
- "Vanish enabled" action bar displayed for staff members
- Now able to add worlds to the location configuration

### V1.12.4
#### Features
- Broadcast messages over the bungee network
#### Bugs
- Older version of 1.12.x were not working

### V1.12.2
#### Features
- Report closing reason support
- Authme integration for supporting staff login
- Trace log feature added

### V1.12.1
#### Features
- New arguments system implemented
- Added strip argument. Example: `/freeze player -S`
- Allow reporting/warning offline users
- Locations implemented
- Added HEALTH argument
- New reporting system implemented
- Implement new reportEvents
- Added Discord Integration for reporting events
- Teleport command 
- Delay argument has been added
- Implemented automatic update for the config file 
- Add clear inventory bypass permission
- BungeeCord support. StaffChat will now be synced over all servers inside the bungee network.
- Update notifier implemented
- Warning discord integration

#### Breaking
- Dropped support for flatfiles
- Implemented Sqlite, Mysql databases
- Added new warning system

#### Bugs
- Resolved issue: https://github.com/garagepoort/StaffPlusPlus/issues/91
Items got removed when inspecting a chest. 