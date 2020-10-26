# Changelog Staff++ | v1.12

### V1.12.11
#### Features
- rename 'teleport' command to 'teleportToLocation'
- Add 'teleportHere' command
- Add vanish indicator in the staff list gui
- Add ban reason placeholder to use for the kick message

#### Bugs
- StaffChat not turning of when using the handle
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
- Implement ban & tempban system (https://github.com/garagepoort/StaffPlusPlus/issues/106)
- Implement ban discord integration
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