package net.simpvp.Misc;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Pronouns implements Listener, CommandExecutor {

  private HashMap<UUID, String> playerPronouns = new HashMap<>();

  public Pronouns() {
    Misc.instance.getCommand("pronouns").setExecutor(this);
    Misc.instance.getServer().getPluginManager().registerEvents(this, Misc.instance);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("You must be a player to set your pronouns.");
      return true;
    }

    if (args.length == 0) {
      sender.sendMessage("Usage: /pronouns she/her");
      return true;
    }

    Player player = (Player) sender;
    String pronounStr = String.join(" ", args);

    if (pronounStr.length() > 20) {
      player.sendMessage(ChatColor.RED + "Sorry, your pronouns are too long. Try something shorter like she/her.");
      return true;
    }

    UUID id = player.getUniqueId();
    playerPronouns.put(id, pronounStr);
    Misc.instance.getConfig().set("Pronouns." + id, pronounStr);
    Misc.instance.saveConfig();
    Misc.instance.getServer()
        .broadcastMessage(String.format("%s's pronouns are now (%s).", player.getName(), pronounStr));

    return false;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    String msg = event.getJoinMessage();
    Player player = event.getPlayer();
    UUID id = player.getUniqueId();
    String pronouns = Misc.instance.getConfig().getString("Pronouns." + id);
    playerPronouns.put(id, pronouns);
    event.setJoinMessage(pronounify(msg, player));
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    String msg = event.getQuitMessage();
    Player player = event.getPlayer();
    event.setQuitMessage(pronounify(msg, player));
    playerPronouns.remove(player.getUniqueId());
  }

  private String pronounify(String message, Player player) {
    UUID id = player.getUniqueId();
    if (!playerPronouns.containsKey(id))
      return message;

    StringBuilder sb = new StringBuilder(message);
    return sb.insert(sb.indexOf(" "), " (" + playerPronouns.get(player.getUniqueId()) + ")").toString();
  }
}
