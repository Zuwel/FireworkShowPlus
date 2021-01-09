package com.zuwel.fireworkshowplus.commands.shows;

import com.zuwel.fireworkshowplus.FireworkShow;
import com.zuwel.fireworkshowplus.commands.base.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class DeleteShowCommand implements BaseCommand {

    @Override
    public String usage() {
        return "deleteshow <showid>";
    }

    @Override
    public String desc() {
        return "Delete a show.";
    }

    @Override
    public String[] aliases() {
        return new String[] {"deleteshow", "removeshow"};
    }

    @Override
    public String permission() {
        return "fireworkshow.show.delete";
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
    public int minArgs() {
        return 1;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {


        String uuid = "";
        if(args.length > 0) {
            uuid = args[1];
        }

        if ( uuid.isEmpty() ) {
            sender.sendMessage(ChatColor.RED + "No ID specified.");
            return false;
        }

        if ( !FireworkShow.getShows().containsKey(uuid) ) {
            sender.sendMessage(ChatColor.RED + "No firework show exists with the ID " + ChatColor.DARK_GREEN + uuid + ChatColor.GREEN + ".");
            return false;
        }

        if (sender instanceof ConsoleCommandSender == false || FireworkShow.getShows().get(uuid).getAuthor() != ((Player) sender).getUniqueId().toString()) {
            sender.sendMessage(ChatColor.RED + "You do not have the permissions necessary to use this command.");
            return false;
        }

        if ( !args[args.length - 1].equalsIgnoreCase("confirm")) {
            sender.sendMessage(ChatColor.YELLOW + "If you're sure you want to delete this show, run the last command with '" + ChatColor.GREEN + "confirm" + ChatColor.YELLOW + "' written at the end.");
            return false;
        }

        if ( FireworkShow.deleteShow(uuid) ) {
            sender.sendMessage(ChatColor.GREEN + "You " + ChatColor.RED + "deleted" + ChatColor.GREEN + " the fireworks show with the ID " + ChatColor.DARK_GREEN + uuid + ChatColor.GREEN + "!");
        }

        return true;

    }

}