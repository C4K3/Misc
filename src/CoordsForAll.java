package net.simpvp.Misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoordsForAll implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;

		if (player == null) {
			if (args.length != 1) {
				Misc.instance.getLogger().info("You need to specify an argument.");
				return true;
			}

			@SuppressWarnings("deprecation") /* Only used on online players */
			Player target = Misc.instance.getServer().getPlayer(args[0]);
			if (target == null) {
				Misc.instance.getLogger().info("The specified player was not found.");
				return true;
			}

			Location loc = target.getLocation();
			Misc.instance.getLogger().info(target.getName() + " is at "
					+ loc.getBlockX() + " "
					+ loc.getBlockY() + " "
					+ loc.getBlockZ() + " "
					+ loc.getWorld().getName());

		} else {
			Location loc = player.getLocation();
			int x = loc.getBlockX();
			int y = loc.getBlockY();
			int z = loc.getBlockZ();
			sender.sendMessage(ChatColor.GOLD + " Your coordinates are:\n" +
					" World = " + loc.getWorld().getName() + "\n" +
					" X = " + x + "\n" +
					" Y = " + y + "\n" +
					" Z = " + z);
		}

		return true;
	}

}

