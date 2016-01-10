package net.simpvp.Misc;

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
	 * Amplifier (level) is set to 1 */
	private static final PotionEffect nightVisionPotionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, 10 * 60 * 20, 1);

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;

		String splayer = sender.getName();

		if (player == null) {
			sender.sendMessage("Only players can use this command.");
			return true;
		}

		if (!player.isOp()) {
			player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return true;
		}

		if (cmd.getName().equals("f")) {
			if (args.length < 1) {
				player.sendMessage(ChatColor.RED + "You need to specify a target");
				return true;
			}

			if (args.length > 1) {
				player.sendMessage(ChatColor.RED + "You cannot specify more than one target");
				return false;
			}

			@SuppressWarnings("deprecation") /* Only used on online players */
			Player target = Misc.instance.getServer().getPlayer(args[0]);

			if (target == null) {
				player.sendMessage(ChatColor.RED + "Unable to find the specified player.");
				return true;
			}

			/* If there is no saved location for the sender
			 * (if the sender is not following anyone)
			 * then save senders information */
			if (!backLoc.containsKey(splayer)) {
				backGameMode.put(splayer, player.getGameMode());
				backLoc.put(splayer, player.getLocation());
			}

			ArrayDeque<String> followedPlayersArray = followedPlayersMap.get(splayer);

			if (followedPlayersArray == null)
				followedPlayersArray = new ArrayDeque<String>();

			if (!followedPlayersArray.contains(player.getName())) {
				followedPlayersArray.add(player.getName());
				followedPlayersMap.put(splayer, followedPlayersArray);
			}

			player.setGameMode(GameMode.CREATIVE);
			player.performCommand("vanish on");

			player.teleport(target.getLocation());
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			player.addPotionEffect(nightVisionPotionEffect);

			for (Player p : Misc.instance.getServer().getOnlinePlayers() ) {
				if (p.isOp())
					p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC
							+ "[" + player.getName() + ": Following "
							+ target.getName() + "]");
			}
		} else if (cmd.getName().equals("b")) {
			if (player.isOp()) {
				Location bloc = backLoc.get(splayer);

				if (bloc == null) {
					player.sendMessage(ChatColor.RED + "You do not have any location saved.");
					return true;
				}

				backLoc.remove(splayer);
				player.teleport(bloc);
				player.setGameMode(backGameMode.get(splayer));
				backGameMode.remove(splayer);
				player.performCommand("vanish off");
				player.removePotionEffect(PotionEffectType.NIGHT_VISION);

				//unexemptPlayers(player);

				/* Notifying all OPs on the server of this */
				for (Player p : Misc.instance.getServer().getOnlinePlayers() ) {
					if (p.isOp())
						p.sendMessage(ChatColor.GRAY + ""
								+ ChatColor.ITALIC + "[" + player.getName() + ": /b]");
				}

			}

		}
		
		return true;
	}

	/* This method unexempts all the players splayer has followed
	 * from /ncp exempt, and then removes splayers array from
	 * the followedPlayersMap hashmap
	 * 
	 * This method should be run on the /b command and on OP logouts
	 */
	public static void unexemptPlayers(Player player) {
		String splayer = player.getName();

		ArrayDeque<String> followedPlayersArray = followedPlayersMap.get(splayer);

		if (followedPlayersArray == null)
			followedPlayersArray = new ArrayDeque<String>();

		for (String followedPlayer : followedPlayersArray)
			player.performCommand("ncp unexempt " + followedPlayer);

		followedPlayersMap.remove(splayer);
	}

}

