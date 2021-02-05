package net.simpvp.Misc;

import java.lang.reflect.*;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Removes shooter info from arrows shot by players.
 *
 * This is to mitigate an exploit in which shot arrows can leak their shooters
 * information, even long after the fact.
 */
public class ArrowOwner implements Listener {

	private Class nmsEntity = null;
	private Class nbtTagCompound = null;
	private Class bukkitCraftEntity = null;

	public ArrowOwner() {
		String package_name = Misc.instance.getServer().getClass().getPackage().getName();
		String version = package_name.substring(package_name.lastIndexOf(".") + 1);

		try {
			nmsEntity = Class.forName(String.format("net.minecraft.server.%s.Entity", version));
			nbtTagCompound = Class.forName(String.format("net.minecraft.server.%s.NBTTagCompound", version));
			bukkitCraftEntity = Class.forName(String.format("org.bukkit.craftbukkit.%s.entity.CraftEntity", version));
		} catch (ClassNotFoundException e) {
			Misc.instance.getLogger().warning("Error enabling ArrowOwner mitigation: " + e);
			e.printStackTrace();
			nmsEntity = null;
			nbtTagCompound = null;
			bukkitCraftEntity = null;
			return;
		}
		Misc.instance.getLogger().info("ArrowOwner mitigation enabled");
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		final Projectile p = event.getEntity();
		new BukkitRunnable() {
			@Override
			public void run() {
				remove_owner_info(p);

			}
		}.runTaskLater(Misc.instance, 1L);

	}

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		if (event.isNewChunk()) {
			return;
		}

		for (Entity e : event.getChunk().getEntities()) {
			if (e instanceof Projectile) {
				remove_owner_info((Projectile) e);
			}
		}
	}

	private void remove_owner_info(Projectile p) {
		try {
			Object e = bukkitCraftEntity.getMethod("getHandle").invoke(p);
			Object nbt = nbtTagCompound.newInstance();
			nmsEntity.getMethod("save", nbtTagCompound).invoke(e, nbt);

			// Set the value to 0 instead of removing the entry, because it
			// seems to do a merge of the maps when calling load.
			int[] owner = (int[]) nbtTagCompound.getMethod("getIntArray", String.class).invoke(nbt, "Owner");
			for (int i = 0; i < owner.length; i++) {
				owner[i] = 0;
			}
			nbtTagCompound.getMethod("setIntArray", String.class, int[].class).invoke(nbt, "Owner", owner);
			nmsEntity.getMethod("load", nbtTagCompound).invoke(e, nbt);
		} catch (Exception exception) {
			Misc.instance.getLogger().warning("ArrowOwner error doing reflection: " + exception);
			exception.printStackTrace();
		}
	}
}
