package com.zuwel.fireworkshowplus.commands;

import com.zuwel.fireworkshowplus.commands.base.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandHandler implements CommandExecutor {


    private static ArrayList<BaseCommand> commands = new ArrayList<BaseCommand>();

    public void register(BaseCommand cmd) {
        commands.add(cmd);
    }

    //This will be used to check if a command exists or not...
    //  Might delete if not used in major revisions. (Made in V2.1.0)
    public boolean exists(String name) {
        for ( BaseCommand cmd : commands ) {
            for ( String alias : cmd.aliases() ) {
                if ( alias.equalsIgnoreCase(name) ) {
                    return true;
                }
            }
        }
        return false;
    }

    //Getter method for the Executor.
    public BaseCommand getExecutor(String name) {
        for ( BaseCommand cmd : commands ) {
            for ( String alias : cmd.aliases() ) {
                if ( alias.equalsIgnoreCase(name) ) {
                    return cmd;
                }
            }
        }
        //Gotta return something if nothing found (in this case null)
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        //What runs if no extra arguments are given
        if(args.length == 0) {
            getExecutor("mainmenu").onCommand(sender, cmd, commandLabel, args);
            return true;
        }

        //What if there are arguments in the command? Such as /example args
        if(args.length > 0) {

            try {
                BaseCommand executor = getExecutor(args[0]);

                //Check if the command sender is allowed to execute the command
                if (sender instanceof ConsoleCommandSender && !executor.serverExec()) {
                    sender.sendMessage(ChatColor.RED + "The server is not allowed to use this command.");
                    return false;
                }
                if (sender instanceof Player && !executor.playerExec()) {
                    sender.sendMessage(ChatColor.RED + "Players are not allowed to use this command.");
                    return false;
                }

                if ( !sender.hasPermission(executor.permission()) ) {
                    sender.sendMessage(ChatColor.RED + "You do not have the permissions necessary to use this command.");
                    return false;
                }

                executor.onCommand(sender, cmd, commandLabel, args);
                return true;

            } catch (NullPointerException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "Unrecognized command!");
                return false;
            }

        }

        return false;
    }

    //TODO: Reimplement all the commands (preferably better than they are right now)
    //  Completed:
    //      - Create Show
    //      - Delete Show
    //      - Add Frame
    //  WIP:
    //      - Play Show
}