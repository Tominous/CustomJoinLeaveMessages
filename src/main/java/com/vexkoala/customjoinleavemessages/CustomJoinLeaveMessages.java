package com.vexkoala.customjoinleavemessages;

import com.vexkoala.customjoinleavemessages.commands.CommandCustomJoinLeaveMessages;
import com.vexkoala.customjoinleavemessages.commands.CommandCustomJoinMessage;
import com.vexkoala.customjoinleavemessages.commands.CommandCustomLeaveMessage;
import com.vexkoala.customjoinleavemessages.events.PlayerJoin;
import com.vexkoala.customjoinleavemessages.events.PlayerQuit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomJoinLeaveMessages extends JavaPlugin {

    @Override
    public void onEnable()
    {
        // Plugin startup logic
        PluginManager pluginManager = getServer().getPluginManager();

        // config.yml
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        if (getConfig().getBoolean("enabled"))
        {
            // data.yml
            Data.setup();
            Data.get().options().header("*-------------------------------*\n" +
                                        "|    CustomJoinLeaveMessages    |\n" +
                                        "|             Data              |\n" +
                                        "*-------------------------------*\n" +
                                        " " +
                                        "Placeholders:\n" +
                                        "  [player]   - player name\n" +
                                        "  [message]  - custom message\n");
            Data.get().options().copyHeader(true);
            Data.get().addDefault("info.total-users", 0);
            Data.get().addDefault("lang.TOO_FEW_ARGS", "&cToo few arguments!");
            Data.get().addDefault("lang.TOO_MANY_ARGS", "&cToo many arguments!");
            Data.get().addDefault("lang.UNKNOWN_ARG", "&cUnknown argument!");
            Data.get().addDefault("lang.MSG_TOO_LONG", "&cMessage too long!");
            Data.get().addDefault("lang.MSG_NOT_SET", "&cMessage not set!");
            Data.get().addDefault("lang.NO_PERMS", "&cYou don't have permission!");
            Data.get().addDefault("lang.NOT_PLAYER", "&cOnly a player can do this command!");
            Data.get().addDefault("lang.RELOAD_SUCCESS", "&8[&eCustomJoinLeaveMessages&8] &aReload successful.");
            Data.get().addDefault("lang.CJM_ADMIN", "&aCustom join message &efor &6[player] &ehas been set to &7[message]");
            Data.get().addDefault("lang.CLM_ADMIN", "&cCustom leave message &efor &6[player] &ehas been set to &7[message]");
            Data.get().addDefault("lang.CJM_PLAYER", "&eYour &acustom join message &ehas been set to &7[message]");
            Data.get().addDefault("lang.CLM_PLAYER", "&eYour &ccustom leave message &ehas been set to &7[message]");
            Data.get().addDefault("lang.CJM_DISPLAY_MSG_ADMIN", "&aCustom join message &efor &6[player] &eis &7[message]");
            Data.get().addDefault("lang.CJM_DISPLAY_MSG_PLAYER", "&eYour &acustom join message &eis &7[message]");
            Data.get().addDefault("lang.CLM_DISPLAY_MSG_ADMIN", "&cCustom leave message &efor &6[player] &eis &7[message]");
            Data.get().addDefault("lang.CLM_DISPLAY_MSG_PLAYER", "&eYour &ccustom leave message &eis &7[message]");
            Data.get().options().copyDefaults(true);
            Data.save();

            // Events
            pluginManager.registerEvents(new PlayerJoin(), this);
            pluginManager.registerEvents(new PlayerQuit(), this);

            // Commands
            getCommand("customjoinmessage").setExecutor(new CommandCustomJoinMessage());
            getCommand("customleavemessage").setExecutor(new CommandCustomLeaveMessage());
            getCommand("customjoinleavemessages").setExecutor(new CommandCustomJoinLeaveMessages());

            getLogger().info("Loaded.");
        }
        else
        {
            pluginManager.disablePlugin(this);
        }

    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
        getLogger().info("Stopped.");
    }

}