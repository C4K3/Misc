package net.simpvp.Misc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.World.Environment;

import java.util.HashMap;
import java.util.UUID;

/**
 * Shortcuts for logblock.
 */
public class AdminShortcuts implements CommandExecutor {

	private static HashMap<UUID, String> LAST_X = new HashMap<>();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		};

		String scmd = command.getName();

		String cmd = null;

		if (scmd.equalsIgnoreCase("dw")) {
			cmd = "lb lookup destroyed block diamond_ore since 8d sum players world \"world\"";
		} else if (scmd.equalsIgnoreCase("dp")) {
			cmd = "lb lookup destroyed block diamond_ore since 8d sum players world \"pvp\"";
		} else if (scmd.equalsIgnoreCase("dn")) {
			cmd = "lb lookup destroyed block ancient_debris since 8d sum players world \"world_nether\"";
		} else if (scmd.equalsIgnoreCase("x")) {
			if (player == null) {
				sender.sendMessage("You must be a player to use this command.");
				return true;
			}

			UUID uuid = player.getUniqueId();

			String block;
			String target;
			if (args.length == 0) {
				target = LAST_X.get(uuid);
				if (target == null) {
					return x_usage(sender);
				}
			} else if (args.length == 1) {
				target = args[0];
			} else {
				return x_usage(sender);
			}

			LAST_X.put(uuid, target);

			if (player.getWorld().getEnvironment() == Environment.NETHER) {
				block = "ancient_debris";
			} else {
				block = "diamond_ore";
			}

			cmd = String.format("lb lookup destroyed block %s player %s time 0 coords", block, target);
		} else {
			return false;
		}

		Misc.instance.getServer().dispatchCommand(sender, cmd);

		return true;
	}

	private boolean  x_usage(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "Incorrect amount of arguments\n" +
				"Proper syntax is /x <player>");
		return true;
	}
}
