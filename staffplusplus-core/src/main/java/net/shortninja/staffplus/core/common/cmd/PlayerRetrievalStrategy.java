package net.shortninja.staffplus.core.common.cmd;

/**
 * Determines how the player should be retrieved when executing a command.
 * Options:
 *         - ONLINE
 *           If specified the command will try to find an online player. If none found the command fails with error
 *         - BOTH
 *           If specified the command will try to find an online or offline player. If none found the command fails with error
 *         - NONE
 *           The command will never try to find a target player
 *         - OPTIONAL_ONLINE
 *           If specified the command will try to find an online player. If none found the command will not fail
 *         - OPTIONAL_BOTH
 *           If specified the command will try to find an online or offline player.  If none found the command will not fail
 */
public enum PlayerRetrievalStrategy {
    ONLINE,
    BOTH,
    NONE,
    OPTIONAL_BOTH,
    OPTIONAL_ONLINE
}
