# Changelog Staff++ | v1.16

### V1.15.19
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


