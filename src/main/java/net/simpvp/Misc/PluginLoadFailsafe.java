package net.simpvp.Misc;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Check that all plugins are loaded, and shutdown the server if a plugin has
 * failed to load.
 */
public class PluginLoadFailsafe {
	public static void schedule_check() {
		new BukkitRunnable() {
			@Override
			public void run() {
				check_plugins_loaded();
			}
		}.runTaskLater(Misc.instance, 1L);
	}

	private static void check_plugins_loaded() {
		Server server = Misc.instance.getServer();
		PluginManager pm = server.getPluginManager();

		boolean success = true;
		String err = "Shutting down servers due to failed plugins.";

		for (Plugin p : pm.getPlugins()) {
			if (p.isEnabled()) {
				continue;
			}

			err += String.format("\nPlugin %s failed to load.", p.getName());
			success = false;
		}

		if (!success) {
			Misc.instance.getLogger().severe(err);
			server.shutdown();
		}
	}
}

