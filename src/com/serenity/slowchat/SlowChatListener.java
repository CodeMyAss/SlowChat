package com.serenity.slowchat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SlowChatListener implements Listener {
    private final Map<String, Long> times = new HashMap<String, Long>();
    private final SlowChat parent;

    public SlowChatListener(SlowChat parent) {
        this.parent = parent;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(final PlayerChatEvent event) {
        if(event.getPlayer().hasPermission("slowchat.unlimited")) {
            return;
        }

        long now = System.currentTimeMillis();
        String name = event.getPlayer().getName();
        Long lastChat = this.times.get(event.getPlayer().getName());

        if(lastChat != null) {
            // earliest time this player can send another chat message
            long earliestNext = lastChat + this.parent.getConfig().getInt("interval") * 1000;
            if(now < earliestNext) { // if we're before that time cancel the message
                int timeRemaining = (int)((earliestNext - now) / 1000) + 1;
                event.getPlayer().sendMessage(ChatColor.RED + "You can not talk for " + timeRemaining + " more second" + (timeRemaining > 1 ? "s" : ""));
                event.setCancelled(true);
                return;
            }
        }
        this.times.put(name, now);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.times.remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }
}
