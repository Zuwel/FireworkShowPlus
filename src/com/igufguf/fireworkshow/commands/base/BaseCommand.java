package com.igufguf.fireworkshow.commands.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface BaseCommand {

    //Usage information displayed in the help menu and when there are command errors.
    //Example: "basecommand <params>"
    String usage();

    //A brief description of the commands functionality.
    //Example: "The building block of all other commands"
    String desc();

    //Key words for executing commands.
    //Example: {"basecommand","base","bc"}
    String[] aliases();

    //Permission associated with running this command
    //Example: "fireworkshow.category.item"
    String permission();

    //Flag for indicating if a player can use this command.
    //Example: true
    boolean playerExec();

    //Flag for indicating if the server can use this command.
    //Example: false
    boolean serverExec();

    //Command event execution function.
    boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args);

}
