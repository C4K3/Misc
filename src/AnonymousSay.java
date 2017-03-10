package net.simpvp.Misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AnonymousSay implements CommandExecutor {
	/** This restores the functionality of /say, where /say appeared as
	 * [Server] to everybody, no matter who used the command.
	 *
	 * I.e. it does not show the username of the admin who used /say.
	 * It also colors the text LIGHT_PURPLE, like the old default  */

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

		Player player = null;
		if (sender instanceof Player){
			player = (Player) sender;
		}

		if (player != null && !player.isOp()) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return false;
		}

		String sargs = String.join(" ", args);

		/* Intentional use of bukkit logger to avoid the [Misc]
		 * prefix */
		Bukkit.getLogger().info("[Server] " + sargs);
		for (Player p : Bukkit.getOnlinePlayers()){
			p.sendMessage(ChatColor.LIGHT_PURPLE + "[Server] " + sargs);
		}
		return true;
	}
}

