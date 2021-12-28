package net.simpvp.Misc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import net.md_5.bungee.api.ChatColor;


public class ServerListMessage implements Listener {
	
	List<?> listOfMOTDS = Misc.instance.getConfig().getList("serverListMessage");
	String motd = "Welcome to simpvp";
	static Class<?> eventsClass;
	static Field field;
	static Method method;
	String eventName = null;
	
	@EventHandler
	public void serverListPingEvent(ServerListPingEvent event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (field != null) {
			eventName = (String) method.invoke(field.get(null));
			if (eventName != null) {
				event.setMotd(ChatColor.LIGHT_PURPLE + "Currently playing:" + eventName);
				return;
			}
		} else {
			Misc.instance.getLogger().info("Event plugin not found");
		}
		if (!listOfMOTDS.isEmpty()) {
			motd = (String) listOfMOTDS.get(ThreadLocalRandom.current().nextInt(listOfMOTDS.size()));
			event.setMotd(motd);
		}
	}
	
	/**
	 * Use reflection to get access to the event plugin. Thanks kutekats!
	 */
	public static void initEvents() {
		try {
		  eventsClass = Class.forName("net.simpvp.Events.Event");
		  field = eventsClass.getField("event");
		  method = eventsClass.getMethod("getEventName");
		} catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | NoSuchMethodException e) {
			Misc.instance.getLogger().info("Event plugin not found");
		}
	}
	
}