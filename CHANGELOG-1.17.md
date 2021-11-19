# Changelog Staff++ | v1.17

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
