package com.igufguf.fireworkshow.objects;

import com.igufguf.fireworkshow.FireworkShow;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyrighted 2017 iGufGuf
 *
 * This file is part of Ultimate Fireworkshow.
 *
 * Ultimate Fireworkshow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ultimate Fireworkshow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Ultimate Fireworkshow.  If not, see http://www.gnu.org/licenses/
 *
 **/
public class Show implements ConfigurationSerializable {

    private String name;
    public ArrayList<Frame> frames = new ArrayList<Frame>();
    private ArrayList<Integer> taskids = new ArrayList<Integer>();
    private boolean running = false;
    private boolean highest = false;

    public Show() {
        this("New Show");
    }

    public Show(String name) {
        this.name = name;
    }

    public void play() {
        if ( running ) return;
        running = true;

        long current = 0;
        for ( final Frame f : frames ) {
            current += f.getDelay();
            taskids.add(Bukkit.getScheduler().scheduleSyncDelayedTask(FireworkShow.fws, new Runnable() {
                @Override
                public void run() {
                    f.play(highest);
                }
            }, current));
        }

        taskids.add(Bukkit.getScheduler().scheduleSyncDelayedTask(FireworkShow.fws, new Runnable() {
            @Override
            public void run() {
                running = false;
                taskids.clear();
            }
        }, current));
    }

    public void stop() {
        if ( !running ) return;

        for ( int id : taskids ) {
            Bukkit.getScheduler().cancelTask(id);
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setHighest(boolean highest) {
        this.highest = highest;
    }
    public boolean getHighest() {
        return highest;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("highest", highest);
        map.put("frames", frames);

        return map;
    }

    public static Show deserialize(Map<String, Object> args) {
        boolean highest = (Boolean) args.get("highest");
        Show show = new Show();
        show.setHighest(highest);
        for ( Frame f : (ArrayList<Frame>) args.get("frames") ) {
            show.frames.add(f);
        }
        return show;
    }
}
