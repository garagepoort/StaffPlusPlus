# Changelog Staff++ | v1.14

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

