# Changelog Staff++ | v1.15

### V1.15.22 [NOT RELEASED]
#### Features
- Implement ban & tempban system
- Implement ban discord integration
- Implement Alt account detection system
- Implement Alt account detection discord integration
- Add option to disable bungee network sync for staffchat

#### Bugs
- Fix miner gui
- Fix prevent inventory open in protected areas
- Fix ui color choosing
- Fix performance when autocompleting offline players

### V1.15.21
#### Features
- Trello integration added for reports
#### Bugs
- Report resolved event was sent when a report was actually rejected

### V1.15.20
#### Features
- Protect player added
- Protect area added
- "Vanish enabled" action bar displayed for staff members
- Now able to add worlds to the location configuration

### V1.15.19
#### Features
- Broadcast messages over the bungee network
#### Bugs
- Older version of 1.15.x were not working

### V1.15.17
#### Features
- Report closing reason support
- Authme integration for supporting staff login
- Trace log feature added

### V1.15.16
#### Features
- Warning discord integration

### V1.15.15
#### Features
- BungeeCord support. StaffChat will now be synced over all servers inside the bungee network.
- Update notifier implemented

### V1.15.14
#### Bugs
- Players could always bypass clear, freeze and teleport commands
- Resolved issue: https://github.com/garagepoort/StaffPlusPlus/issues/91
Items got removed when inspecting a chest. 

### V1.15.13
#### Features
- Add clear inventory bypass permission

#### Bugs
- Plugins directory was wrongly named which made it crash on Linux machines 

### V1.15.11
#### Features
- Delay argument has been added
- Implemented automatic update for the config file 

#### Bugs
- teleport command was not present in the configuration file

### V1.15.10
#### Bugs
- DELAY Action type not working when users are online

### V1.15.9
#### Features
- Teleport command added

#### Breaking
- Added new warning system

### V1.15.7
#### Features
- Implement new reportEvents
- Added Discord Integration for reporting events

#### Bugs
- Use ZonedDateTimeFor Reporting
- Remove silent sql failure

### V1.15.6
#### Features
- Added HEALTH argument
- New reporting system implemented

#### Bugs
- Fix bug when opening different inventories (BrewingStand, Furnace, Dispenser, ...)

#### Breaking
- Dropped support for flatfiles
- Implemented Sqlite, Mysql databases

### V1.15.5
#### Features
- New arguments system implemented
- Added strip argument. Example: `/freeze player -S`
- Added wiki to github
- Autocompletion for offline players
- Autocompletion for arguments

### V1.15.4
#### Features
- Locations implemented
- Freeze command now has the ability to take a teleportLocation parameter `/freeze myplayer -Tspawn`

### V1.15.3
#### Features
- Allow reporting/warning offline users

### V1.15.2
#### Bugfix
- Logout commands on freeze can be disabled configuring: `logout-commands: ''`
