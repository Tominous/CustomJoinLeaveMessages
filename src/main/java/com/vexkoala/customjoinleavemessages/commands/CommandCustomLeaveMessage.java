package com.vexkoala.customjoinleavemessages.commands;

import com.vexkoala.customjoinleavemessages.CustomJoinLeaveMessages;
import com.vexkoala.customjoinleavemessages.Data;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandCustomLeaveMessage implements TabExecutor
{

    Plugin plugin = CustomJoinLeaveMessages.getPlugin(CustomJoinLeaveMessages.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getName().equals("customleavemessage"))
        {
            if (args.length > 0)
            {
                // data.yml
                Data.setup();

                if (args[0].equalsIgnoreCase("user")) // /clm user [username] set [message]
                {
                    if (!sender.hasPermission("cjlm.customleavemessage.others"))
                    {
                        String error = formatColors(Data.get().getString("lang.NO_PERM"));
                        sender.sendMessage(error);
                        return false;
                    }

                    if (args.length < 4)
                    {
                        String error = formatColors(Data.get().getString("lang.TOO_FEW_ARGS"));
                        sender.sendMessage(error);
                        return false;
                    }

                    if (args[2].equalsIgnoreCase("set"))
                    {
                        // Get player UUID
                        String playerName = args[1];
                        String playerUUID = Bukkit.getServer().getOfflinePlayer(playerName).getUniqueId().toString();

                        // Get message from command
                        args = Arrays.copyOfRange(args, 3, args.length);
                        String message = String.join(" ", args);

                        // Get max characters
                        int maxChar = plugin.getConfig().getInt("messages.leave.max-characters");

                        if (message.length() > maxChar)
                        {
                            String error = formatColors(Data.get().getString("lang.MSG_TOO_LONG"));
                            sender.sendMessage(error);
                            return false;
                        }

                        // Save to data.yml
                        Data.get().set("messages." + playerUUID + ".leave", message);
                        Data.save();

                        String success = formatMessage(Data.get().getString("lang.PLUGIN_PREFIX") + Data.get().getString("lang.CLM_ADMIN"), playerName, message);
                        sender.sendMessage(success);
                        return true;
                    }
                    String error = formatColors(Data.get().getString("lang.UNKNOWN_ARG"));
                    sender.sendMessage(error);
                    return false;
                }
                else if (args[0].equalsIgnoreCase("set")) // /clm set [message]
                {
                    if (sender instanceof Player) // Is player?
                    {
                        if (args.length < 2) // If command doesn't contain message
                        {
                            String error = formatColors(Data.get().getString("lang.TOO_FEW_ARGS"));
                            sender.sendMessage(error);
                            return false;
                        }

                        // Get player UUID
                        Player player = (Player) sender;
                        String playerUUID = player.getUniqueId().toString();

                        // Get message from command
                        args = Arrays.copyOfRange(args, 1, args.length);
                        String message = String.join(" ", args);

                        // Get max characters
                        int maxChar = plugin.getConfig().getInt("messages.leave.max-characters");

                        if (message.length() > maxChar)
                        {
                            String error = formatColors(Data.get().getString("lang.MSG_TOO_LONG"));
                            sender.sendMessage(error);
                            return false;
                        }

                        // Save to data.yml
                        Data.get().set("messages." + playerUUID + ".leave", message);
                        Data.save();

                        String success = formatMessage(Data.get().getString("lang.PLUGIN_PREFIX") + Data.get().getString("lang.CLM_PLAYER"), player, message);
                        sender.sendMessage(success);
                        return true;
                    }
                    String error = formatColors(Data.get().getString("lang.NOT_PLAYER"));
                    sender.sendMessage(error);
                    return false;
                }
                else if (args[0].equalsIgnoreCase("view")) // /clm view
                {
                    if (args.length > 2)
                    {
                        String error = formatColors(Data.get().getString("lang.TOO_MANY_ARGS"));
                        sender.sendMessage(error);
                        return false;
                    }

                    if (args.length == 2) // /clm view [username]
                    {
                        if (!sender.hasPermission("cjlm.customleavemessage.view.others"))
                        {
                            String error = formatColors(Data.get().getString("lang.NO_PERM"));
                            sender.sendMessage(error);
                            return false;
                        }

                        // Get player UUID
                        String playerName = args[1];
                        String playerUUID = Bukkit.getServer().getOfflinePlayer(playerName).getUniqueId().toString();
                        if (Data.get().getString("messages." + playerUUID + ".leave") != null)
                        {
                            String message = formatColors(formatMessage(Data.get().getString("lang.PLUGIN_PREFIX") + Data.get().getString("lang.CLM_DISPLAY_MSG_ADMIN"), playerName, Data.get().getString("messages." + playerUUID + ".leave")));
                            sender.sendMessage(message);
                            return true;
                        }
                        String error = formatColors(Data.get().getString("lang.MSG_NOT_SET"));
                        sender.sendMessage(error);
                        return false;
                    }

                    if (sender instanceof Player)
                    {
                        // Get player UUID
                        Player player = (Player) sender;
                        String playerUUID = player.getUniqueId().toString();

                        // Get message from data.yml
                        if (Data.get().getString("messages." + playerUUID + ".leave") != null)
                        {
                            String message = formatColors(formatMessage(Data.get().getString("lang.PLUGIN_PREFIX") + Data.get().getString("lang.CLM_DISPLAY_MSG_PLAYER"), player, Data.get().getString("messages." + playerUUID + ".leave")));
                            sender.sendMessage(message);
                            return true;
                        }
                        String error = formatColors(Data.get().getString("lang.MSG_NOT_SET"));
                        sender.sendMessage(error);
                        return false;
                    }
                    String error = formatColors(Data.get().getString("lang.NOT_PLAYER"));
                    sender.sendMessage(error);
                    return false;
                }
                String error = formatColors(Data.get().getString("lang.UNKNOWN_ARG"));
                sender.sendMessage(error);
                return false;
            }
            String error = formatColors(Data.get().getString("lang.TOO_FEW_ARGS"));
            sender.sendMessage(error);
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        if (args.length == 1)
        {
            List<String> arguments = new ArrayList<>();
            arguments.add("set");
            arguments.add("user");
            arguments.add("view");
            return arguments;
        }
        else if (args[0].equalsIgnoreCase("user") && args.length == 2 || args[0].equalsIgnoreCase("view") && args.length == 2)
        {
            List<String> playerNames = new ArrayList<>();
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (Player player : players)
            {
                playerNames.add(player.getName());
            }
            return playerNames;
        }
        else if (args[0].equalsIgnoreCase("user") && args.length == 3)
        {
            List<String> arguments = new ArrayList<>();
            arguments.add("set");
            return arguments;
        }
        else
        {
            return new ArrayList<>();
        }
    }

    private static String formatMessage(String string, String name, String message)
    {
        // Format [player], [message]
        string = string.replaceAll("\\[player\\]", name);
        string = string.replaceAll("\\[message\\]", message);
        // Format colors
        string = formatColors(string);

        return string;
    }

    private static String formatMessage(String string, Player player, String message)
    {
        // Format [player]
        string = string.replaceAll("\\[player\\]", player.getName());
        if (player.hasPermission("cjlm.customleavemessage.color")) // If player has permission
        {
            // Format [message]
            string = string.replaceAll("\\[message\\]", message);
            // Format colors
            string = formatColors(string);
        }
        else
        {
            // Format colors
            string = formatColors(string);
            // Format [message]
            string = string.replaceAll("\\[message\\]", message);
        }

        return string;
    }

    private static String formatColors(String string)
    {
        // Format colors
        string = string.replaceAll("&", "§").replaceAll("§§", "&");

        return string;
    }

}