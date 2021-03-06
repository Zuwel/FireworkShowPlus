package com.igufguf.fireworkshow.commands;

import com.igufguf.fireworkshow.FireworkShow;
import com.igufguf.fireworkshow.objects.fireworks.NormalFireworks;
import com.igufguf.fireworkshow.objects.Frame;
import com.igufguf.fireworkshow.objects.Show;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!cmd.getName().equalsIgnoreCase("fireworkshow")) { return true; }

        //Help command for Fireworks Shows
        if (args.length < 1 || (args.length == 1 && args[0].equals("help")))
        {
            sender.sendMessage(ChatColor.GREEN + "/fws create <showname>" + ChatColor.GRAY + " Create a new fireworkshow");
            sender.sendMessage(ChatColor.GREEN + "/fws delete <showname>" + ChatColor.GRAY + " Delete a fireworkshow");
            sender.sendMessage(ChatColor.GREEN + "/fws addframe <showname> <delay>" + ChatColor.GRAY + " Add a frame to a show");
            sender.sendMessage(ChatColor.GREEN + "/fws delframe <showname> <frameid>" + ChatColor.GRAY + " Delete a frame from a show");
            sender.sendMessage(ChatColor.GREEN + "/fws dupframe <showname> <frameid>" + ChatColor.GRAY + " Duplicate a frame from a show");
            sender.sendMessage(ChatColor.GREEN + "/fws newfw <showname> (<frameid>)" + ChatColor.GRAY + " Set a firework on your location for a show");
            sender.sendMessage(ChatColor.GREEN + "/fws play <showname>" + ChatColor.GRAY + " Start a fireworkshow");
            sender.sendMessage(ChatColor.GREEN + "/fws highest <showname> <true/false>" + ChatColor.GRAY + " Set if the fireworks will spawn on the highest block available or where it was initially placed");
            return true;
        }
        //Play Fireworks show command
        if (args[0].equalsIgnoreCase("play"))
        {
            if ( args.length < 2 )
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " play <showname>");
                return true;
            }

            if (sender instanceof Player && !sender.hasPermission("fireworkshow.stop"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if ( !FireworkShow.getShows().containsKey(args[1].toLowerCase()) )
            {
                sender.sendMessage(ChatColor.RED + "That fireworkshow does not exist!");
                return true;
            }

            Show show = FireworkShow.getShows().get(args[1].toLowerCase());

            if ( show.isRunning() )
            {
                sender.sendMessage(ChatColor.GREEN + "fireworkshow " + ChatColor.DARK_GREEN + args[1].toLowerCase() + ChatColor.GREEN + " is already running!");
                return true;
            }

            show.play();
            sender.sendMessage(ChatColor.GREEN + "You started the " + ChatColor.DARK_GREEN + args[1].toLowerCase() + ChatColor.GREEN + "Firework show!");
            return true;
        }

        //Stop Fireworks show command
        if (args[0].equalsIgnoreCase("stop"))
        {
            if ( args.length != 2 )
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " stop <showname>");
                return true;
            }

            if ( sender instanceof Player && !sender.hasPermission("fireworkshow.play"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if ( args[1].equalsIgnoreCase("all") )
            {
                for ( Show show : FireworkShow.getShows().values() )
                {
                    show.stop();
                }

                sender.sendMessage(ChatColor.GREEN + "You stopped all fireworkshows!");
                return true;
            }

            if ( !FireworkShow.getShows().containsKey(args[1].toLowerCase()) )
            {
                sender.sendMessage(ChatColor.RED + "That fireworkshow does not exist!");
                return true;
            }

            FireworkShow.getShows().get(args[1].toLowerCase()).stop();
            sender.sendMessage(ChatColor.GREEN + "You stopped fireworkshow " + ChatColor.DARK_GREEN + args[1].toLowerCase() + ChatColor.GREEN + "!");
            return true;
        }

        if ( !(sender instanceof Player) )
        {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players!");
            return true;
        }
        //Create Fireworks command
        if ( args[0].equalsIgnoreCase("create"))
        {
            if ( args.length != 2 )
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " create <showname>");
                return true;
            }

            if ( !sender.hasPermission("fireworkshow.create") )
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            String name = args[1].toLowerCase();

            if ( FireworkShow.getShows().containsKey(name) )
            {
                sender.sendMessage(ChatColor.RED + "A show with that name already exists!");
                return true;
            }

            try {
                FireworkShow.createShow(name);
                sender.sendMessage(ChatColor.GREEN + "You created a fireworkshow with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
            } catch (Exception e) {
                System.out.println(e.toString());
                sender.sendMessage(ChatColor.RED + "An error occurred while attempting to create a firework show!");
            }
        }
        //Delete Fireworks Command
        else if (args[0].equalsIgnoreCase("delete"))
        {
            if (args.length != 2 )
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " delete <showname>");
                return true;
            }

            if (!sender.hasPermission("fireworkshow.delete"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if (!FireworkShow.getShows().containsKey(name))
            {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            FireworkShow.getShows().remove(name);
            FireworkShow.getShowFiles().set(name, null);
            sender.sendMessage(ChatColor.GREEN + "You deleted the Fireworks show " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        }

        //Add an Empty frame for a show with a delay
        else if (args[0].equalsIgnoreCase("addframe"))
        {
            if (args.length != 3)
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " addframe <showname> <delay>");
                return true;
            }

            if (!sender.hasPermission("fireworkshow.addframe"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if (!FireworkShow.getShows().containsKey(name)) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if (!args[2].matches("[0-9]+"))
            {
                sender.sendMessage(ChatColor.RED + "Invalid delay!");
                return true;
            }
            long delay = Long.valueOf(args[2]);

            if (delay > 600)
            {
                sender.sendMessage(ChatColor.RED + "The delay can't be longer than 30 seconds!");
                return true;
            }

            FireworkShow.getShows().get(name).frames.add(new Frame(delay));
            FireworkShow.getShowFiles().set(name, FireworkShow.getShows().get(name));
            sender.sendMessage(ChatColor.GREEN + "You added a new frame (#" + ChatColor.YELLOW + FireworkShow.getShows().get(name).frames.size() + ChatColor.GREEN + ") " +
                    "to the firworkshow with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        }

        //Delete a frame from the show
        else if (args[0].equalsIgnoreCase("delframe"))
        {
            if (args.length != 3)
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " delframe <showname> <frameid>");
                return true;
            }
            //Check for Delete Frame permission
            if (!sender.hasPermission("fireworkshow.delframe"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            //Show does not exist
            if (!FireworkShow.getShows().containsKey(name))
            {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }
            //Frame does not exist
            if ( !args[2].matches("[0-9]+") ||FireworkShow.getShows().get(name).frames.size() < Integer.valueOf(args[2]) )
            {
                sender.sendMessage(ChatColor.RED + "That frame does not exists!");
                return true;
            }
            int frame = Integer.valueOf(args[2]);

            FireworkShow.getShows().get(name).frames.remove(frame-1);
            FireworkShow.getShowFiles().set(name, FireworkShow.getShows().get(name));
            sender.sendMessage(ChatColor.GREEN + "You removed a frame from the fireworkshow with name" + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        }
        //Duplicate last frame in the show
        else if ( args[0].equalsIgnoreCase("dupframe") )
        {
            if (args.length != 3)
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " dupframe <showname> <frameid>");
                return true;
            }

            if (!sender.hasPermission("fireworkshow.dupframe"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if (!FireworkShow.getShows().containsKey(name))
            {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if (!args[2].matches("[0-9]+") ||FireworkShow.getShows().get(name).frames.size() < Integer.valueOf(args[2]))
            {
                sender.sendMessage(ChatColor.RED + "That frame does not exists!");
                return true;
            }
            int frameid = Integer.valueOf(args[2]);

            Frame frame = new Frame(FireworkShow.getShows().get(name).frames.get(frameid-1));
            FireworkShow.getShows().get(name).frames.add(frame);
            FireworkShow.getShowFiles().set(name, FireworkShow.getShows().get(name));
            sender.sendMessage(ChatColor.GREEN + "You duplicated a frame (#" + ChatColor.YELLOW + FireworkShow.getShows().get(name).frames.size() + ChatColor.GREEN + ") " +
                    "from the fireworkshow with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        }

        //Add new held firework into last, or specified frame.
        else if (args[0].equalsIgnoreCase("newfw"))
        {
            if (args.length < 2)
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " newfw <showname>");
                return true;
            }

            if (!sender.hasPermission("fireworkshow.newfw"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if (!FireworkShow.getShows().containsKey(name))
            {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if (FireworkShow.getShows().get(name).frames.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "There are no frames in this show!");
                return true;
            }

            int frame;
            if (args.length == 3)
            {
                if (!args[2].matches("[0-9]+") || FireworkShow.getShows().get(name).frames.size() < Integer.valueOf(args[2]))
                {
                    sender.sendMessage(ChatColor.RED + "That frame does not exists!");
                    return true;
                }
                frame = Integer.valueOf(args[2]);
            }
            else
            {
                frame = FireworkShow.getShows().get(name).frames.size();
            }
            Player p = (Player) sender;
//          if ( p.getInventory().getItemInMainHand() == null ||p.getInventory().getItemInMainHand().getType() != Material.FIREWORK_ROCKET)
            if (p.getInventory().getItemInMainHand().getType() != Material.FIREWORK_ROCKET)
            {
                sender.sendMessage(ChatColor.RED + "Please hold a firework or firework charge in your hand!");
                return true;
            }
            FireworkMeta meta = (FireworkMeta) p.getInventory().getItemInMainHand().getItemMeta();
            NormalFireworks nf = new NormalFireworks(meta, p.getLocation());
            FireworkShow.getShows().get(name).frames.get(frame-1).add(nf);
            sender.sendMessage(ChatColor.GREEN + "You added firework to the Firework Show with name " + ChatColor.DARK_GREEN + name
                    + ChatColor.GREEN + " on frame (#" + ChatColor.YELLOW + frame + ChatColor.GREEN + ")!");

            FireworkShow.getShowFiles().set(name, FireworkShow.getShows().get(name));
        }

        else if(args[0].equalsIgnoreCase("list"))
        {
            Set<String> set = FireworkShow.getShowFiles().getKeys(false);
            String[] list = set.toArray(new String[set.size()]);

            int maxItems = 10;
            int page = 0;
            if (args.length > 1) { page = Integer.parseInt(args[1]) - 1; }

            if (page > Math.ceil(list.length/maxItems)|| page < 0)
            {
                sender.sendMessage(ChatColor.GREEN + "No fireworks found! Try /fws create <fireworks_id>."); //Replace with more user friendly message.
                return true;
            }
            sender.sendMessage("Firework Shows || Page " + (page+1));
            sender.sendMessage("-----------------------"); //Format this shit too
            for(int i=0;i<maxItems;i++)
            {
                if( (page*maxItems) + i >= list.length ) { break; }
                sender.sendMessage(list[(page*maxItems) + i]);
            }
        }

        else if ( args[0].equalsIgnoreCase("highest") ) {
            if ( args.length < 2 ) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " highest <showname> <true/false>");
                return true;
            }

            if ( !sender.hasPermission("fireworkshow.highest") ) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();


            if ( !FireworkShow.getShows().containsKey(name) ) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if ( args[2].equalsIgnoreCase("true") ) {
                FireworkShow.getShows().get(name).setHighest(true);
            } else if ( args[2].equalsIgnoreCase("false")) {
                FireworkShow.getShows().get(name).setHighest(false);
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid input! Use 'true' or 'false'.");
                return true;
            }
            FireworkShow.getShowFiles().set(name, FireworkShow.getShows().get(name));
            sender.sendMessage(ChatColor.GREEN + "You set highest parameter for '" + ChatColor.DARK_GREEN + name
                    + ChatColor.GREEN + "' to " + FireworkShow.getShows().get(name).getHighest() + ".");
        }

        FireworkShow.saveShows();
        return true;
    }

}