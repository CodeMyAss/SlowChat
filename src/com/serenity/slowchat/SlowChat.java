package com.serenity.slowchat;

import org.bukkit.plugin.java.JavaPlugin;

public class SlowChat extends JavaPlugin {
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
