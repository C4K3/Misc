package net.simpvp.Misc;

import org.bukkit.potion.PotionEffectType;
import java.util.Optional;

public class NetherRoof {

	private static final long DAMAGE_TIMER = 20L;
	private static final double DAMAGE_AMOUNT = 10.0d;

	public NetherRoof(Misc plugin) {
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this::damagePlayers, 0L, DAMAGE_TIMER);
	}

	/* Full-size beacons are currently (mc 1.18) the only way to get Resistance II.
	*  Future updates could potentially break this.*/
	private void damagePlayers() {
		//noinspection ConstantConditions
		Misc.instance.getServer().getWorld("world_nether").getPlayers().stream()
				.filter(player ->
						player.getLocation().getY() >= 127 &&
						Optional.ofNullable(player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
								.map(e -> e.getAmplifier() != 1)
								.orElse(true))
				.forEach(player -> player.damage(DAMAGE_AMOUNT));
	}
}