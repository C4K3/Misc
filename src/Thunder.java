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

public class Thunder {

	public static void add_protocol_listeners() {
		Misc.instance.getLogger().info("Enabling thunder modification");

		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Misc.instance, ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT) {

			@Override
			public void onPacketSending(PacketEvent event) {

				PacketContainer p = event.getPacket();

				Sound soundType = p.getSoundEffects().read(0);

				if (soundType != Sound.ENTITY_LIGHTNING_BOLT_THUNDER) {
					return;
				}

				/* Change the direction of all thunder packets to
				 * a random direction, but keep the distance and height
				 * of the thunder the same, to prevent it from
				 * sound differently. */
				Location playerlocation = event.getPlayer().getLocation();
				double origx = p.getIntegers().read(0) - playerlocation.getBlockX();
				double origz = p.getIntegers().read(2) - playerlocation.getBlockZ();
				double dist = Math.sqrt(origx * origx +  origz * origz);

				Random rng = new Random();
				double angle = Math.toRadians(rng.nextInt(360));

				int new_x = (int) (Math.cos(angle) * dist + playerlocation.getBlockX());
				int new_z = (int) (Math.sin(angle) * dist + playerlocation.getBlockZ());

				p.getIntegers().write(0, new_x);
				p.getIntegers().write(2, new_z);
			}

		});	
	}

}

