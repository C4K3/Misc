package net.simpvp.Misc;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PigmanDropFix implements Listener {

	private static final int ADULT_XP = 5;
	private static final int BABY_XP = 12;
	private static final float INGOT_CHANCE = 0.025f;

	/**
	 * Reverts the 1.21.5 Zombified Piglin drop changes.
	 * See: https://bugs.mojang.com/browse/MC/issues/MC-56653
	 */
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntityType() != EntityType.ZOMBIFIED_PIGLIN)
			return;

		PigZombie pigman = (PigZombie) event.getEntity();

		if (!pigman.isAngry() || event.getDroppedExp() > 0)
			return;

		// Experience
		int droppedExp = pigman.isAdult() ? ADULT_XP : BABY_XP;
		event.setDroppedExp(droppedExp);

		// Gold ingot
		if (Math.random() < INGOT_CHANCE) {
			ItemStack ingot = new ItemStack(Material.GOLD_INGOT, 1);
			event.getDrops().add(ingot);
		}

		// Mainhand weapon
		ItemStack weapon = pigman.getEquipment().getItemInMainHand();
		float chance = pigman.getEquipment().getItemInMainHandDropChance();
		if (weapon != null && Math.random() < chance) {
			event.getDrops().add(weapon);
		}
	}
}
