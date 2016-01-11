package net.simpvp.Misc;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A command to let /everybody/ see when somebody first joined.
 */
public class FirstJoinedCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;

		if (args.length < 1 && player == null) {
			Misc.instance.getLogger().info("You must specify a player to use this command.");
			return true;
		}

		Player target;
		if (args.length > 0) {
			@SuppressWarnings("deprecation") /* Only used for online players */
			Player tmp = Misc.instance.getServer().getPlayer(args[0]);
			target = tmp;
		} else {
			target = player;
		}

		if (target == null) {
			if (player == null) {
				Misc.instance.getLogger().info("Could not find the specified player.");
			} else {
				player.sendMessage(ChatColor.RED + "Could not find the specified player.");
			}

			return true;
		}

		long days = ((System.currentTimeMillis() - target.getFirstPlayed()) / (1000L * 60 * 60 * 24));
		SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy");
		String msg = target.getName() + " first joined on "
			+ ChatColor.AQUA + sdf.format(new Date(target.getFirstPlayed()))
			+ ChatColor.GREEN + ", " + days + " day";
		if (days != 1)
			msg += "s";
		msg += " ago. Note that for people who joined before 2012,"
		       + " this command may show a later date than the true date.";

		if (player == null) {
			Misc.instance.getLogger().info(msg);
		} else {
			player.sendMessage(ChatColor.GREEN + msg);
		}

		return true;
	}

}

