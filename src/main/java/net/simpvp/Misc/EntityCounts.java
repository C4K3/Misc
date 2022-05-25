package net.simpvp.Misc;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityCounts implements CommandExecutor, TabCompleter {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player && !sender.isOp()) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return false;
		}

		Map<String, Integer> counts;
		String msg;

		command:
		switch(args.length) {
			case 0:
				counts = getAllEntityCounts(null);
				msg = "Entity count:\n ";
				break;
			case 2:
				switch(args[0]) {
					case "type":
						try {
							EntityType type = EntityType.valueOf(args[1].toUpperCase());
							counts = getAllEntityCounts(e -> e.getType() == type);
							msg = args[1] + " count:\n ";
							break command;
						} catch (IllegalArgumentException e) {
							sender.sendMessage(ChatColor.RED + "Unknown entity type: " + args[1]);
							return false;
						}
					case "player":
						Player target = Misc.instance.getServer().getPlayer(args[1]);
						if (target != null) {
							counts = detailedEntityCount(target);
							msg = "Detailed entity count for " + args[1] + ":\n ";
							break command;
						} else {
							sender.sendMessage(ChatColor.RED + "Player is not online.");
							return false;
						}
				}
			default:
				sender.sendMessage(ChatColor.RED + "Usage: /entitycounts [type|player] [option]");
				return false;
		}

		msg += counts
				.entrySet().stream()
				.filter(e -> e.getValue() > 0)
				.sorted((a, b) -> b.getValue().compareTo(a.getValue()))
				.map(e -> e.getKey() + ": " + e.getValue())
				.collect(Collectors.joining(", "));
		sender.sendMessage(msg);
		return true;
	}

	/**
	 * Short form to run getEntityCount for all online players
	 * @param matching Predicate to filter entities, can be set to null to return everything
	 * @return Map of all players and the number of matched entities around them
	 */
	private static Map<String, Integer> getAllEntityCounts(Predicate<Entity> matching) {
		return Misc.instance.getServer().getOnlinePlayers().stream()
				.collect(Collectors.toMap(Player::getName, p -> getEntityCount(p, matching)));
	}

	/**
	 * Find number of entities near a player potentially matching some filter
	 * @param player Player to check
	 * @param matching Predicate to filter entities, can be set to null to return everything excluding hanging entities
	 *                 (item frames, paintings, variants)
	 * @return Number of matched entities
	 */
	private static int getEntityCount(Player player, Predicate<Entity> matching) {
		Stream<Chunk> chunks = getChunks(player).stream();
		final Predicate<Entity> finalMatching = matching == null ? e -> !(e instanceof Hanging) : matching;
		return chunks.mapToInt(c -> (int) (Arrays.stream(c.getEntities()).filter(finalMatching).count())).sum();
	}

	/**
	 * Get detailed count of all different entity types near a specific player
	 * @param player Player to check
	 * @return Map of all found entity types and their count
	 */
	private static Map<String, Integer> detailedEntityCount(Player player) {
		HashMap<String, Integer> counts = new HashMap<>();
		getChunks(player).stream()
				.flatMap(c -> Arrays.stream(c.getEntities()))
				.forEach(entity -> counts.merge(entity.getType().name().toLowerCase(), 1, Integer::sum));
		return counts;
	}

	/**
	 * Get all chunks around a player within server view distance
	 * @param player Player to find chunks
	 * @return All nearby chunks
	 */
	private static List<Chunk> getChunks(Player player) {
		List<Chunk> chunks = new ArrayList<>();
		World world = player.getWorld();
		Chunk mid = player.getLocation().getChunk();
		int midX = mid.getX();
		int midZ = mid.getZ();
		int dist = Misc.instance.getServer().getViewDistance();

		for (int x = -dist; x <= dist; x++) {
			for (int z = -dist; z <= dist; z++) {
				chunks.add(world.getChunkAt(midX + x, midZ + z));
			}
		}
		return chunks;
	}

	private static final List<String> entityTypeList = Arrays.stream(EntityType.values())
			.map(e -> e.name().toLowerCase())
			.collect(Collectors.toList());

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.isOp()) return null;

		String typing = args[args.length-1];
		switch(args.length) {
			case 1: return autoComplete(typing, Arrays.asList("type", "player"));
			case 2:
				switch (args[0]) {
					case "type": return autoComplete(typing, entityTypeList);
					case "player": return null; // Returning null shows tab completions for online player names
				}
			default: return new ArrayList<>(); // Returning an empty list shows no tab completions
		}
	}

	/**
	 * Gives autocomplete suggestion for passed argument and list of valid options
	 * @param arg Current argument being typed
	 * @param list List of options, such as all entity types
	 * @return List of matching options from what is typed so far
	 */
	private static List<String> autoComplete(String arg, List<String> list) {
		return StringUtil.copyPartialMatches(arg, list, new ArrayList<>());
	}
}
