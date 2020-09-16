package com.zuwel.fireworkshowplus.commands.frames;

import com.zuwel.fireworkshowplus.commands.base.BaseCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AddFrameCommand implements BaseCommand {
    @Override
    public String usage() {
        return "addframe <showid> <delay>";
    }

    @Override
    public String desc() {
        return "Add frame to the show.";
    }

    @Override
    public String[] aliases() {
        return new String[] {"addframe", "newframe", "addf", "newf"};
    }

    @Override
    public String permission() {
        return "fireworkshow.frame.add";
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

        //TODO: Implement old command handler code!

        return false;
    }
}
