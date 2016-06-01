package io.redrield.talkingbot;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class TalkingBot extends JavaPlugin{

    Map<Player, Boolean> toggleState = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new BotListener(this), this);
        getCommand("chatbot").setExecutor(new BotCommands(this));
    }

    public Map<Player, Boolean> getToggleState() {
        return toggleState;
    }
}
