================
  Permissions
================

.. note::
    Use this to give a player all Staff+ permissions.
    Take note that this will NOT give the player the "block" permission.
    This also gives the player permission to toggle other player's vanish, staff mode, and etc.
.. code::
wild-card: "staff.*"

.. note::
    Permission for blocking listed commands at "blocked-commands" and "blocked-mode-commands".
.. code::
block: "staff.block"

.. note::
  Permission for clearing/getting player reports.
.. code::
report: "staff.report"

.. note::
    Permission for bypassing reports.
.. code:: 
report-bypass: "staff.report.bypass"

.. note::
    Permission for using "/warn" and clearing/getting player warnings.
.. code::  
warn: "staff.warn"

.. note::
  Permission for bypassing warnings.
.. code::
warn-bypass: "staff.warn.bypass"

# Permission for using staff chat.
staff-chat: "staff.staffchat"

# Permission for using "/v total".
vanish-total: "staff.vanish.total"

# Permission for using "/v list".
vanish-list: "staff.vanish.list"

# Permission for using "/chat clear".
chat-clear: "staff.chat.clear"

# Permission for using "/chat toggle".
chat-toggle: "staff.chat.toggle"

# Permission for using "/chat slow".
chat-slow: "staff.chat.slow"

# Permission for bypassing the word blacklist.
blacklist: "staff.blacklist"

# Permission for managing tickets.
tickets: "staff.tickets"

# Permission for receiving mention alerts.
mention: "staff.alerts.mention"

# Permission for receiving name change alerts.
name-change: "staff.alerts.namechange"

# Permission for receiving xray alerts.
xray: "staff.alerts.xray"

# Permission for toggling staff mode.
mode: "staff.mode"

# Permission for using the staff mode compass
compass: "staff.compass"

# Permission for using the staff mode random teleport
random-teleport: "staff.random-teleport"

# Permission for the staff mode GUI hub
gui: "staff.gui"

# Permission for the staff mode counter GUI
counter: "staff.counter"

# Permission for using freeze.
freeze: "staff.freeze"

# Permission for bypassing freeze.
freeze-bypass: "staff.freeze.bypass"

# Permission for initializing clicks per second tests
cps: "staff.cps"

# Permission for using examine
examine: "staff.examine"

# Permission for editing inventories with examine.
examine-modify: "staff.examine.modify"

# Permission for using staff mode follow
follow: "staff.follow"

# Permission for locking/unlocking the server.
lockdown: "staff.lockdown"

# Permission for giving a player's previous inventory back.
revive: "staff.revive"

# Permission for being noted as a staff member in the staff list command and for login.
# This also excludes the player from being randomly teleported to!
member: "staff.member"

# Permission for using "/strip".
strip: "staff.strip"

# Permission for use /tp
tp: "staff.tp"

# Permission for using /staffplus
staffplus: "staff.staffplus"

# Use this for players who's ip should not be shown in examine mode
ipPerm: "staff.staffplus.hideip"

# Permission for using the clear inventory config
invClear: "staff.staffplus.clearinv"

# Permission for using the /resetPassword command
resetPass: "staff.staffplus.resetpassword"
