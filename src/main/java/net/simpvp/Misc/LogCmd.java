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
import org.bukkit.event.server.ServerCommandEvent;

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

		if (event.isCancelled()) {
			msg = "(Cancelled) " + msg;
		}

		/* Intentional use of bukkit logger instead of plugin logger */
		Bukkit.getLogger().info(msg);
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

		/* Intentional use of bukkit logger instead of plugin logger */
		Bukkit.getLogger().info(msg);
	}

}

