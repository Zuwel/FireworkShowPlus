package com.igufguf.fireworkshow;

import com.igufguf.fireworkshow.commands.CommandHandler;
import com.igufguf.fireworkshow.commands.frames.AddFrameCommand;
import com.igufguf.fireworkshow.commands.menus.MainMenuCommand;
import com.igufguf.fireworkshow.commands.shows.CreateShowCommand;
import com.igufguf.fireworkshow.commands.shows.DeleteShowCommand;
import com.igufguf.fireworkshow.objects.Show;
import com.igufguf.fireworkshow.objects.Frame;
import com.igufguf.fireworkshow.objects.fireworks.NormalFireworks;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class FireworkShow extends JavaPlugin
{
    private static final HashMap<String, Show> shows = new HashMap<>();
    private static FileConfiguration showsfile = new YamlConfiguration();
    public static FireworkShow fws;
    public static CommandHandler handler;
    public static File dataFolder;

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
            Show show = new Show(); //Create new show object to put in our hashmap

            if ( showsfile.contains(key+".name") ) { //Check if the parameter exists
                show.setName(showsfile.getString(key+".name"));
            } else {
                getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[FireworkShow]: No 'name' param found. Adding param...");
                showsfile.set(key+".name",show.getName()); //Set the default value in the show file
            }

            if ( showsfile.contains(key+".frames") ) {
                show.frames.addAll((ArrayList<Frame>) showsfile.getList(key + ".frames"));
            } else {
                getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[FireworkShow]: No 'frames' param found. Adding param...");
                showsfile.set(key+".frames",show.frames);
            }

            if ( showsfile.contains(key+".highest") ) { //Check if the parameter exists
                show.setHighest(showsfile.getBoolean(key+".highest"));
            } else {
                getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[FireworkShow]: No 'highest' param found. Adding param...");
                showsfile.set(key+".highest",show.getHighest()); //Set the default value in the show file
            }

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
        handler = new CommandHandler();
        fws = this;

        dataFolder = getDataFolder();

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
            showsfile.save(new File(dataFolder, fileName+".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: Add a config option to censor profanity in fireworks shows
    //  Try and make it handle Cunthorpe problem adequately.
    //  Alternatively have it hide them from the shows list.
    //  Also add param to make it private or public show. (Private shows only available to Authors/Editors/OP's, requires perm fireworks.shows.list.viewall)
    public static void createShow(String name)
    {
        String uuid = "";
        while ( uuid.isEmpty() || showsfile.contains(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
        shows.put(uuid, new Show());
        showsfile.set(uuid, shows.get(uuid));
        showsfile.set(uuid+".name", name);
        showsfile.set(uuid+".highest", shows.get(uuid).getHighest());
        showsfile.set(uuid+".frames", shows.get(uuid).frames);
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
