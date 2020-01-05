package net.simpvp.Misc;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Date;

public class DateCommand implements CommandExecutor {
	/*
	 * Adds the /date command -- analogous to unix date (without any options)
	 */

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		String date = new Date().toString();

		if (player != null) {
			player.sendMessage(date);
		} else {
			if (args.length < 1) {
				sender.sendMessage("Non-players must specify a target to /date.");
				return true;
			}

			for (int i = 0; i < args.length; i++) {
				@SuppressWarnings("deprecation") /* Only used for currently online players */
				Player target = Misc.instance.getServer().getPlayerExact(args[i]);

				if (target == null) {
					sender.sendMessage("The specified player " + args[i] + " was not found.");
					continue;
				}

				target.sendMessage(date);
			}
		}

		return true;
	}
}
