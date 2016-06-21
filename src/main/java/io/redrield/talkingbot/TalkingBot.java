package io.redrield.talkingbot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

public class TalkingBot extends JavaPlugin{

    Map<Player, Boolean> toggleState = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new BotListener(this), this);
        getCommand("chatbot").setExecutor(new BotCommands(this));
        try {
            update();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Player, Boolean> getToggleState() {
        return toggleState;
    }

    @SuppressWarnings("resource")
	private void update() throws IOException {
        URL spigot = new URL("http://spigotmc.org/api/general.php");
        HttpURLConnection con = (HttpURLConnection) spigot.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.getOutputStream().write("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=23944".getBytes());
        String ver = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        if(Integer.parseInt(ver.replace(".", "")) > Integer.parseInt(this.getDescription().getVersion().replace(".", ""))) {
            File update = new File("plugins" + File.separator + "update");
            if(!update.exists()) {
                update.mkdir();
            }
            ReadableByteChannel in = Channels.newChannel(new URL("https://api.spiget.org/v1/resources/23944/download").openStream());
            FileChannel out = new FileOutputStream(new File(update, "TalkingBot.jar")).getChannel();
            out.transferFrom(in, 0, Long.MAX_VALUE);
            Bukkit.getConsoleSender().sendMessage(String.format("%s[" + this.getDescription().getName() + "] New version downloaded! Your version: " + this.getDescription().getVersion() + ". Newest version: " + ver + "\nRestart server for update to take effect", ChatColor.YELLOW));
        }
    }
}
