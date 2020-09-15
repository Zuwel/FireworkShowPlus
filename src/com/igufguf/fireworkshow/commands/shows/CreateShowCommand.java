package com.igufguf.fireworkshow.commands.shows;

import com.igufguf.fireworkshow.FireworkShow;
import com.igufguf.fireworkshow.commands.base.BaseCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CreateShowCommand implements BaseCommand {

    @Override
    public String usage() {
        return "createshow <showname>";
    }

    @Override
    public String desc() {
        return "Create a new show with a name of your choice.";
    }

    @Override
    public String[] aliases() {
        return new String[] {"createshow", "newshow"};
    }

    @Override
    public String permission() {
        return "fireworkshow.show.create";
    }

    @Override
    public boolean playerExec() {
        return true;
    }

    @Override
    public boolean serverExec() {
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        String name = "New Show";

        //If there are additional arguments, concatenate them and use them as the name.
        if (args.length > 0) {
            StringBuilder argsJoin = new StringBuilder(args[1]);
            for (int i = 2; i < args.length; i++) {
                argsJoin.append(" ").append(args[i]);
            }
            name = argsJoin.toString();
        }

        FireworkShow.createShow(name);
        sender.sendMessage(ChatColor.GREEN + "You created a fireworks show with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");

        return true;

    }

}
