package com.igufguf.fireworkshow;

import com.igufguf.fireworkshow.objects.Frame;
import com.igufguf.fireworkshow.objects.Show;
import com.igufguf.fireworkshow.objects.fireworks.NormalFireworks;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Copyrighted 2017 iGufGuf
 *
 * This file is part of Ultimate Fireworkshow.
 *
 * Ultimate Fireworkshow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ultimate Fireworkshow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Ultimate Fireworkshow.  If not, see http://www.gnu.org/licenses/
 *
 **/
public class Main extends JavaPlugin {

    private static final HashMap<String, Show> shows = new HashMap<>();

    private static FileConfiguration showsfile = new YamlConfiguration();
    public static Main main;

    @Override
    public void onEnable() {
        main = this;

        File f = new File(getDataFolder(), "shows.yml");
        if ( !f.exists() ) {
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
        try {
            showsfile.load(f);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        for ( String key : showsfile.getKeys(false) ) {
            Show show = new Show();
            for ( Frame frame : (ArrayList<Frame>) showsfile.getList(key) ) {
                show.add(frame);
            }
            shows.put(key, show);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ( !cmd.getName().equalsIgnoreCase("fireworkshow") ) {
            return true;
        }

        if ( args.length == 0 || (args.length == 1 && args[0].equals("help")) ) {
            sender.sendMessage(ChatColor.GREEN + "/fws create <showname>" + ChatColor.GRAY + " Create a new fireworkshow");
            sender.sendMessage(ChatColor.GREEN + "/fws delete <showname>" + ChatColor.GRAY + " Delete a fireworkshow");
            sender.sendMessage(ChatColor.GREEN + "/fws addframe <showname> <delay>" + ChatColor.GRAY + " Add a frame to a show");
            sender.sendMessage(ChatColor.GREEN + "/fws delframe <showname> <frameid>" + ChatColor.GRAY + " Delete a frame from a show");
            sender.sendMessage(ChatColor.GREEN + "/fws dupframe <showname> <frameid>" + ChatColor.GRAY + " Duplicate a frame from a show");
            sender.sendMessage(ChatColor.GREEN + "/fws newfw <showname> (<frameid>)" + ChatColor.GRAY + " Set a firework on your location for a show");
            sender.sendMessage(ChatColor.GREEN + "/fws play <showname>" + ChatColor.GRAY + " Start a fireworkshow");
        }

        if ( args[0].equalsIgnoreCase("play")) {
            if ( args.length < 2 ) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " play <showname>");
                return true;
            }

            if ( sender instanceof Player && !sender.hasPermission("fireworkshow.stop")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if ( !shows.containsKey(args[1].toLowerCase()) ) {
                sender.sendMessage(ChatColor.RED + "That fireworkshow does not exist!");
                return true;
            }

            Show show = shows.get(args[1].toLowerCase());

            if ( show.isRunning() ) {
                sender.sendMessage(ChatColor.GREEN + "fireworkshow " + ChatColor.DARK_GREEN + args[1].toLowerCase() + ChatColor.GREEN + " is already running!");
                return true;
            }

            show.play();
            sender.sendMessage(ChatColor.GREEN + "You started fireworkshow " + ChatColor.DARK_GREEN + args[1].toLowerCase() + ChatColor.GREEN + "!");
            return true;
        }

        if ( args[0].equalsIgnoreCase("stop")) {
            if ( args.length != 2 ) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " stop <showname>");
                return true;
            }

            if ( sender instanceof Player && !sender.hasPermission("fireworkshow.play")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if ( args[1].equalsIgnoreCase("all") ) {
                for ( Show show : shows.values() ) {
                    show.stop();
                }

                sender.sendMessage(ChatColor.GREEN + "You stopped all fireworkshows!");
                return true;
            }

            if ( !shows.containsKey(args[1].toLowerCase()) ) {
                sender.sendMessage(ChatColor.RED + "That fireworkshow does not exist!");
                return true;
            }

            shows.get(args[1].toLowerCase()).stop();
            sender.sendMessage(ChatColor.GREEN + "You stopped fireworkshow " + ChatColor.DARK_GREEN + args[1].toLowerCase() + ChatColor.GREEN + "!");
            return true;
        }

        if ( !(sender instanceof Player) ) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players!");
            return true;
        }

        if ( args[0].equalsIgnoreCase("create") ) {
            if ( args.length != 2 ) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " create <showname>");
                return true;
            }

            if ( !sender.hasPermission("fireworkshow.create") ) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            String name = args[1].toLowerCase();

            if ( shows.containsKey(name) ) {
                sender.sendMessage(ChatColor.RED + "A show with that name already exists!");
                return true;
            }

            shows.put(name, new Show());
            showsfile.set(name, shows.get(name));
            sender.sendMessage(ChatColor.GREEN + "You created a fireworkshow with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        } else if ( args[0].equalsIgnoreCase("delete") ) {
            if ( args.length != 2 ) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " delete <showname>");
                return true;
            }

            if ( !sender.hasPermission("fireworkshow.delete") ) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if ( !shows.containsKey(name) ) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            shows.remove(name);
            showsfile.set(name, null);
            sender.sendMessage(ChatColor.GREEN + "You deleted the fireworkshow with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        } else if ( args[0].equalsIgnoreCase("addframe") ) {
            if ( args.length != 3 ) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " addframe <showname> <delay>");
                return true;
            }

            if ( !sender.hasPermission("fireworkshow.addframe") ) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if ( !shows.containsKey(name) ) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if ( !args[2].matches("[0-9]+") ) {
                sender.sendMessage(ChatColor.RED + "Invalid delay!");
                return true;
            }
            long delay = Long.valueOf(args[2]);

            if ( delay > 600 ) {
                sender.sendMessage(ChatColor.RED + "The delay can't be longer than 30 seconds!");
                return true;
            }

            shows.get(name).add(new Frame(delay));
            showsfile.set(name, shows.get(name));
            sender.sendMessage(ChatColor.GREEN + "You added a new frame (#" + ChatColor.YELLOW + shows.get(name).size() + ChatColor.GREEN + ") " +
                    "to the firworkshow with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        } else if ( args[0].equalsIgnoreCase("delframe") ) {
            if ( args.length != 3 ) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " delframe <showname> <frameid>");
                return true;
            }

            if ( !sender.hasPermission("fireworkshow.delframe") ) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if ( !shows.containsKey(name) ) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if ( !args[2].matches("[0-9]+") ||shows.get(name).size() < Integer.valueOf(args[2]) ) {
                sender.sendMessage(ChatColor.RED + "That frame does not exists!");
                return true;
            }
            int frame = Integer.valueOf(args[2]);

            shows.get(name).remove(frame-1);
            showsfile.set(name, shows.get(name));
            sender.sendMessage(ChatColor.GREEN + "You removed a frame from the fireworkshow with name" + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        } else if ( args[0].equalsIgnoreCase("dupframe") ) {
            if ( args.length != 3 ) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " dupframe <showname> <frameid>");
                return true;
            }

            if ( !sender.hasPermission("fireworkshow.dupframe") ) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if ( !shows.containsKey(name) ) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if ( !args[2].matches("[0-9]+") ||shows.get(name).size() < Integer.valueOf(args[2]) ) {
                sender.sendMessage(ChatColor.RED + "That frame does not exists!");
                return true;
            }
            int frameid = Integer.valueOf(args[2]);

            Frame frame = shows.get(name).get(frameid-1);
            shows.get(name).add(frame);
            showsfile.set(name, shows.get(name));
            sender.sendMessage(ChatColor.GREEN + "You duplicated a frame (#" + ChatColor.YELLOW + shows.get(name).size() + ChatColor.GREEN + ") " +
                    "from the fireworkshow with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        } else if ( args[0].equalsIgnoreCase("newfw") ) {
            if ( args.length < 2 ) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " newfw <showname>");
                return true;
            }

            if ( !sender.hasPermission("fireworkshow.newfw") ) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if ( !shows.containsKey(name) ) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            int frame;
            if ( args.length == 3 ) {
                if (!args[2].matches("[0-9]+") || shows.get(name).size() < Integer.valueOf(args[2])) {
                    sender.sendMessage(ChatColor.RED + "That frame does not exists!");
                    return true;
                }
                frame = Integer.valueOf(args[2]);
            } else {
                frame = shows.get(name).size();
            }

            Player p = (Player) sender;
            if ( p.getInventory().getItemInMainHand() == null ||p.getInventory().getItemInMainHand().getType() != Material.FIREWORK_ROCKET){
                sender.sendMessage(ChatColor.RED + "Please hold a firework or firework charge in your hand!");
                return true;
            }
            FireworkMeta meta = (FireworkMeta) p.getInventory().getItemInMainHand().getItemMeta();

            NormalFireworks nf = new NormalFireworks(meta, p.getLocation());
            shows.get(name).get(frame-1).add(nf);
            sender.sendMessage(ChatColor.GREEN + "You added firework to the fireworkshow with name " + ChatColor.DARK_GREEN + name
                    + ChatColor.GREEN + " on frame (#" + ChatColor.YELLOW + frame + ChatColor.GREEN + ")!");

            showsfile.set(name, shows.get(name));
        }

        try {
            showsfile.save(new File(getDataFolder(), "shows.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
