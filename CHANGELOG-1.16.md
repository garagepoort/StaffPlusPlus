# Changelog Staff++ | v1.16

### V1.16.13 
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


