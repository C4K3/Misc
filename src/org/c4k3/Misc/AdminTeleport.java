package org.c4k3.Misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminTeleport implements CommandExecutor {
	/* Adds the /tp, /tpc and /tps commands */
	
	private String scmd;
	
	private int argslength;
	
	private Player tplayer;
		
	private Location loc = new Location(null, 0, 0, 0);
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		scmd = cmd.getName();
		
		Player player = null;
		if (sender instanceof Player){
			player = (Player) sender;
		}
		
		if ( player == null ) {
			/* Sender is not a player */
			Bukkit.getLogger().info("Only players can use this command");
			return false;
		}
		
		/* /tp command
		 * Teleports sender to target player
		 * Autofills names
		 */
		if ( scmd.equals("tp") ) {
			
			if ( sender.isOp() ) {
				/* Sender is OP */
				
				argslength = args.length;
				
				if ( argslength == 1 ) {
					/* Args are of proper length */
					
					tplayer = Bukkit.getPlayer(args[0]);
					
					if ( tplayer == null ) {
						/* Target player is null (player was not found) */
						sender.sendMessage(ChatColor.RED + "Target player not found");
						return false;
						
					} else {
						/* Target player was found (teleporting sender to target) */
						sender.sendMessage("Teleporting you to " + tplayer.getName());
						player.teleport(tplayer);
						return false;
					}
					
				}
				
				if ( argslength == 0 ) {
					/* Arguments length is 0 (sender didn't specify any target) */
					sender.sendMessage(ChatColor.RED + "You need to specify a target");
					return false;
				}
				
				if ( argslength > 1 ) {
					sender.sendMessage(ChatColor.RED + "You cannot specify more than one target");
					return false;
				}
				
			} else {
				/* Sender is not OP */
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return false;
			}
			
		}
		
		/*/tps command
		 * Teleports target to sender
		 * Autofills names
		 */
		if ( scmd.equals("tps") ) {
			
			if ( sender.isOp() ) {
				/* Sender is OP */
				
				argslength = args.length;
				
				if ( argslength == 1 ) {
					/* Correct amount of arguments */
					
					tplayer = Bukkit.getPlayer(args[0]);
					
					if ( tplayer == null ) {
						/* Target player is null (player was not found) */
						sender.sendMessage(ChatColor.RED + "Target player not found");
						return false;
						
					} else {
						/* Target player was found (teleporting target to sender) */
						sender.sendMessage(ChatColor.GOLD + "Teleporting " + tplayer.getName() + " to you");
						tplayer.teleport(player);
						return false;
					}
					
				}
				
				if ( argslength == 0 ) {
					/* Not enough arguments (sender didn't specify a target) */
					sender.sendMessage(ChatColor.RED + "You need to specify a target");
					return false;
				}
				
				if ( argslength > 1 ) {
					/* Too many arguments */
					sender.sendMessage(ChatColor.RED + "You can only specify more than one target");
					return false;
				}
				
			} else {
				/* Sender is not OP */
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return false;
			}
			
		}
		
		/* /tpc command
		 * Teleports sender to target coordinates
		 * */
		if ( scmd.equals("tpc") ) {
			
			if ( sender.isOp() ) {
				
				argslength = args.length;
				
				if ( argslength == 3 || argslength == 4 ) {
					/* Correct amount of arguments */
					
					/* Try to convert arguments into doubles for target location */
					try {
						
					loc.setX(Integer.parseInt(args[0]));
					loc.setY(Integer.parseInt(args[1]));
					loc.setZ(Integer.parseInt(args[2]));
					
					} catch (Exception e) {
						/* Exception is most likely caused by parseInt not being able to convert string to an integer
						 * (== sender entered a string, not coordinates in the first 3 arguments)
						 */
						sender.sendMessage(ChatColor.RED + "Unable to convert input arguments into location\n" +
								"Proper syntax is /tpc <x> <y> <z> [world]");
						Bukkit.getLogger().info("parseInt exception: " + e);
						return false;
					}
					
					if ( argslength == 4 ) {
						/* The sender is trying to teleport to a specific world */
						
						try {
							/* Try to get world from fourth argument */							
							loc.setWorld(Bukkit.getWorld(args[3]));
							
							player.teleport(loc);
							
							sender.sendMessage(ChatColor.GOLD + "Teleporting you to specified location");
							
						} catch (Exception e) {
							/* Exception is most likely caused by Bukkit.getWorld being unable to get the world
							 * (== world does not exist)
							 */
							sender.sendMessage(ChatColor.RED + "Unable to get world " + args[3] +
									"\nPlease note some worlds use the world_ prefix (eg world_nether)");
							Bukkit.getLogger().info("Bukkit.getWorld exception: " + e);
							return false;
							
						}
						
					} else {
						/* Argslength is 3
						 * (== sender did not specify a world)
						 * therefore we use the world the sender is currently in
						 */						
						loc.setWorld(player.getWorld());
						
						player.teleport(loc);
						
						sender.sendMessage(ChatColor.GOLD + "Teleporting you to specified location");
						
					}
										
				} else {
					/* Incorrect amount of arguments */
					sender.sendMessage(ChatColor.RED + "Incorrect amount of arguments.\n" +
							"Proper syntax is /tpc <x> <y> <z> [world]");
					return false;
				}
				
			} else {
				/* Sender is not OP */
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return false;
			}
			
			
		}
		
		return false;
		
	}

}
