package net.simpvp.Misc;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Provides the /relativecoords command to easily let people calculate relative
 * coords between two points, for use in setting command blocks.
 */
public class RelativeCoords implements Listener, CommandExecutor {

	private class Selection {
		Block left;
		Block right;
	}

	private static HashMap<UUID, Selection> selections = new HashMap();

	// Don't ignore cancelled because WorldEdit will cancel it
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=false)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		if (action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack hand = player.getInventory().getItemInMainHand();
		if (hand == null || hand.getType() != Material.WOODEN_AXE) {
			return;
		}

		Selection s = selections.getOrDefault(player.getUniqueId(), new Selection());
		Block block = event.getClickedBlock();

		if (action == Action.LEFT_CLICK_BLOCK) {
			s.left = block;
		} else if (action == Action.RIGHT_CLICK_BLOCK) {
			s.right = block;
		}
		selections.put(player.getUniqueId(), s);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player;
		if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			sender.sendMessage("You must be a player to use this command.");
			return true;
		}

		Selection s = selections.get(player.getUniqueId());
		if (s == null || s.left == null || s.right == null) {
			player.sendMessage(ChatColor.RED + "Left and right click on a block while holding a wooden axe.");
			return true;
		}

		if (s.left.getWorld().getUID() != s.right.getWorld().getUID()) {
			player.sendMessage(ChatColor.RED + "The two points are not in the same world.");
			return true;
		}

		int x = s.left.getX() - s.right.getX();
		int y = s.left.getY() - s.right.getY();
		int z = s.left.getZ() - s.right.getZ();

		TextComponent chat = new TextComponent(String.format("Relative coords: ~%d ~%d ~%d", x, y, z));
		ClickEvent click = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("~%d ~%d ~%d", x, y, z));
		chat.setClickEvent(click);

		sender.spigot().sendMessage(chat);

		return true;
	}
}
