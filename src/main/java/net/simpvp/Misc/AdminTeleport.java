package net.simpvp.Misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.command.BlockCommandSender;

/**
 * Adds the /tp, /tpc, /tpw and /tpo commands
 */
public class AdminTeleport implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;

		if (player != null && !player.isOp()) {
			player.sendMessage(ChatColor.RED
					+ "You do not have permission to use this command.");
			return true;
		}

		String scmd = cmd.getName();

		if (scmd.equals("tpc")) {
			tpc(sender, args);
		} else if (scmd.equals("tpo")) {
			tpo(player, args);
		} else if (scmd.equals("tpw")) {
			tpw(player, args);
		}

		return true;

	}

	/**
	 * Teleports the sender (or other player if specified) to the given coords
	 *
	 * This command takes a CommandSender instead of player, so that we can
	 * get the coords of command blocks, if set, so that we can understand
	 * relative coordinates from command blocks.
	 */
	private void tpc(CommandSender sender, String[] args) {
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;

		BlockCommandSender cb = null;
		if (sender instanceof BlockCommandSender)
			cb = (BlockCommandSender) sender;

		if (args.length > 5 || args.length < 3) {
			tpc_usage(player, "Invalid number of arguments.");
			return;
		}

		if (args.length < 5 && player == null) {
			tpc_usage(player, "If you want to use this command as a non-player,"
					+ " you must specify a player.");
			return;
		}

		Location loc = null;
		if (player != null) {
			loc = player.getLocation();
		} else if (sender != null) {
			loc = cb.getBlock().getLocation();

			/* Add 0.5 to each of the coordinates, so the player spawns in the center
			 * of the block instead of at the edge. */
			loc.setX(loc.getX() + 0.5);
			loc.setY(loc.getY() + 0.5);
			loc.setZ(loc.getZ() + 0.5);
		} else {
			loc = Misc.instance.getServer().getWorld("world").getSpawnLocation();
		}

		try {
			if (args[0].equals("~")) {
				// NOP
			} else if (args[0].startsWith("~")) {
				loc.setX(loc.getX() + Integer.parseInt(args[0].substring(1)));
			} else {
				loc.setX(Integer.parseInt(args[0]) + 0.5);
			}

			if (args[1].equals("~")) {
				// NOP
			} else if (args[1].startsWith("~")) {
				loc.setY(loc.getY() + Integer.parseInt(args[1].substring(1)));
			} else {
				loc.setY(Integer.parseInt(args[1]) + 0.5);
			}

			if (args[2].equals("~")) {
				// NOP
			} else if (args[2].startsWith("~")) {
				loc.setZ(loc.getZ() + Integer.parseInt(args[2].substring(1)));
			} else {
				loc.setZ(Integer.parseInt(args[2]) + 0.5);
			}
		} catch (Exception e) {
			tpc_usage(player, "Unable to parse coordinates to numbers.");
			return;
		}

		if (args.length > 3) {
			World world = Misc.instance.getServer().getWorld(args[3]);
			if (world == null) {
				tpc_usage(player, "No such world found. Note that some worlds"
						+ " are prepended with world_, e.g. world_the_nether");
				return;
			}

			loc.setWorld(Misc.instance.getServer().getWorld(args[3]));

		} else if (player != null) {
			loc.setWorld(player.getWorld());
		} else {
			tpc_usage(player, "If you want to use this command as a non-player,"
					+ " you have to specify a world.");
			return;
		}

		Player target;
		if (args.length > 4) {
			/* do it this way because for some reason you can't use annotations
			 * on simple assignments */
			@SuppressWarnings("deprecation") /* Only used for currently online players */
			Player tmp = Misc.instance.getServer().getPlayer(args[4]);
			target = tmp;

			if (target == null) {
				tpc_usage(player, "The specified player was not found.");
				return;
			}
		} else {
			target = player;
		}

		if (target.isInsideVehicle())
			target.leaveVehicle();
		target.teleport(loc);

		notify_admins(player, "Teleported " + target.getName() + " to coordinates");
	}

	/**
	 * Displays usage info for tpc
	 */
	private void tpc_usage(Player player, String error) {
		error += "\n"
			+ "Proper syntax is /tpc <x> <y> <z> [world] [player]";

		if (player == null) {
			Misc.instance.getLogger().info(error);
		} else {
			player.sendMessage(ChatColor.RED + error);
		}
	}

	/**
	 * Teleports the given player to the command sender
	 */
	private void tpo(Player player, String[] args) {
		if (player == null) {
			Misc.instance.getLogger().info("You cannot use this command.");
			return;
		}

		if (args.length != 1) {
			player.sendMessage(ChatColor.RED + "Invalid amount of arguments.\n"
					+ "Proper usage is: /tpo <player>");
			return;
		}

		@SuppressWarnings("deprecation") /* Only used for online players */
		Player target = Misc.instance.getServer().getPlayer(args[0]);

		if (target == null) {
			player.sendMessage(ChatColor.RED + "The specified player was not found.");
			return;
		}

		if (target.isInsideVehicle())
			target.leaveVehicle();
		target.teleport(player.getLocation());

		notify_admins(player, "Teleported " + target.getName() + " to " + player.getName());
	}

	/**
	 * Teleports the sender to the spawn point of the given world
	 */
	private void tpw(Player player, String[] args) {
		if (player == null) {
			Misc.instance.getLogger().info("You cannot use this command.");
			return;
		}

		if (args.length != 1) {
			player.sendMessage(ChatColor.RED + "Invalid amount of arguments.\n"
					+ "Proper usage is: /tpw <world>");
			return;
		}

		World world = Misc.instance.getServer().getWorld(args[0]);

		if (world == null) {
			player.sendMessage(ChatColor.RED + "No such world found. Note that some worlds"
					+ " are prepended with world_, e.g. world_the_nether");
			return;
		}

		if (player.isInsideVehicle())
			player.leaveVehicle();
		player.teleport(world.getSpawnLocation());

		notify_admins(player, "Teleported " + player.getName()
				+ " to spawn in '" + world.getName() + "'");
	}

	/**
	 * Notifies all admins on the server of the action taken by sender.
	 */
	private void notify_admins(Player sender, String message) {
		String ssender;
		if (sender == null) {
			ssender = "CONSOLE";
		} else {
			ssender = sender.getName();
		}

		for (Player p : Misc.instance.getServer().getOnlinePlayers()) {
			if (p.isOp())
				p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC
						+ "[" + ssender + ": " + message + "]");
		}

		/* Intentional use of bukkit logger */
		Bukkit.getLogger().info("[" + ssender + ": " + message + "]");
	}

}

