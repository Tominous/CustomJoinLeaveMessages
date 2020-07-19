package com.vexkoala.customjoinleavemessages.events;

import com.vexkoala.customjoinleavemessages.CustomJoinLeaveMessages;
import com.vexkoala.customjoinleavemessages.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class PlayerJoin implements Listener
{
    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event)
    {
        Plugin plugin = CustomJoinLeaveMessages.getPlugin(CustomJoinLeaveMessages.class);

        // data.yml
        Data.setup();

        // Custom join message
        if (!plugin.getConfig().getBoolean("messages.join.disabled"))
        {

            // Get player UUID
            Player player = event.getPlayer();
            String playerUUID = player.getUniqueId().toString();

            // Get total users
            int totalUsers = Data.get().getInt("info.total-users");

            // Get join message
            String prefix = plugin.getConfig().getString("messages.join.prefix");
            String message = plugin.getConfig().getString("messages.join.message");

            if (Data.get().getString("messages." + playerUUID + ".join") != null) // Check if player has a custom message
            {
                message = Data.get().getString("messages." + playerUUID + ".join");
            }
            if (player.hasPermission("cjlm.customjoinmessage.color")) // Can player have colored message?
            {
                message = formatColors(message);
            }

            // Broadcast the message
            String customMessage = formatColors(formatMessage(prefix, player, totalUsers)) + message;
            event.setJoinMessage(customMessage);
        }

        // First join message
        if (!plugin.getConfig().getBoolean("messages.first-join.disabled"))
        {
            Player player = event.getPlayer();

            if (!player.hasPlayedBefore())
            {
                // Get total users
                int totalUsers = Data.get().getInt("info.total-users");

                // Save totalUsers + 1
                Data.get().set("info.total-users", totalUsers + 1);
                Data.save();

                // Broadcast first-join message
                String firstJoin = formatColors(formatMessage(plugin.getConfig().getString("messages.first-join.message"), player, totalUsers + 1));
                event.setJoinMessage(firstJoin);

                // User welcome message
                if (!plugin.getConfig().getBoolean("messages.user-welcome.disabled"))
                {
                    String userWelcome = formatColors(formatMessage(plugin.getConfig().getString("messages.user-welcome.message"), player, totalUsers + 1));
                    player.sendMessage(userWelcome);
                }
            }
        }
        // User welcome back message
        if (!plugin.getConfig().getBoolean("messages.user-welcome-back.disabled"))
        {
            Player player = event.getPlayer();

            // Get total users
            int totalUsers = Data.get().getInt("info.total-users");

            if (player.hasPlayedBefore())
            {
                String userWelcomeBack = formatColors(formatMessage(plugin.getConfig().getString("messages.user-welcome-back.message"), player, totalUsers));
                player.sendMessage(userWelcomeBack);
            }
        }

    }

    private static String formatMessage(String message, Player player, int totalUsers)
    {
        // Format [playerName], [playerDisplayName], [totalUsers]
        message = message.replaceAll("\\[playerName\\]", player.getName()).replaceAll("\\[playerDisplayName\\]", player.getDisplayName()).replaceAll("\\[totalUsers\\]", Integer.toString(totalUsers));

        return message;
    }

    private static String formatColors(String message)
    {
        // Format colors
        message = message.replaceAll("&", "§").replaceAll("§§", "&");

        return message;
    }
}