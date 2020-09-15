package com.igufguf.fireworkshow.commands.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface BaseCommand {

    //Usage information displayed in the help menu and when there are command errors.
    //Example: "basecommand <params>"
    public String usage();

    //A brief description of the commands functionality.
    //Example: "The building block of all other commands"
    public String desc();

    //Key words for executing commands.
    //Example: {"basecommand","base","bc"}
    public String[] aliases();

    //Permission associated with running this command
    public String permission();

    //Flag for indicating if a player can use this command.
    //Example: true
    public boolean playerExec();

    //Flag for indicating if the server can use this command.
    //Example: false
    public boolean serverExec();

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args);

}
