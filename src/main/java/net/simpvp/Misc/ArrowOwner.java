package net.simpvp.Misc;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;

/**
 * Removes shooter info from arrows shot by players.
 *
 * This is to mitigate an exploit in which shot arrows can leak their shooters
 * information, even long after the fact.
 */
public class ArrowOwner implements Listener {
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

		for (org.bukkit.entity.Entity e : event.getChunk().getEntities()) {
			if (e instanceof Projectile) {
				remove_owner_info((Projectile) e);
			}
		}
	}

	private void remove_owner_info(Projectile p) {
		Entity e = ((CraftEntity) p).getHandle();
		NBTTagCompound nbt = new NBTTagCompound();
		e.save(nbt);

		// Set the value to 0 instead of removing the entry, because it
		// seems to do a merge of the maps when calling load.
		int[] owner = nbt.getIntArray("Owner");
		for (int i = 0; i < owner.length; i++) {
			owner[i] = 0;
		}
		nbt.setIntArray("Owner", owner);
		e.load(nbt);
	}
}
