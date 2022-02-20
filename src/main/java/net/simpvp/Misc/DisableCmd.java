package net.simpvp.Misc;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class DisableCmd implements Listener {

	private enum Cmd {
		// Commands that cannot be run by anybody
		DISABLED,
		// Commands that can only be run by non-players (e.g. command blocks)
		NONPLAYER,
		// Commands that can only be run by OPs
		OPONLY,
	}

	private static HashMap<String, Cmd> cmds = new HashMap();;

public DisableCmd() {
		for (String c : (List<String>) Misc.instance.getConfig().getList("oponlyCmds")) {
			c = c.toLowerCase();
			if (cmds.put(c, Cmd.OPONLY) != null) {
				Misc.instance.getLogger().warning("Duplicate blocked command " + c);
			}
		}
		for (String c : (List<String>) Misc.instance.getConfig().getList("nonplayerCmds")) {
			c = c.toLowerCase();
			if (cmds.put(c, Cmd.NONPLAYER) != null) {
				Misc.instance.getLogger().warning("Duplicate blocked command " + c);
			}
		}
		for (String c : (List<String>) Misc.instance.getConfig().getList("disabledCmds")) {
			c = c.toLowerCase();
			if (cmds.put(c, Cmd.DISABLED) != null) {
				Misc.instance.getLogger().warning("Duplicate blocked command " + c);
			}
		}
		Misc.instance.getLogger().info("Disabling " + cmds.values().stream().filter(cmd -> cmd == Cmd.OPONLY).count() + " commands from opOnlyCmds");
		Misc.instance.getLogger().info("Disabling " + cmds.values().stream().filter(cmd -> cmd == Cmd.NONPLAYER).count() + " commands from nonplayerCmds");
		Misc.instance.getLogger().info("Disabling " + cmds.values().stream().filter(cmd -> cmd == Cmd.DISABLED).count() + " commands from disabledCmds");
	}


	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		String[] split = event.getMessage().split(" ");
		if (split.length < 1) {
			return;
		}

		String cmd = split[0].trim().substring(1).toLowerCase();
		Player player = event.getPlayer();
		Cmd c = cmds.get(cmd);

		if (cmd.contains(":")) {
			disable_event(event, "Blocked command that contained :");
		} else if (c == null) {
			return;
		} else if (c == Cmd.DISABLED) {
			disable_event(event, "Blocked command found in disabledCmds");
		} else if (c == Cmd.NONPLAYER) {
			disable_event(event, "Blocked command found in nonplayerCmds");
		} else if (c == Cmd.OPONLY && !player.isOp()) {
			disable_event(event, "Blocked command found in oponlyCmds");
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onServerCommandEvent(ServerCommandEvent event) {
		if (event.getSender() instanceof ConsoleCommandSender) {
			return;
		}

		String[] split = event.getCommand().split(" ");
		if (split.length < 1) {
			return;
		}

		String cmd = split[0].trim().toLowerCase();
		Cmd c = cmds.get(cmd);

		boolean allowCommand = true;
		if (cmd.equals("kill") || cmd.equals("tp") || cmd.equals("execute")) {
			for (String args: split) {
				if (args.startsWith("@") && !(args.contains("distance="))) {
					allowCommand = false;
					break;
				}
			}
		}

		if (!allowCommand) {
			event.getSender().sendMessage("Blocking command without a distance limit: " + cmd);
			Misc.instance.getLogger().info("Blocking command without a distance limit: " + cmd);
			event.setCancelled(true);
		}

		if (c == Cmd.DISABLED) {
			event.getSender().sendMessage("You do not have permission to use this command.");
			Misc.instance.getLogger().info("Blocked command '" + cmd + "' found in disabledCmds");
			event.setCancelled(true);
		} else if (cmd.contains(":")) {
			event.getSender().sendMessage("You do not have permission to use this command.");
			Misc.instance.getLogger().info("Blocked command '" + cmd + "' that contained :");
			event.setCancelled(true);
		}
	}

	private void disable_event(PlayerCommandPreprocessEvent event, String blocked_message) {
		event.getPlayer().sendMessage("Unknown command. Type \"/help\" for help.");
		Misc.instance.getLogger().info(blocked_message);
		event.setCancelled(true);
	}
}
