package net.simpvp.Misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

		if (scmd.equals("tp")) {
			tp(player, args);
		} else if (scmd.equals("tpc")) {
			tpc(player, args);
		} else if (scmd.equals("tpo")) {
			tpo(player, args);
		} else if (scmd.equals("tpw")) {
			tpw(player, args);
		}

		return true;

	}

	/**
	 * Teleports sender to target player
	 * Autofills names
	 */
	private void tp(Player player, String[] args) {
		if (player == null) {
			Misc.instance.getLogger().info("You cannot use this command.");
			return;
		}

		if (args.length != 1) {
			player.sendMessage(ChatColor.RED
					+ "Invalid usage.\n"
					+ "Correct usage is: /tp <player>");
			return;
		}

		@SuppressWarnings("deprecation") /* Only used for currently online players */
		Player target = Misc.instance.getServer().getPlayer(args[0]);

		if (target == null) {
			player.sendMessage(ChatColor.RED
					+ "The specified player was not found.");
			return ;
		}

		if (player.isInsideVehicle())
			player.leaveVehicle();
		player.teleport(target);

		notify_admins(player, "Teleported " + player.getName() + " to " + target.getName());
	}

	/**
	 * Teleports the sender (or other player if specified) to the given coords
	 */
	private void tpc(Player player, String[] args) {
		if (args.length > 5 || args.length < 3) {
			tpc_usage(player, "Invalid amount of arguments.");
			return;
		}

		if (args.length < 5 && player == null) {
			tpc_usage(player, "If you want to use this command as a non-player,"
					+ " you must specify a player.");
			return;
		}

		// FIXME: See if the bogus coords are necessary to initialize
		Location loc = new Location(Misc.instance.getServer().getWorld("world"), 0, 0, 0);

		try {
			loc.setX(Integer.parseInt(args[0]) + 0.5);
			loc.setY(Integer.parseInt(args[1]) + 0.5);
			loc.setZ(Integer.parseInt(args[2]) + 0.5);
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

