package net.simpvp.Misc;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;

/**
 * Code for sending a configurable announcement whenever a player joins the server.
 *
 * The announcement is configured using announcement in the plugin config.yml.
 */
public class Announcement implements Listener {
	private static String announcement = null;

	public Announcement(Misc plugin) {
		String config = plugin.getConfig().getString("announcement");
		if (config != null && !config.isEmpty()) {
			this.announcement = ChatColor.AQUA + "[Announcement] " + config;
		}
	}

	@EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (announcement == null) {
			return;
		}

		Player player = event.getPlayer();
		new BukkitRunnable() {
			@Override
			public void run() {
				if (player != null) {
					player.sendMessage(announcement);
				}
			}
		}.runTaskLater(Misc.instance, 30 * 20);
	}
}
