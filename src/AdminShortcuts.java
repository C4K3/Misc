package net.simpvp.Misc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminShortcuts implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		String scmd = command.getName();

		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		/* On /dw runs the command
		 * /lb destroyed block 56 since 1d sum players world "world" */
		if (scmd.equalsIgnoreCase("dw")) {
			player.performCommand(
					"lb lookup destroyed block diamond_ore since 8d sum players world \"world\"");
			return true;
		}

		/* On /dp runs the command
		 * /lb destroyed block 56 since 1d sum players world "pvp" */
		if (scmd.equalsIgnoreCase("dp")) {
			player.performCommand(
					"lb lookup destroyed block diamond_ore since 8d sum players world \"pvp\"");
			return true;
		}

		/* On /x runs the command
		 * /lb destroyed block 56 since 1d player args[0] coords */
		if (scmd.equalsIgnoreCase("x")) {

			/* Checking that the correct amount of arguments were entered */
			if (args.length == 1) {
				player.performCommand(
						"lb lookup destroyed block diamond_ore player " + args[0] + " coords");
			} else {
				/* Incorrect amount of arguments */
				sender.sendMessage(ChatColor.RED + "Incorrect amount of arguments\n" +
						"Proper syntax is /x <player>");
				return true;
			}

		}

		return false;

	}

}

