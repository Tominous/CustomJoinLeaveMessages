package com.vexkoala.customjoinleavemessages;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class Data
{

    private static File file;
    private static FileConfiguration data;
    private static final Plugin plugin = CustomJoinLeaveMessages.getPlugin(CustomJoinLeaveMessages.class);

    // File setup
    public static void setup()
    {
        file = new File(plugin.getDataFolder(), "data.yml");

        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        data = YamlConfiguration.loadConfiguration(file);
    }

    // Get file instance
    public static FileConfiguration get()
    {
        return data;
    }

    // Save file
    public static void save()
    {
        try
        {
            data.save(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Reload config
    public static void reload()
    {
        data = YamlConfiguration.loadConfiguration(file);
    }

}
