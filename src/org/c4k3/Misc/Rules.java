package org.c4k3.Misc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Rules implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		sender.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "=== RULES ===");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.DARK_AQUA + "#1" + ChatColor.GOLD + " Griefing/killing/stealing is " + ChatColor.BOLD + "allowed");
		sender.sendMessage(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
		sender.sendMessage(ChatColor.DARK_AQUA + "#2" + ChatColor.GOLD + " No cheats/hacks/spam. This includes: Flymod, x-ray and manual spam. It's a zero tolerance policy; no warnings, no second chances, no excuses. Ignorance is not an excuse");
		sender.sendMessage(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
		sender.sendMessage(ChatColor.DARK_AQUA + "#3" + ChatColor.GOLD + " Asking admins for free stuff/day/night/teleport is strictly forbidden, punished by being called a " + ChatColor.ITALIC + "noob");
		
		return false;
		
	}

}
