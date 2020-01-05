package net.simpvp.Misc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminShortcuts implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		String scmd = command.getName();

		String cmd = null;

		/* On /dw runs the command
		 * /lb destroyed block 56 since 1d sum players world "world" */
		if (scmd.equalsIgnoreCase("dw")) {
			cmd = "lb lookup destroyed block diamond_ore since 8d sum players world \"world\"";
		}

		/* On /dp runs the command
		 * /lb destroyed block 56 since 1d sum players world "pvp" */
		if (scmd.equalsIgnoreCase("dp")) {
			cmd = "lb lookup destroyed block diamond_ore since 8d sum players world \"pvp\"";
		}

		/* On /x runs the command
		 * /lb destroyed block 56 since 1d player args[0] coords */
		if (scmd.equalsIgnoreCase("x")) {

			/* Checking that the correct amount of arguments were entered */
			if (args.length == 1) {
				cmd = "lb lookup destroyed block diamond_ore player " + args[0] + " time 0 coords";
			} else {
				/* Incorrect amount of arguments */
				sender.sendMessage(ChatColor.RED + "Incorrect amount of arguments\n" +
						"Proper syntax is /x <player>");
				return true;
			}

		}

		if (cmd == null) {
			return false;
		}

		Misc.instance.getServer().dispatchCommand(sender, cmd);

		return true;

	}

}
