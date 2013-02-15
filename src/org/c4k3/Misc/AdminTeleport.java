package org.c4k3.Misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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
	
	private World world;
	
	private String splayer; //String of senders name
	
	private String stplayer; //String of targets name (if target is a player)
	
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
		
		splayer = player.getName();
		
		/* /tp command
		 * Teleports sender to target player
		 * Autofills names
		 * Syntax: /tp <target player>
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

						stplayer = tplayer.getName();
						
						/* Notifying all OPs on the server of this */
						for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

							if ( onlinePlayer.isOp()) {
								
								onlinePlayer.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + splayer + ": Teleported " + splayer + " to " + stplayer + "]");
							
							}
							
						}
						
						if ( player.isInsideVehicle() ) player.leaveVehicle();
						player.teleport(tplayer);
						
						return true;
						
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
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command\n" +
						"With the exception of /pvp and /kill, teleportation is disabled on this server");
				return false;
			}
			
		}
		
		/* /tps command
		 * Teleports target to sender
		 * Autofills names
		 * Syntax: /tps <target player>
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
						
						stplayer = tplayer.getName();
						
						/* Notifying all OPs on the server of this */
						for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

							if ( onlinePlayer.isOp()) {
								
								onlinePlayer.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + splayer + ": Teleported " + stplayer + " to " + splayer + "]");
							
							}
							
						}
						
						if ( tplayer.isInsideVehicle() ) tplayer.leaveVehicle();
						tplayer.teleport(player);
						
						return true;
					
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
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command\n" +
						"With the exception of /pvp and /kill, teleportation is disabled on this server");
				return false;
			}
			
		}
		
		/* /tpc command
		 * Teleports sender to target coordinates
		 * If a 4th argument is supplied, it is taken as the target world
		 * Syntax: /tpc <x> <y> <z> [world]
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
						sender.sendMessage(ChatColor.RED +  "Unable to convert input arguments into location\n" +
								"Proper syntax is /tpc <x> <y> <z> [world]");
						Bukkit.getLogger().info("Expected parseInt exception: " + e);
						return false;
					}

					if ( argslength == 4 ) {
						/* The sender is trying to teleport to a specific world */
						
						try {
							/* Try to get world from fourth argument */							
							loc.setWorld(Bukkit.getWorld(args[3]));
							
							/* Notifying all OPs on the server of this */
							for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

								if ( onlinePlayer.isOp()) {
									
									onlinePlayer.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + splayer + ": Teleported " + splayer + " to coordinates]");
								
								}
								
							}
							
							player.teleport(loc);
														
						} catch (Exception e) {
							/* Exception is most likely caused by Bukkit.getWorld being unable to get the world
							 * (== world does not exist)
							 */
							sender.sendMessage(ChatColor.RED + "Unable to get world '" + args[3] + "'" +
									"\nPlease note some worlds use the world_ prefix\n(eg world_nether, world_the_end)");
							Bukkit.getLogger().info("Expected Bukkit.getWorld exception: " + e);
							return false;
							
						}
						
					} else {
						/* Argslength is 3
						 * (== sender did not specify a world)
						 * therefore we use the world the sender is currently in
						 */						
						loc.setWorld(player.getWorld());
						
						/* Notifying all OPs on the server of this */
						for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

							if ( onlinePlayer.isOp()) {
								
								onlinePlayer.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + splayer + ": Teleported " + splayer + " to coordinates]");
							
							}
							
						}
						
						if ( player.isInsideVehicle() ) player.leaveVehicle();
						player.teleport(loc);
						
					}
										
				} else {
					/* Incorrect amount of arguments */
					sender.sendMessage(ChatColor.RED + "Incorrect amount of arguments.\n" +
							"Proper syntax is /tpc <x> <y> <z> [world]");
					return false;
				}
				
			} else {
				/* Sender is not OP */
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command\n" +
						"With the exception of /pvp and /kill, teleportation is disabled on this server");
				return false;
			}
			
			
		}
		
		/* /tpw command
		 * Teleports command sender to spawn of target world
		 * Syntax: /tpw <world>
		 */
		if ( scmd.equals("tpw")) {
			
			if ( sender.isOp() ) {
				
				/* If only one argument was supplied.
				 * Proper syntax is /tpw <world>
				 * so argument length of 1 is correct
				 */
				if ( args.length == 1 ) {
					
					try {
						/* Try to get world from argument
						 * Will error if world is not found
						 */
						world = Bukkit.getWorld(args[0]);
						
						loc = world.getSpawnLocation();
						
						/* Notifying all OPs on the server of this */
						for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

							if ( onlinePlayer.isOp()) {
								
								onlinePlayer.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + splayer + ": Teleported " + splayer + " to spawn in '" + args[0] + "']");
							
							}
							
						}
						
						if ( player.isInsideVehicle() ) player.leaveVehicle();
						player.teleport(loc);
						
					} catch (Exception e) {
						/* Exception is most likely caused by Bukkit.getWorld being unable to get the world
						 * (== world does not exist)
						 */
						sender.sendMessage(ChatColor.RED + "Unable to get world " + args[0] +
								"\nPlease note some worlds use the world_ prefix\n(eg world_nether, world_the_end)");
						Bukkit.getLogger().info("Expect Bukkit.getWorld exception: " + e);
						return false;
					}
					
				} else {
					/* Incorrect amount of arguments (suppose to be 1) */
					sender.sendMessage(ChatColor.RED + "Incorrect amount of arguments.\n"
							+ "Proper syntax is /tpw <world>");
					return false;
				}
				
				
			} else {
				/* Sender is not OP */
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command\n" +
						"With the exception of /pvp and /kill, teleportation is disabled on this server");
				return false;
			}
			
		}
		
		return false;
		
	}

}
