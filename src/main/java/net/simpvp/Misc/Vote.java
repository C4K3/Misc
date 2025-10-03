package net.simpvp.Misc;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Vote implements CommandExecutor {

    private final Misc plugin;

    public Vote(Misc plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        String title = plugin.getConfig().getString("vote.title", "§6You can vote daily on the following sites:");

        List<String> links = plugin.getConfig().getStringList("vote.links");

        if (links == null || links.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Voting links not configured.");
            return true;
        }

        player.sendMessage(title);
        player.sendMessage("");

        int index = 1;
        for (String url : links) {
            String buttonLabel = ChatColor.GREEN + "[Vote #" + index + "]";
            TextComponent button = new TextComponent(TextComponent.fromLegacyText(buttonLabel));

            button.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
            button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick to open!").create()));

            player.spigot().sendMessage(button);
            index++;
        }

        String footer = plugin.getConfig().getString("vote.footer", "§6The top monthly voters will be displayed in spawn!");
        player.sendMessage("");
        player.sendMessage(footer);

        return true;
    }
}
