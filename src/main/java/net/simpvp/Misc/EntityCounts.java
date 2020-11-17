package net.simpvp.Misc;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class EntityCounts implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player && !sender.isOp()) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return false;
		}

		String msg = Misc.instance.getServer().getOnlinePlayers().stream()
				.collect(Collectors.toMap(Player::getName, EntityCounts::getEntityCount))
				.entrySet().stream()
				.sorted((a, b) -> b.getValue().compareTo(a.getValue()))
				.map(e -> e.getKey() + ": " + e.getValue())
				.collect(Collectors.joining(", "));

		sender.sendMessage("Top players: " + msg);
		return true;
	}

	/**
	 * @param player Player to check
	 * @return All entities within the server view distance of player
	 */
	public static int getEntityCount(Player player) {
		int count = 0;
		World world = player.getWorld();
		Chunk mid = player.getLocation().getChunk();
		int midX = mid.getX();
		int midZ = mid.getZ();
		int dist = Misc.instance.getServer().getViewDistance();

		for (int x = -dist; x < dist; x++) {
			for (int z = -dist; z < dist; z++) {
				Chunk c = world.getChunkAt(midX + x, midZ + z);
				/* This could be filtered down to only living entities or specifically laggy ones such as villagers
				   but it should be good enough to give a general idea of who is causing lag. */
				count += c.getEntities().length;
			}
		}
		return count;
	}
}
