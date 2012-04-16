package com.serenity.slowchat;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.Date;

public class SlowChatListener implements Listener {
    private final SlowChat parent;
    private final Server server;
    private final PluginManager pm;

    public SlowChatListener(SlowChat parent) {
        this.parent = parent;
        this.server = parent.getServer();
        this.pm = this.server.getPluginManager();
    }

    @EventHandler()
    public void onPlayerJoin(final PlayerJoinEvent event) {
        this.parent.times.put(event.getPlayer().getName(), new Date(0));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(final PlayerChatEvent event) {
        if(event.getPlayer().hasPermission("slowchat.unlimited")) {
            return;
        }

        String name = event.getPlayer().getName();
        Date lastChat = this.parent.times.get(name);

        if(lastChat != null) {
            Date past = new Date(System.currentTimeMillis() - (this.parent.getConfig().getInt("interval") * 1000));
            if(past.before(lastChat)) {
                int timeRemaining = (int)((lastChat.getTime() - past.getTime()) / 1000) + 1;
                event.getPlayer().sendMessage(ChatColor.RED + "You can not talk for " + String.valueOf(timeRemaining) + " more second" + (timeRemaining > 1 ? "s" : ""));
                event.setCancelled(true);
                return;
            }
        }
        this.parent.times.put(name, new Date());
    }

    @EventHandler()
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.parent.times.remove(event.getPlayer().getName());
    }

    @EventHandler()
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }
}
