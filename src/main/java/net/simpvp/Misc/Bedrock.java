package net.simpvp.Misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Bedrock implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (sender instanceof Player) {
			sender.sendMessage("You do not have permission to use this command.");
			return true;
		}

		String target_world = "pvp";
		int radius = 20;
		Misc.instance.getLogger().info(String.format("Transforming all content in %s within %d of 0,0 into bedrock", target_world, radius));

		World world = Misc.instance.getServer().getWorld(target_world);

		long count = 0;
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				// Let's see how good the bukkit scheduler is
				final int cx = x;
				final int cz = z;
				new BukkitRunnable() {
					@Override
					public void run() {
						Misc.instance.getLogger().info(String.format("Transforming chunk at %d,%d", cx, cz));
						Chunk c = world.getChunkAt(cx, cz);
						convert_chunk(c);
					}
				}.runTaskLater(Misc.instance, count);
				count++;
			}
		}

		Misc.instance.getLogger().info("Bedrock transformation scheduling complete");

		return true;
	}

	private void convert_chunk(Chunk c) {
		c.load(true);
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 256; y++) {
				for (int z = 0; z < 16; z++) {
					Block b = c.getBlock(x, y, z);
					if (ignore_materials(b.getType())) {
						continue;
					}
					b.setType(Material.PINK_STAINED_GLASS);
				}
			}
		}

		c.unload(true);
	}

	private boolean ignore_materials(Material m) {
		switch(m) {
			case AIR:
			case WATER:
			case LAVA:
				return true;
			default:
				return false;
		}
	}
}
