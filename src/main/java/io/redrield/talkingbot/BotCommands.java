package io.redrield.talkingbot;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BotCommands implements CommandExecutor {
	
    TalkingBot plugin;
    
    public BotCommands(TalkingBot plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
    	if(cmd.getName().equalsIgnoreCase("chatbot") && cs instanceof Player) {
    		Player p = (Player) cs;
    		if(args.length==0) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("incorrect-usage")));
    			return true;
    		}
    		if(args.length==1) {
    			if(args[0].equalsIgnoreCase("reload")) {
                    if(p.hasPermission("chatbot.reload")) {
                        plugin.reloadConfig();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("reloaded")));
                        return true;
                    }else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("no-permission")));
                        return true;
                    }
                }
                if(args[0].equalsIgnoreCase("toggle")) {
                    if(p.hasPermission("chatbot.toggle")) {
                        boolean old = plugin.getToggleState().get(p);
                        plugin.getToggleState().put(p, !old);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("toggle-" + (old ? "on" : "off"))));
                        return true;
                    }else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("no-permission")));
                        return true;
                    }
                }
            }
            if(args.length>=2) {
                if(args[0].equalsIgnoreCase("add")) {
                    if(p.hasPermission("chatbot.add")) {
                        String msg = args[1];
                        String res = "";
                        for(int i = 2; i < args.length; i++) {
                            res = res + args[i] + " ";
                        }
                        if(plugin.getConfig().contains("miscellaneous." + msg)) {
                            List<String> currentResponses = plugin.getConfig().getStringList("miscellaneous." + msg);
                            currentResponses.add(res);
                            plugin.getConfig().set("miscellaneous." + msg, currentResponses);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("message-added")));
                            plugin.saveConfig();
                            plugin.reloadConfig();
                            return true;
                        }
                        List<String> start = new ArrayList<>();
                        start.add(res);
                        plugin.getConfig().set("miscellaneous." + msg, start);
                        plugin.saveConfig();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("message-added")));
                        plugin.reloadConfig();
                        return true;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("no-permission")));
                    return true;
                }
            }
        }
        return false;
    }
}