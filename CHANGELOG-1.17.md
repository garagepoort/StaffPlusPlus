# Changelog Staff++ | v1.17

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
  - Fix Session Exception on EntityTargetLivingEntityEven
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
