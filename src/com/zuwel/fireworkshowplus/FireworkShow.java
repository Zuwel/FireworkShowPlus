package com.zuwel.fireworkshowplus;

import com.zuwel.fireworkshowplus.commands.CommandHandler;
import com.zuwel.fireworkshowplus.commands.frames.AddFrameCommand;
import com.zuwel.fireworkshowplus.commands.menus.MainMenuCommand;
import com.zuwel.fireworkshowplus.commands.shows.CreateShowCommand;
import com.zuwel.fireworkshowplus.commands.shows.DeleteShowCommand;
import com.zuwel.fireworkshowplus.objects.Show;
import com.zuwel.fireworkshowplus.objects.Frame;
import com.zuwel.fireworkshowplus.objects.fireworks.NormalFireworks;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;


public class FireworkShow extends JavaPlugin
{
    private static final HashMap<String, Show> shows = new HashMap<>();
    private static FileConfiguration showsfile = new YamlConfiguration();
    public static FireworkShow fws;
    public static Server server;
    public static CommandHandler handler;
    public static File dataFolder;
    public static String version;

    public void loadShowFile(String fileName) {

        shows.clear();

        File f = new File(dataFolder, fileName+".yml");
        if ( !f.exists() )
        {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try
        {
            getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[FireworkShow]: Attempting to load show file '" + f.getName() + "'");
            showsfile.load(f);
        }
        catch (IOException | InvalidConfigurationException e)
        {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[FireworkShow]: Failed to load show file '" + f.getName() + "'!");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        for ( String key : showsfile.getKeys(false) ) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[FireworkShow]: Indexing show '" + key + "'...");
            Show show = new Show((Show) showsfile.get(key)); //Create new show object to put in our hashmap

            shows.put(key, show);
        }

    }

    public void initCommands(CommandHandler handler){
        //Register commands
        handler.register( new MainMenuCommand() );
        handler.register( new CreateShowCommand() );
        handler.register( new DeleteShowCommand() );
        handler.register( new AddFrameCommand() );

        //Initialize command handler
        getCommand("fireworkshow").setExecutor(handler);
    }

    @Override
    public void onEnable()
    {
        fws = this;
        server = getServer();
        handler = new CommandHandler();
        dataFolder = getDataFolder();
        version = fws.getDescription().getVersion();

        ConfigurationSerialization.registerClass(Show.class);
        ConfigurationSerialization.registerClass(Frame.class);
        ConfigurationSerialization.registerClass(NormalFireworks.class);

        loadShowFile("shows");

        saveShows(); //Save any changes made to the shows file upon initializing the shows

        initCommands(handler); //Initialize command handling

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
        saveShows("shows");
    }

    public static void saveShows(String fileName)
    {
        try {
            server.getConsoleSender().sendMessage(ChatColor.GREEN + "[FireworkShow]: Attempting to save fireworkshow to '" + fileName + ".yml'!");
            showsfile.save(new File(dataFolder, fileName+".yml"));
        } catch (IOException e) {
            server.getConsoleSender().sendMessage(ChatColor.RED + "[FireworkShow]: Failed to save fireworkshow to '" + fileName + ".yml'!");
            e.printStackTrace();
        }
    }

    //TODO: Add a config option to censor profanity in fireworks shows
    //  Try and make it handle Cunthorpe problem adequately.
    //  Alternatively have it hide them from the shows list.
    //  Also add param to make it private or public show. (Private shows only available to Authors/Editors/OP's, requires perm fireworks.shows.list.viewall)
    public static void createShow(String name, String author)
    {
        String uuid = "";
        while ( uuid.isEmpty() || showsfile.contains(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
        shows.put(uuid, new Show(name, author));
        showsfile.set(uuid, shows.get(uuid));
        saveShows();
    }

    //TODO: Needs to work with several ways of deleting. (UUID/Name Search/Author
    //  Also needs to have several ways of deleting. (Shows with name/All by Author)
    public static boolean deleteShow(String uuid)
    {
        if ( shows.containsKey( uuid ) ) {
            shows.remove(uuid);
            showsfile.set(uuid, null);
            saveShows();
            return true;
        }
        return false;
    }

}
