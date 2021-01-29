package net.simpvp.Misc;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

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
	public void onShootBow(EntityShootBowEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}

		Projectile p;
		if (event.getProjectile() instanceof Projectile) {
			p = (Projectile) event.getProjectile();
		} else {
			Misc.instance.getLogger().warning("Shot non-projectile");
			return;
		}

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
