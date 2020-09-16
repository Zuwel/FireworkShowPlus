package com.igufguf.fireworkshow;

import com.igufguf.fireworkshow.commands.CommandHandler;
import com.igufguf.fireworkshow.objects.Show;
import com.igufguf.fireworkshow.objects.Frame;
import com.igufguf.fireworkshow.objects.fireworks.NormalFireworks;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FireworkShow extends JavaPlugin
{
    private static final HashMap<String, Show> shows = new HashMap<>();
    private static FileConfiguration showsfile = new YamlConfiguration();
    public static FireworkShow fws;
    public static File dataFolder;
    private static Server server;
    @Override
    public void onEnable()
    {
        CommandHandler commands = new CommandHandler();
        fws = this;
        server = getServer();

        dataFolder = getDataFolder();

        shows.clear();

        File f = new File(dataFolder, "shows.yml");
        if ( !f.exists() )
        {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ConfigurationSerialization.registerClass(Show.class);
        ConfigurationSerialization.registerClass(Frame.class);
        ConfigurationSerialization.registerClass(NormalFireworks.class);
        try
        {
            showsfile.load(f);
        }
        catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }

        for ( String key : showsfile.getKeys(false) ) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[FireworkShow]: Indexing show '" + key + "'...");
            Show show = new Show((Show) showsfile.get(key)); //Create new show object to put in our hashmap
            shows.put(key, show);
        }

        saveShows();

        getCommand("fireworkshow").setExecutor(commands);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[FireworkShow]: Fireworks show is Enabled!");
    }

    @Override
    public void onDisable()
    {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[FireworkShow]: Fireworks show is Disabled!");
    }

    //Getters
    public static HashMap<String, Show> getShows()
    {
        return shows;
    }

    public static FileConfiguration getShowFiles()
    {
        return showsfile;
    }

    public static void saveShows()
    {
        try {
            server.getConsoleSender().sendMessage(ChatColor.GREEN + "[FireworkShow]: Attempting to save fireworkshow to 'shows.yml'!");
            showsfile.save(new File(dataFolder, "shows.yml"));
        } catch (IOException e) {
            server.getConsoleSender().sendMessage(ChatColor.RED + "[FireworkShow]: Failed to save fireworkshow to 'shows.yml'!");
            e.printStackTrace();
        }
    }

    public static void createShow(String name)
    {
        shows.put(name, new Show());
        showsfile.set(name, shows.get(name));
    }
}
