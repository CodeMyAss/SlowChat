package com.serenity.slowchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SlowChatCommand implements CommandExecutor {
    private SlowChat parent;

    public SlowChatCommand(SlowChat parent) {
        this.parent = parent;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if(!sender.hasPermission("slowchat.set")) {
            sender.sendMessage(ChatColor.RED + "No Permission");
            return true;
        }

        if(args.length != 1) {
            player.sendMessage(ChatColor.GOLD + "SlowChat Delay: " + ChatColor.GREEN + String.valueOf(this.parent.getConfig().getInt("interval") * 1000) + " seconds");
            return true;
        }

        try {
            int interval = Integer.parseInt(args[0]);
            this.parent.getConfig().set("interval", interval);
            this.parent.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Updated time between messages to " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " second" + (interval == 1 ? "" : "s"));
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.GOLD + args[0] + ChatColor.RED + " is not a valid number");
        }
        return true;
    }
}
