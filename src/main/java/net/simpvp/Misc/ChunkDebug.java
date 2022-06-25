package net.simpvp.Misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkDebug implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;

			if (!player.isOp()) {
				player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
				return true;
			}
		}

		Chunk chunk;
		if (args.length >= 2) {
			int x;
			int z;
			World world;

			try {
				x = Integer.parseInt(args[0]);
				z = Integer.parseInt(args[1]);
			} catch (Exception e) {
				sender.sendMessage(String.format("Invalid integer: %s", e));
				return true;
			}

			if (args.length >= 3) {
				world = Misc.instance.getServer().getWorld(args[2]);
				if (world == null) {
					sender.sendMessage(String.format("No world named %s", args[2]));
					return true;
				}
			} else {
				if (player == null) {
					sender.sendMessage("Non-players must specify the world");
					return true;
				}

				world = player.getLocation().getWorld();
			}

			chunk = world.getChunkAt(x, z);
		} else if (player == null) {
			sender.sendMessage("Non-players must provide coordinates");
			return true;
		} else {
			chunk = player.getLocation().getChunk();
		}

		int blockx = chunk.getBlock(0, 0, 0).getX();
		int blockz = chunk.getBlock(0, 0, 0).getZ();

		int regionx = blockx >> 9;
		int regionz = blockz >> 9;

		String msg = "";
		msg += String.format("Chunk X: %d\n", chunk.getX());
		msg += String.format("Chunk Z: %d\n", chunk.getZ());
		msg += String.format("Block X: %d -> %d\n", blockx, chunk.getBlock(15, 0, 15).getX());
		msg += String.format("Block Z: %d -> %d\n", blockz, chunk.getBlock(15, 0, 15).getZ());
		msg += String.format("Region X: %d\n", regionx);
		msg += String.format("Region Z: %d\n", regionz);
		msg += String.format("IsLoaded: %s\n", chunk.isLoaded());
		msg += String.format("IsForceLoaded: %s\n", chunk.isForceLoaded());
		msg += String.format("InhabitedTime: %d\n", chunk.getInhabitedTime());
		msg += String.format("Entities: %d\n", chunk.getEntities().length);
		msg += String.format("TileEntities: %d\n", chunk.getTileEntities().length);

		sender.sendMessage(msg);
		return true;
	}
}
