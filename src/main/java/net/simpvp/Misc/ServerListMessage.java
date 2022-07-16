package net.simpvp.Misc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import net.md_5.bungee.api.ChatColor;


public class ServerListMessage implements Listener {

	List<?> listOfMOTDS;
	Method method = null;

	@EventHandler
	public void serverListPingEvent(ServerListPingEvent event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (method != null) {
			String eventName = (String) method.invoke(null);
			if (eventName != null) {
				event.setMotd(ChatColor.LIGHT_PURPLE + "Currently playing:" + eventName);
				return;
			}
		}
		if (listOfMOTDS != null && !listOfMOTDS.isEmpty()) {
			String motd = (String) listOfMOTDS.get(ThreadLocalRandom.current().nextInt(listOfMOTDS.size()));
			event.setMotd(ChatColor.translateAlternateColorCodes('&', motd));
		}
	}

	/**
	 * Use reflection to get access to the event plugin. Thanks kutekats!
	 */
	public ServerListMessage(Misc plugin) {
		Class<?> eventsClass = null;
		listOfMOTDS = plugin.getConfig().getList("serverListMessages");
		try {
		  eventsClass = Class.forName("net.simpvp.Events.Event");
		  method = eventsClass.getMethod("getEventName");
		} catch (ClassNotFoundException | SecurityException | IllegalArgumentException | NoSuchMethodException e) {
			Misc.instance.getLogger().info("Event plugin not found");
		}
	}

}

