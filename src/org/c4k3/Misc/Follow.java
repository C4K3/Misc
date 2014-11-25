package org.c4k3.Misc;

import java.util.ArrayDeque;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Follow implements CommandExecutor {

	/* HashMap of senders gamemode when they used /f */
	private static HashMap<String, GameMode> backGameMode = new HashMap<String, GameMode>();

	/* HashMap of senders location when they used /f */
	private static HashMap<String, Location> backLoc = new HashMap<String, Location>();

	/* HashMap of all the players an admin is following
	 * Entries are reset on admin logouts, therefore
	 * DO NOT USE THIS HASHMAP FOR ANYTHING OTHER THAN /ncp exempt */
	private static HashMap<String, ArrayDeque<String>> followedPlayersMap = new HashMap<String, ArrayDeque<String>>();

	/* Create final variable of potion effect applied wto players for nightvision
	 * Time limit is set to 10 minutes (counted in ticks)
	 * Amplifier (level) is set to 1
	 */
	private static final PotionEffect nightVisionPotionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, 10 * 60 * 20, 1);

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

		Player player = null;
		if (sender instanceof Player){
			player = (Player) sender;
		}

		String splayer = sender.getName();

		/* If sender is not a player (== console) */
		if ( player == null ) {
			sender.sendMessage("Only players can use this command");
			return false;
		}

		if ( cmd.getName().equals("f") == true ) {

			if ( sender.isOp() == true ) {

				if ( args.length < 1  ) {
					sender.sendMessage(ChatColor.RED + "You need to specify a target");
					return false;
				}

				if ( args.length > 1 ) {
					sender.sendMessage(ChatColor.RED + "You cannot specify more than one target");
					return false;
				}

				// It is now presumed that the argument length is 1

				// tplayer == target player
				Player tplayer = Bukkit.getPlayer(args[0]);

				if ( tplayer != null ) {

					String stplayer = tplayer.getName(); // String of target players name

					// sLoc == senders location
					Location sLoc = player.getLocation();

					/* If there is no saved location for the sender
					 * (if the sender is not following anyone)
					 * then save senders information
					 */
					if ( !backLoc.containsKey(splayer)) {
						backGameMode.put(splayer, player.getGameMode());
						backLoc.put(splayer, sLoc);

					}

					/* Retrieving ArrayDeque for this admin from the HashMap */
					ArrayDeque<String> followedPlayersArray = followedPlayersMap.get(splayer);

					/* If the data retrieved from the HashMap is null, create a new empty ArrayDeque */
					if ( followedPlayersArray == null ) followedPlayersArray = new ArrayDeque<String>();

					/* If the target player is not already in the followedPlayers array
					 * Adds the target player to the array, and then puts the array into followedPlayersMap
					 * and exempts the target player from NoCheatPlus
					 */
					if ( !followedPlayersArray.contains(stplayer)) {
						followedPlayersArray.add(stplayer);
						//player.performCommand("ncp exempt " + stplayer);
						followedPlayersMap.put(splayer, followedPlayersArray);
					}

					player.setGameMode(GameMode.CREATIVE);
					player.performCommand("vanish on");

					// tloc == target players location
					player.teleport(tplayer);
					player.removePotionEffect(PotionEffectType.NIGHT_VISION);
					player.addPotionEffect(nightVisionPotionEffect);

					/* Notifying all OPs on the server of this */
					for (Player onlinePlayer : Bukkit.getOnlinePlayers() ) {

						if ( onlinePlayer.isOp()) {

							onlinePlayer.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + splayer + ": Following " + stplayer + "]");

						}

					}

				} else {
					sender.sendMessage(ChatColor.RED + "Target player not found");
				}

			} else {
				// If player is not OP
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			}

			return false;

		}
		/* End of /follow */

		/* /goback command */
		if ( cmd.getName().equals("b") == true ) {

			if ( player.isOp() == true ) {

				Location bLoc = backLoc.get(splayer);
				if ( bLoc != null ) {
					
					//TODO
					/* Add +1 to y as a workaround of people getting stuck */
					//bLoc.setY(bLoc.getY() + 1.0);
					Location fLoc = new Location(bLoc.getWorld(), bLoc.getBlockX(), bLoc.getBlockY() - 1, bLoc.getBlockZ() );
					player.sendBlockChange(fLoc, fLoc.getBlock().getType(), fLoc.getBlock().getData());

					/* Undoing effects of /f (as many as possible) */
					backLoc.remove(splayer);
					player.teleport(bLoc);
					player.setGameMode(backGameMode.get(splayer));
					backGameMode.remove(splayer);
					player.performCommand("vanish off");
					player.removePotionEffect(PotionEffectType.NIGHT_VISION);

					//unexemptPlayers(player);

					/* Notifying all OPs on the server of this */
					for (Player onlinePlayer : Bukkit.getOnlinePlayers() ) {

						if ( onlinePlayer.isOp()) {

							onlinePlayer.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + splayer + ": /b]");

						}

					}

					return false;

				} else {
					// If bLoc == null
					sender.sendMessage(ChatColor.RED + "You do not have any location saved");
					return false;
				}

			} else {
				// If player is not OP
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return false;
			}

		}

		return false;
	}

	/* This class unexempts all the players splayer has followed
	 * from /ncp exempt, and then removes splayers array from
	 * the followedPlayersMap hashmap
	 * 
	 * This method should be run on the /b command and on OP logouts
	 */
	public static void unexemptPlayers(Player player) {

		String splayer = player.getName();

		/* Retrieving array from followedPlayersMap
		 * Then unexmpting each player in the array from /ncp exempt
		 * Then removing array from followedPlayersMap
		 */
		ArrayDeque<String> followedPlayersArray = followedPlayersMap.get(splayer);

		if ( followedPlayersArray == null ) followedPlayersArray = new ArrayDeque<String>();

		for ( String followedPlayer : followedPlayersArray ) {

			player.performCommand("ncp unexempt " + followedPlayer);

		}

		followedPlayersMap.remove(splayer);

	}

}
