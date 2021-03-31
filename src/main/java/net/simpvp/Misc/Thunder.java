package net.simpvp.Misc;

import java.util.Random;

import org.bukkit.Sound;
import org.bukkit.Location;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;

public class Thunder {

	public static void add_protocol_listeners() {
		Misc.instance.getLogger().info("Enabling thunder modification");

		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Misc.instance, ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT) {

			@Override
			public void onPacketSending(PacketEvent event) {

				PacketContainer p = event.getPacket();

				Sound soundType = p.getSoundEffects().read(0);

				switch (soundType) {
					case ENTITY_LIGHTNING_BOLT_THUNDER:
					// Does this actually work for these two?
					case EVENT_RAID_HORN:
					case ENTITY_WITHER_SPAWN:
						break;
					default:
						return;
				}

				/* Change the direction of all thunder packets to
				 * a random direction, but keep the distance and height
				 * of the thunder the same, to prevent it from
				 * sound differently.
				 *
				 * Note: While the packet does include absolute
				 * coordinates (multiplied by 8) the vanilla
				 * server normalizes the packet distance to be
				 * within range of each player. The server will
				 * never send packets with absolute coords
				 * matching that of another player. In other
				 * words, the distance should never be more
				 * than a few hundred.
				 *
				 * This does have the unfortunate effect of not
				 * making it viable for us to know if a given
				 * thunder packet was triggered from a
				 * lightning strike nearby or from far away.
				 * Checking that would be desirable, such that
				 * we could completely skip modifying sounds
				 * that have a source nearby.*/
				Location playerlocation = event.getPlayer().getLocation();
				double origx = (p.getIntegers().read(0) / 8) - playerlocation.getBlockX();
				double origz = (p.getIntegers().read(2) / 8) - playerlocation.getBlockZ();
				double dist = Math.sqrt(origx * origx + origz * origz);

				Random rng = new Random();
				double angle = Math.toRadians(rng.nextInt(360));

				int new_x = (int) (Math.cos(angle) * dist) + playerlocation.getBlockX();
				int new_z = (int) (Math.sin(angle) * dist) + playerlocation.getBlockZ();
				new_x *= 8;
				new_z *= 8;

				p.getIntegers().writeSafely(0, new_x);
				p.getIntegers().writeSafely(2, new_z);
			}

		});

	ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Misc.instance, ListenerPriority.NORMAL, PacketType.Play.Server.WORLD_EVENT) {
			@Override
			public void onPacketSending(PacketEvent event) {
				// For every WORLD_EVENT packet just set the
				// position of the packet source to be the same
				// as player receiving the packet.
				//
				// The exploit for this works the same as for
				// thunder, the only real difference being that
				// this packet doesn't do the times 8 thing
				// that the sound packet does.
				//
				// This packet conveniently has a global field,
				// which should be set on every packet that
				// occurs from far away (though in theory we're
				// not guaranteed that.) The global field has
				// this convenient behavior where setting it
				// means the position isn't used for loudness.
				// So setting the coords to 0,0 doesn't cause
				// it to sound too unnatural, we wouldn't gain
				// anything over this by setting actually
				// random coords, like we do for thunder sound
				// packets.

				PacketContainer p = event.getPacket();

				Boolean global = p.getBooleans().read(0);
				if (global == false) {
					return;
				}

				Integer effect_id = p.getIntegers().read(0);
				BlockPosition position = new BlockPosition(0, 100, 0);
				p.getBlockPositionModifier().writeSafely(0, position);
			}
	});

	}
}

