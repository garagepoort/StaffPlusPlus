# Changelog Staff++ | v1.16

### V1.16.34 (NOT YET RELEASED)
#### Features
- Severity levels now working for warnings issued through examine mode
- Ability to interact with the players inventory in examine mode.

#### Bugs
- Unban command logged the ban issuer instead of the unban issuer
- Fix nullpointer OnInventoryOpen
- Fix alt account detection, do not detect same player

### V1.16.33
#### Features
- 1.16.4 support

### V1.16.32
#### Features
- rename 'teleport' command to 'teleportToLocation'
- Add 'teleportHere' command
- Add vanish indicator in the staff list gui
- Add ban reason placeholder to use for the kick message

#### Bugs
- StaffChat not turning off when using the handle
- Staff items triggered by pressure plates
- Better error messages for tempban

### V1.16.30
#### Features
- Teleport players back to their original location

#### Bugs
- Freezing not working through the gui or staff item
- Custom items enchantments not working
- Staff items only worked when clicking on air

### V1.16.29
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

### V1.16.28
#### Bugs
- Wrong titles for reports ui
- Closing reason message displayed even when closing-reason-disabled
- Closing reason not working if other plugins intercept the event

### V1.16.27
#### Features
- Implement ban & tempban system (https://github.com/garagepoort/StaffPlusPlus/issues/106)
- Implement ban discord integration
- Implement Alt account detection system
- Implement Alt account detection discord integration
- Add option to disable bungee network sync for staffchat (https://github.com/garagepoort/StaffPlusPlus/issues/111)

#### Bugs
- Fix miner gui
- Fix prevent inventory open in protected areas
- Fix ui color choosing
- Fix performance when autocompleting offline players (https://github.com/garagepoort/StaffPlusPlus/issues/111)

### V1.16.26
#### Features
- Trello integration added for reports
#### Bugs
- Report resolved event was sent when a report was actually rejected

### V1.16.25
#### Features
- Protect player added
- Protect area added
- "Vanish enabled" action bar displayed for staff members
- Now able to add worlds to the location configuration

### V1.16.24
#### Features
- Broadcast messages over the bungee network
#### Bugs
- Older version of 1.16.x were not working

### V1.16.22
#### Features
- Report closing reason support
- Authme integration for supporting staff login
- Trace log feature added

### V1.16.21
#### Features
- Warning discord integration

### V1.16.20
#### Features
- BungeeCord support. StaffChat will now be synced over all servers inside the bungee network.
- Update notifier implemented

### V1.16.19
#### Bugs
- Players could always bypass clear, freeze and teleport commands
- Resolved issue: https://github.com/garagepoort/StaffPlusPlus/issues/91
Items got removed when inspecting a chest. 

### V1.16.18
#### Features
- Add clear inventory bypass permission

#### Bugs
- Plugins directory was wrongly named which made it crash on Linux machines 

### V1.16.16
#### Features
- Delay argument has been added
- Implemented automatic update for the config file 

#### Bugs
- teleport command was not present in the configuration file

### V1.16.15
#### Bugs
- DELAY Action type not working when users are online

### V1.16.14
#### Features
- Teleport command added

#### Breaking
- Added new warning system

### V1.16.12
#### Features
- Implement new reportEvents
- Added Discord Integration for reporting events

#### Bugs
- Use ZonedDateTimeFor Reporting
- Remove silent sql failure

### V1.16.11
#### Features
- Added HEALTH argument
- New reporting system implemented

#### Bugs
- Fix bug when opening different inventories (BrewingStand, Furnace, Dispenser, ...)

#### Breaking
- Dropped support for flatfiles
- Implemented Sqlite, Mysql databases

### V1.16.10
#### Features
- New arguments system implemented
- Added strip argument. Example: `/freeze player -S`
- Added wiki to github
- Autocompletion for offline players
- Autocompletion for arguments

### V1.16.9
#### Features
- Locations implemented
- Freeze command now has the ability to take a teleportLocation parameter `/freeze myplayer -Tspawn`

### V1.16.8
#### Features
- Allow reporting/warning offline users

### V1.16.6
#### Bugfix
- Logout commands on freeze can be disabled configuring: `logout-commands: ''`


