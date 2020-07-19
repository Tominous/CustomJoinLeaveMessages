package com.vexkoala.customjoinleavemessages.events;

import com.vexkoala.customjoinleavemessages.CustomJoinLeaveMessages;
import com.vexkoala.customjoinleavemessages.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlayerQuit implements Listener
{
    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event)
    {
        Plugin plugin = CustomJoinLeaveMessages.getPlugin(CustomJoinLeaveMessages.class);

        // data.yml
        Data.setup();

        if (!plugin.getConfig().getBoolean("messages.leave.disabled"))
        {
            // Get player UUID
            Player player = event.getPlayer();
            String playerUUID = player.getUniqueId().toString();

            // Get total users
            int totalUsers = Data.get().getInt("info.total-users");

            // Get leave message
            String prefix = plugin.getConfig().getString("messages.leave.prefix");
            String message = plugin.getConfig().getString("messages.leave.message");

            if (Data.get().getString("messages." + playerUUID + ".leave") != null) // Check if player has a custom message
            {
                message = Data.get().getString("messages." + playerUUID + ".leave");
            }
            if (player.hasPermission("cjlm.customleavemessage.color")) // Can player have colored message?
            {
                message = formatColors(message);
            }

            // Broadcast the message
            String customMessage = formatColors(formatMessage(prefix, player, totalUsers)) + message;
            event.setQuitMessage(customMessage);
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