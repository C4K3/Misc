package net.simpvp.Misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * This class logs all user commands, including cancelled commands
 */
public class LogCmd {

	/* Sets this class to register events */
	public LogCmd(Misc plugin) {
		//plugin.getServer().getPluginManager().registerEvents(this, plugin);
		plugin.getServer().getPluginManager().registerEvents(new PlayerLogger(), plugin);

		if (plugin.getConfig().getBoolean("logNonPlayerCmds")) {
			new NonPlayerLogger(plugin);
		}
	}

	private class PlayerLogger implements Listener {
		@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=false)
		public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
			Location loc = event.getPlayer().getLocation();

			String msg = String.format("At '%d %d %d %s' %s entered %s",
					loc.getBlockX(),
					loc.getBlockY(),
					loc.getBlockZ(),
					loc.getWorld().getName(),
					event.getPlayer().getName(),
					event.getMessage());

			if (event.isCancelled()) {
				msg = "(Cancelled) " + msg;
			}

			/* Intentional use of bukkit logger instead of plugin logger */
			Bukkit.getLogger().info(msg);
		}
	}

	private class NonPlayerLogger implements Listener {

		private HashMap<String, Integer> repetitions = new HashMap<>();

		public NonPlayerLogger(Misc plugin) {
			plugin.getServer().getPluginManager().registerEvents(this, plugin);

			// Dump the saved repetitions every 5 minutes
			new BukkitRunnable() {
				@Override
				public void run() {
					log_repetitions();
				}
			}.runTaskTimer(plugin, 20L * 60 * 5, 20L * 60 * 5);
		}

		@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=false)
		public void onServerCommandEvent(ServerCommandEvent event) {
			String msg = "ServerCommandEvent from " + event.getSender().getName();

			if (event.getSender() instanceof BlockCommandSender) {
				Block block = ((BlockCommandSender) event.getSender()).getBlock();
				msg += String.format("(%d %d %d %s)", block.getX(), block.getY(), block.getZ(), block.getWorld().getName());
			}
			if (event.getSender() instanceof Entity) {
				Location loc = ((Entity) event.getSender()).getLocation();
				msg += String.format("(%d %d %d %s)", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName());
			}
			msg += ": " + event.getCommand();

			if (event.isCancelled()) {
				msg = "(Cancelled) " + msg;
			}

			Integer r = repetitions.putIfAbsent(msg, 0);
			if (r == null) {
				/* Intentional use of bukkit logger instead of plugin logger */
				Bukkit.getLogger().info(msg);
			} else {
				repetitions.put(msg, r + 1);
			}
		}

		@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
		public void onPluginDisable(PluginDisableEvent event) {
			log_repetitions();
		}

		private void log_repetitions() {
			for (Map.Entry<String, Integer> e : repetitions.entrySet()) {
				if (e.getValue() == 0) {
					continue;
				}
				/* Intentional use of bukkit logger instead of plugin logger */
				Bukkit.getLogger().info(String.format("Hid %d repetitions of %s", e.getValue(), e.getKey()));
			}
			repetitions.clear();
		}
	}
}
