package com.zuwel.fireworkshowplus.commands.shows;

import com.zuwel.fireworkshowplus.commands.base.BaseCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class PlayCommand implements BaseCommand {


    @Override
    public String usage() {
        return null;
    }

    @Override
    public String desc() {
        return null;
    }

    @Override
    public String[] aliases() {
        return new String[] {"play", "p"};
    }

    @Override
    public String permission() {
        return "fireworkshow.show.play";
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



        return true;

    }
}
