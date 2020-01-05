package net.simpvp.Misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * This class logs all user commands, including cancelled commands
 */
public class LogCmd implements Listener {

	/* Sets this class to register events */
	public LogCmd(Misc plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=false)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Location loc = event.getPlayer().getLocation();

		String msg = "At " + loc.getWorld().getName()
			+ " " + loc.getBlockX()
			+ " " + loc.getBlockY()
			+ " " + + loc.getBlockZ()
			+ " " + event.getPlayer().getName() + " entered " + event.getMessage();

		if (event.isCancelled())
			msg = "(Cancelled) " + msg;

		/* Intentional use of bukkit logger instead of plugin logger */
		Bukkit.getLogger().info(msg);
	}

}

