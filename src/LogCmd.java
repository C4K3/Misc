package net.simpvp.Misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class LogCmd implements Listener {

	/* Sets this class to register events */
	public LogCmd(Misc plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	/* On PlayerCommandPreprocessEvents (player commands) log command, sender and coordinates it was issued at */
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=false)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player target = event.getPlayer();
		Location loc = target.getLocation();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		String world = loc.getWorld().getName();
		Bukkit.getLogger().info("At " + world + " " + x + " " + y + " " + + z + " " + event.getPlayer().getName() + " entered " + event.getMessage());
	}



}

