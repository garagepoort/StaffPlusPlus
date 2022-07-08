# Changelog Staff++ | v1.19

# V1.19.3

### Features
- Bungee support for phrase detection alerts.
- Added book censor.
- Added configuration option to prevent interaction while being vanished. (pressure plates, tripwires, ...)

### Breaking change
- Rename StaffPlus to StaffPlusPlus
- GUI hub permission change. View permissions needed to see Ban and Mute information in the hub.
- Fix night-vision not turning off when exiting staff mode

### Bugs
- Discord alert notifications were still sent even if the player has the bypass permission
- Fix player detail view inventory permission.
- Long appeal reasons were not completely visible in the lore.
- Try fixing sqlite database lock errors
- Fix player detail sometimes not opening 

# V1.19.2

### Features
- 19.0 support

# V1.18.17

### Features
- Blacklist module now supports signs and anvils.
- Night vision support for vanish
- %message% placeholder added to phrase detection

### Bugs
- Fix merging option for blacklist module
- Duplication issue fixed for silent inventory interaction
- Fix phrase detection triggered on cancelled events
- Fix freeze chat channel not getting disabled by the correct configuration property

# V1.18.16

### Bugs
- Fix permission issue for staff-locations

# V1.18.15

### Features
- Added staff locations
- Prefixes in chat messages can now be ignored by prepending your message with `[NO_PREFIX]`
- Added default vanish type. Allowing to do `/vanish` without parameters.
- Vanish can now also be indicated by a bossbar
- Added vanish others permission.
- Added multiple extra options for vanish mode
- Added a vanished luckperms context
- Added tooltip support for messages. For more info see wiki.
- Added pickaxe info for xray events.
- Added kicks overview button to player detail.
- Migrations table name is now configurable

### Bugs
- Fix nullpointer when exiting staff mode
- Fix vanish sometimes not vanishing completely after relogging
- Fix spectator mode not working in staff mode.

### Features
- Add translation for player info gui
- Add /staffhub command
- Added configuration option to switch confirmation type for ip bans
- Added placeholder support for staff chat channel prefix

### Bugs
- Fix sqlite database locked error
- Fix spaces shown as %space% in staff chat commands.
- Some back actions were not working in the GUI
- Fix logger warning

# V1.18.14

### Bugs
- Fix connection leak

# V1.18.13

### Features
- Add translation for player info gui
- Add /staffhub command
- Added configuration option to switch confirmation type for ip bans
- Added placeholder support for staff chat channel prefix

### Bugs
- Fix sqlite database locked error
- Fix spaces shown as %space% in staff chat commands.
- Some back actions were not working in the GUI
- Fix logger warning

# V1.18.12

### Features
- Add optional notification sound to staffchat channels.
- Add option to disable the offline-player-cache (used for autocompletion of offline player names)

### Bugs
- Fix issue with opening my-warnings-view
- Back button was not working on some GUIs

# V1.18.11

### Bugs
- Fix 1.18.2 teleport and examine functionality for offline players

# V1.18.10

### Breaking change
  - You now need the permission "staff.examine.view-ip" to view the ip inside the examine GUI.

### Features
  - Add support for 1.18.2
  - NameChanged alerts can now be sent over bungee
  - Player mentioned alerts can now be sent over bungee
  - Xray alerts can now be sent over bungee
  - Alerts are extended with alerts for censored message triggered by the blacklist module
  - Russian language added (Thanks to: GoldenWind)
  - Gradients colors are now supported in version 1.18, 1.17 and 1.16

### Bugs
  - Fix papi placeholders for ban templates
  - Prevent wrong configuration from creating reports without reason
  - Added correct permissions for player information GUI actions
  - Fix bug with threshold calculations
  - Fix turning off silent chest interaction

# V1.18.10

### Breaking change
  - You now need the permission "staff.examine.view-ip" to view the ip inside the examine GUI.

### Features
  - Add support for 1.18.2
  - NameChanged alerts can now be sent over bungee
  - Player mentioned alerts can now be sent over bungee
  - Xray alerts can now be sent over bungee
  - Alerts are extended with alerts for censored message triggered by the blacklist module
  - Russian language added (Thanks to: GoldenWind)
  - Gradients colors are now supported in version 1.18, 1.17 and 1.16

### Bugs
  - Fix papi placeholders for ban templates
  - Prevent wrong configuration from creating reports without reason
  - Added correct permissions for player information GUI actions
  - Fix bug with threshold calculations
  - Fix turning off silent chest interaction

# V1.18.9

### Features
  - Add chat channels for reports
  - Add chat channels for frozen players
  - Add ipbans-migrate command (migrate default bukkit bans to staff++)
  - Add bans-migrate command (migrate default bukkit ipbans to staff++)
  - Add configured commands on chat phrase detection
  - The report command can now be used from console.
  - Chat phrases detection can now have actions configured
  - Added command detection.

### Bugs
  - Fix potion effects not wearing off if a frozen player logs out.
  - Fix no back button on the ban overview when coming from the hub GUI
  - Fix performance issue warn command
  - Fix list vanish not working on players joining the server

# V1.18.7

### Features
  - Added frozen luckperms context

### Bugs
  - Custom staff modules config was getting cleared automatically.

# V1.18.6

### Features
  - Teleport to player can now teleport to offline players

### Bugs
  - Fix web registration not working if servername contains spaces

# V1.18.5

### Features
  - Added my-mutes command
  - Added notification to inform a player that he is muted.

# V1.18.4

### Breaking change
  - The personnel command now requires the `staff.personnel.view` permission

### Features
  - Translation keys added for report statuses
  - Mutes can now be appealed in-game and on the web platform
  - Bans can now be appealed in-game and on the web platform
  - vanish-on-enter option added for staff mode
  - Support using "\n" for newlines in translation files
  - Added separate chat prevented translation keys for freeze and vanish

### Bugs
  - Delayed/appeal commands not working in an SQLITE database
  - Players cannot see warning detail when appeal approved
  - Fix frozen players could teleport using ender pearls

# V1.18.3

### Features
  - reduce jar size

### Bugs
  - Unresolved reports GUI could not be opened
  - Appeal GUI in 1.12 could not be opened

# V1.18.2

### Features
  - Player Notes can now be viewed, created and deleted through the web platform
  - Add translation keys for the reports GUIs

### Bugs
  - Disabling the freeze module was not working
  - Examine GUIs were not working on 1.18

# V1.18.0

### Features
  - 1.18 support
  - In a multi server setup, report/bans/mute/warning/notes data can now be configured in server groups
  - Accept/delete/resolve/reject Reports through the web platform

# V1.17.23

### Features
  - First version of GUI styling

# V1.17.22

### Bugfixes
  - Spanish translation was missing a few keys
  - On 1.12 reports were not showing up if sqlite was used
  - Fixed the disableStaffMode on world change functionality
  - Added authme to softdepends, this was making startup fail in some cases

# V1.17.20

### Features
  - Rework freeze-module, move config and add command hooks for freeze/unfreeze
  - Freeze now has an allowed-commands config which whitelists commands that can be executed while frozen  
  - Add kick-notifications permission

### Bugfixes
  - Performance issues fix

# V1.17.19

### Features
  - Night vision for staff mode
  - Staff mode now turns off potion effects on enter and restores potion effects on exit
  - The /ipbans command now takes an optional "-players" parameter which list all players banned by ip.

### Bug fixes
  - Upgrading to the newest staff++ version was not working in some cases for sqlite users
  - Soft muted players were still triggering mentions

# V1.17.18

### Bug fixes
  - Fix migrator for warning thresholds

# V1.17.17

### Features
  - Command hooks configuration has changed and improved
  - UUID on player overview is now visible even if player is offline
  - ipban and ban templates are now also displayed whenever a banned player tries to rejoin
  - Add mute reduce/extend commands
  - Add SSL option for mysql connections
  - /sfly message is now configurable in the lang files

### Bug fixes
  - Infractions menu fails to open
  - Warnings gui sometimes not opening when no appeal found
  - Setting an op player permission explicitly to false was not working

# V1.17.15

### Features
  - Added UUID to the player overview GUI

### Bug fixes
  - Fix notes migration not working for all notes.

# V1.17.14

### Breaking
  - Creation of notes now need a different permission. See the notes section on the wiki

### Features
  - Rework notes functionality
  - Added the ability to create private notes.

### Bug fixes
  - Players command was not working when warnings module disabled

# V1.17.13

### Breaking
  - permission "staff.counter-show-vanished" has been replaced with "staff.player-view.detail.vanished"

### Features
  - New Players overview guis implemented
  - New staff mode item: "Player details" stick
  - Add blocked commands for muted players
  - Soft mute implemented (Mute players without them knowing)
  - Add Indonesion language (Thanks to: Whoosh)
  - Staffchat is now forwarded to console as well

### Bug fixes
  - Report reject quick action changed status to resolved when no resolve-reason mandatory
  - Fix nullpointer on freeze mode item
  - Fix on/off vanish indicator item in staff mode
  - Vanish action notification was failing
  - Switching staff modes was broken
  - Fix performance issues with retrieving player settings.
  - Fix broadcast command not getting disabled when module disabled

# V1.17.12

### Features
  - Rework report guis to have quick actions
  - Add %server% placeholder for staffchat

### Bug fixes
  - Report detail was not showing the status
  - Disable staff mode on logout was broken  
  - MinerGui teleports player to you instead of you to the player

# V1.17.11

### Breaking change
  - temp Ban and temp mute permissions must now be limited by a separate permission. Check the wiki to see the permission needed.

### Features
  - Add staff join messages
  - Add ban reduce/extend commands
  - Ban messages are now send across the bungee network

### Bug fixes
  - Fix mobs attacking vanished players.
  - Fix Staff list only showing first page.
  - Fix command autocompletion
  - Fix database issues with ipv6 ips
  - Fix cooldown commands interfering with eachother

# V1.17.8

### Bug fixes
  - Fix performance issue with vanish and investigation action bar
  - Fix the warn command
  - Fix the infractions-top command

# V1.17.7

### Features
  - Updated the Spanish translations (Thanks to KrazyxWolf)
  - Add /clear-ips command to clear a players Ip history.
  - Add cooldown for every command in staff++

### bugfixes
  - Relocate dependencies which caused other plugins to fail (Thanks to A248)
  - Fix Session Exception on EntityTargetLivingEntityEvent
  - Fix duplicate ip entries
  - Fix reports lang migrator
  - Fix some major performance issues

# V1.17.6

### Features
  - Placeholders and formatting added for `tempmuted` and `muted` message
  - Warnings GUI now shows the player who was warned
  - New permission `staff.warnings.notifications`, player with this permission will be notified of other players getting warned.
  - Added /altcheck command.
  - New and improved report messages support.
  - tempmute command can now be delayed
  - Implemented a first version of IP-bans

### bugfixes
  - Mute was not enforced when chatting in the staffchat channels
  - preventing entity damage while in staff mode was broken. 

# V1.17.4

### Features
  - Staff chat channels formatting can now be configured per channel.
  - Added permissions for bypassing triggering alerts

### bugfixes
  - Vanish not enabled when reconnecting in staff mode
  - Fix tempmute not working through console
  - Fix ConcurrentModificationException for alt detection

# V1.17.3
## bugfixes
  - duration of tempban messages was broken

# V1.17.2
### Features
  - Add manage-bans command
  - Add PLAYER vanish type (vanishes player in game but keeps name in tablist)
  - Placeholders and formatting added for `ban-permabanned-kick` and `ban-tempbanned-kick` message
  - bans/tempbans/unbans can now be performed silently
  - Add ip-required option + SAME_IP detection level to the alt-detect module

### bugfixes
  - fixes vanish messages shown when exiting staff mode, even when vanish module was disabled
  - Fixes GUI hub sometimes not opening submenus
  - Fix tempban not working through console

# V1.17.1
### bugfixes
  - fix /eview command for offline players

# V1.17.0
### Features 
  - 1.17 added

# bugfixes
  - clean install of the plugin using sqlite was not working on versions 15 and below
  - Fix tempmute command to not try and mute an offline player
