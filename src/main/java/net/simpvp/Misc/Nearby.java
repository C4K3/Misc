package net.simpvp.Misc;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Nearby implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			sender.sendMessage("Only players can use this command.");
			return true;
		}

		if (!player.isOp()) {
			player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return true;
		}

		Integer radius = null;
		if (args.length > 0) {
			try {
				radius = Integer.parseInt(args[0]);
			} catch (Exception e) {
				player.sendMessage(ChatColor.RED + "Invalid integer '" + args[0] + "': " + e);
				return true;
			}
		}

		Location loc = player.getLocation();

		if (radius == null) {
			Player nearest = null;
			Double nearest_distance = null;
			for (Player p : loc.getWorld().getPlayers()) {
				if (p.getEntityId() == player.getEntityId()) {
					continue;
				}

				double p_distance = loc.distanceSquared(p.getLocation());
				if (nearest_distance == null || p_distance < nearest_distance) {
					nearest = p;
					nearest_distance = p_distance;
				}
			}

			if (nearest == null) {
				sender.sendMessage(ChatColor.GREEN + "There are no players in the same world as you.");
			} else {
				double distance = loc.distance(nearest.getLocation());
				sender.sendMessage(String.format(ChatColor.GREEN + "The nearest player is %s who is %.0f blocks away.", nearest.getName(), loc.distance(nearest.getLocation())));
			}
		} else {
			ArrayList<Player> within = new ArrayList<Player>();

			double radius_sq = (double) radius * (double) radius;

			// Don't use getNearbyEntities as the number of entities will
			// tend to be significantly greater than the number of players.
			for (Player p : loc.getWorld().getPlayers()) {
				if (p.getEntityId() == player.getEntityId()) {
					continue;
				}

				if (loc.distanceSquared(p.getLocation()) < radius_sq) {
					within.add(p);
				}
			}

			if (within.isEmpty()) {
				player.sendMessage(ChatColor.GREEN + "There is nobody within " + radius + " blocks.");
			} else {
				if (within.size() == 1) {
					sender.sendMessage(String.format("%sThere is 1 player within %d blocks:", ChatColor.GREEN, radius));
				} else {
					sender.sendMessage(String.format("%sThere are %d players within %d blocks:", ChatColor.GREEN, within.size(), radius));
				}

				for (Player p : within) {
					player.sendMessage(String.format("  %s%s is %.0f blocks away.", ChatColor.GREEN, p.getName(), loc.distance(p.getLocation())));
				}
			}
		}

		return true;
	}
}
