# Changelog Staff++ | v1.15

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
