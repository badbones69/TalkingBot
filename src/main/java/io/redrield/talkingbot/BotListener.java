package io.redrield.talkingbot;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Random;

public class BotListener implements Listener {

    TalkingBot plugin;

    public BotListener(TalkingBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("bot-prefix"));
        String msg = e.getMessage();
        Player p = e.getPlayer();
        String[] split = msg.split(" ");
        StringBuilder lookNew = new StringBuilder();
        if(plugin.getToggleState().get(p) || !p.hasPermission("talkingbot.interact")) {
            return;
        }
        for(int i = 0; i < split.length; i++) {
            if(i==0) {
                lookNew.append(split[i]);
                continue;
            }
            lookNew.append("-").append(split[i]);
        }
        if(plugin.getConfig().contains("miscellaneous." + lookNew.toString())) {
            List<String> selection = plugin.getConfig().getStringList("miscellaneous." + lookNew.toString());
            String say = ChatColor.translateAlternateColorCodes('&', selection.get(new Random().nextInt(selection.size())).replace("%player%", p.getName()));
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.broadcastMessage(prefix + " " + say));
            return;
        }
        if(split.length>0 && split[0].equalsIgnoreCase(plugin.getConfig().getString("bot-name"))) {

            String[] lookup = msg.substring(plugin.getConfig().getString("bot-name").length()).split(" ");



            if(lookup.length == 1) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    List<String> selection = plugin.getConfig().getStringList("no-match.bot-name-only");
                    String say = selection.get(new Random().nextInt(selection.size()));
                    say = say.replace("%player%", p.getName());
                    Bukkit.broadcastMessage(prefix + " " + say);
                }, plugin.getConfig().getLong("response-speed"));
                return;
            }

            String look = "sayings";


            for (int i = 0; i < lookup.length; i++) {
                look = look + "." + lookup[i];
            }

            if(plugin.getConfig().contains(look)) {
                List<String> selection = plugin.getConfig().getStringList(look);
                final String say = selection.get(new Random().nextInt(selection.size())).replace("%player%", p.getName());
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().broadcastMessage(prefix + " " + say), plugin.getConfig().getLong("response-speed"));
            }else {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    List<String> selection = plugin.getConfig().getStringList("no-match.question-not-found");
                    String say = selection.get(new Random().nextInt(selection.size()));
                    say = say.replace("%player%", p.getName());
                    Bukkit.broadcastMessage(prefix + " " +  say);
                }, plugin.getConfig().getLong("response-speed"));
            }


        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        plugin.getToggleState().put(e.getPlayer(), false);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        plugin.getToggleState().remove(e.getPlayer());
    }
}
