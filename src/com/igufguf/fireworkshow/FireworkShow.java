package com.igufguf.fireworkshow;

import com.igufguf.fireworkshow.commands.CommandHandler;
import com.igufguf.fireworkshow.objects.Show;
import com.igufguf.fireworkshow.objects.Frame;
import com.igufguf.fireworkshow.objects.fireworks.NormalFireworks;
import org.bukkit.ChatColor;
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


public class FireworkShow extends JavaPlugin
{
    private static final HashMap<String, Show> shows = new HashMap<>();
    private static FileConfiguration showsfile = new YamlConfiguration();
    public static FireworkShow fws;
    public static File dataFolder;
    @Override
    public void onEnable()
    {
        CommandHandler commands = new CommandHandler();
        fws = this;

        dataFolder = getDataFolder();
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
            Show show = new Show();
            show.setHighest(showsfile.getBoolean(key+".highest"));
            for ( Frame frame : (ArrayList<Frame>) showsfile.getList(key+".frames") ) {
                show.frames.add(frame);
            }
            shows.put(key, show);
        }
        getCommand("fireworkshow").setExecutor(commands);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[FireworksShow]: Fireworks show is Enabled!");
    }

    @Override
    public void onDisable()
    {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[FireworksShow]: Fireworks show is Disabled!");
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
            showsfile.save(new File(dataFolder, "shows.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createShow(String name)
    {
        shows.put(name, new Show());
        showsfile.set(name, shows.get(name));
        showsfile.set(name+".highest", shows.get(name).getHighest());
        showsfile.set(name+".frames", shows.get(name).frames);
    }
}
