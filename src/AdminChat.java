package net.simpvp.Misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminChat implements CommandExecutor {
	/* Adds the /a command that lets OPs talk in private
	 * Only OPs can use the command
	 * The commands arguments are displayed to all other OPs on the server
	 */

	String splayer;

	String sargs;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

		if ( cmd.getName().equals("a")){

			if  ( sender.isOp() ) {

				splayer = sender.getName();

				sargs = "";

				for ( int i = 0; i < args.length; i++ ) {
					sargs = sargs + " " + args[i];
				}

				Bukkit.getLogger().info("[" + splayer + "]" + sargs );

				/* For all online players, if player isOp sendMessage */
				for (Player player : Bukkit.getOnlinePlayers()) {

					if ( player.isOp()) {

						player.sendMessage("<" + ChatColor.GOLD + splayer + ChatColor.RESET + ">" + sargs );

					}

				}

			} else {
				/* If player is not OP */
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return false;
			}

		}

		return false;
	}

}

