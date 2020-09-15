package com.igufguf.fireworkshow.commands.menus;

import com.igufguf.fireworkshow.commands.base.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class MainMenuCommand implements BaseCommand {

    @Override
    public String usage() {
        return "mainmenu";
    }

    @Override
    public String desc() {
        return "The front facing menu when interacting with FireworkShow.";
    }

    @Override
    public String[] aliases() {
        return new String[]{"mainmenu", "main", "menu", "mm"};
    }

    @Override
    public String permission() {
        return "fireworkshow.mainmenu";
    }

    @Override
    public boolean playerExec() {
        return true;
    }

    @Override
    public boolean serverExec() {
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {


        //TODO: Actually make this do shit! Interactivity!
        sender.sendMessage(ChatColor.GOLD + "----------------[ " + ChatColor.GREEN + "Ultimate Firework Show" +
                ChatColor.GOLD + " ]----------------");
        for(int i=0;i<18;i++){
            sender.sendMessage("");
        }
        sender.sendMessage(ChatColor.GOLD + "----------------[ " + ChatColor.GREEN + "Ultimate Firework Show" +
                ChatColor.GOLD + " ]----------------");

        return true;

    }

}
