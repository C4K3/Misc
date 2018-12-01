package net.simpvp.Misc;

import java.util.Random;

import org.bukkit.Sound;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
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

				/* Change the x/y/z to random values.
				 * x and z are up to 100 blocks in either direction
				 * from the player.
				 * y may not be necessary to randomize,
				 * but we do it just to be safe */
				Random rng = new Random();
				int x = rng.nextInt(1600) - 800;
				int y = rng.nextInt(200) + 400;
				int z = rng.nextInt(1600) - 800;

				Location playerlocation = event.getPlayer().getLocation();

				p.getIntegers().write(0, playerlocation.getBlockX() + x);
				p.getIntegers().write(1, y);
				p.getIntegers().write(2, playerlocation.getBlockZ() + z);
			}

		});	
	}

}

