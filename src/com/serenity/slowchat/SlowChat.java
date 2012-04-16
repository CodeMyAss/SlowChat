package com.serenity.slowchat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public class SlowChat extends JavaPlugin {
    public Map<String, Date> times = new HashMap<String, Date>();

    @Override
    public void onEnable() {
        // Copy the default configuration
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        // Register events
        this.getServer().getPluginManager().registerEvents(new SlowChatListener(this), this);

        // Register commands
        this.getCommand("slowchat").setExecutor(new SlowChatCommand(this));
    }
}
