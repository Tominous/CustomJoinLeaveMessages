package com.vexkoala.customjoinleavemessages.commands;

import com.vexkoala.customjoinleavemessages.CustomJoinLeaveMessages;
import com.vexkoala.customjoinleavemessages.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CommandCustomJoinLeaveMessages implements TabExecutor
{

    Plugin plugin = CustomJoinLeaveMessages.getPlugin(CustomJoinLeaveMessages.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getName().equals("customjoinleavemessages"))
        {
            if (args.length > 0)
            {
                if (args.length > 1)
                {
                    String error = formatColors(Data.get().getString("lang.TOO_MANY_ARGS"));
                    sender.sendMessage(error);
                    return false;
                }

                if (args[0].equalsIgnoreCase("reload"))
                {
                    if (sender.hasPermission("cjlm.customjoinleavemessages.reload"))
                    {
                        plugin.reloadConfig();
                        Data.reload();

                        String success = formatColors(Data.get().getString("lang.RELOAD_SUCCESS"));
                        sender.sendMessage(success);
                        return true;
                    }
                    String error = formatColors(Data.get().getString("lang.NO_PERMS"));
                    sender.sendMessage(error);
                    return false;
                }
                String error = formatColors(Data.get().getString("lang.UNKNOWN_ARG"));
                sender.sendMessage(error);
                return false;
            }
            else
            {
                String success = formatColors("&8--------------[&e " + plugin.getDescription().getPrefix() + " &8]--------------\n" +
                                                    "&fVersion: &a" + plugin.getDescription().getVersion() + "\n" +
                                                    "&a" + plugin.getDescription().getDescription() + "\n" +
                                                    "&fAuthor: &a" + plugin.getDescription().getAuthors() + "\n" +
                                                    "&8-----------------------------------------------------");
                sender.sendMessage(success);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        if (args.length == 1)
        {
            List<String> arguments = new ArrayList<>();
            arguments.add("reload");
            return arguments;
        }
        else
        {
            return new ArrayList<>();
        }
    }

    private static String formatColors(String string)
    {
        // Format colors
        string = string.replaceAll("&", "§").replaceAll("§§", "&");

        return string;
    }

}