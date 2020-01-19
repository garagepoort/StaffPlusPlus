package net.shortninja.staffplus.server.hook;

public interface IHook {

	/**
	 * The name of the hooked plugin.
	 *
	 * @return The plugin name.
	 * @since 3.2.9
	 */
	String getPluginName();

	/**
	 * The version of the hooked plugin.
	 *
	 * @return The plugin version.
	 * @since 3.2.9
	 */
	String getPluginVersion();

	/**
	 * When enabling the hook.
	 *
	 * @since 3.2.9
	 */
	void onEnable();

	/**
	 * When disabling the hook.
	 *
	 * @since 3.2.9
	 */
	void onDisable();

	/**
	 * Whether the hook can actually hook.
	 *
	 * @return True if can be hooked otherwise false.
	 * @since 3.2.9
	 */
	boolean canHook();
}
