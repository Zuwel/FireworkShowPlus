package com.zuwel.fireworkshowplus.commands.frames;

import com.zuwel.fireworkshowplus.FireworkShow;
import com.zuwel.fireworkshowplus.commands.base.BaseCommand;
import com.zuwel.fireworkshowplus.objects.Frame;
import org.bukkit.ChatColor;
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
    public int minArgs() {
        return 2;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        String uuid = args[1].toLowerCase();

        if (!FireworkShow.getShows().containsKey(uuid)) {
            sender.sendMessage(ChatColor.RED + "A show with that uuid doesn't exist!");
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

        FireworkShow.getShows().get(uuid).getFrames().add(new Frame(delay));
        FireworkShow.getShowFiles().set(uuid, FireworkShow.getShows().get(uuid));
        sender.sendMessage(ChatColor.GREEN + "You added a new frame (#" + ChatColor.YELLOW + FireworkShow.getShows().get(uuid).getFrames().size() + ChatColor.GREEN + ") " +
                "to the firworkshow with name " + ChatColor.DARK_GREEN + FireworkShow.getShows().get(uuid).getName() + ChatColor.GREEN + "!");

        return true;

    }
}
