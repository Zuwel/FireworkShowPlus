package com.igufguf.fireworkshow.objects;

import com.igufguf.fireworkshow.objects.fireworks.Fireworks;
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
public class Frame implements ConfigurationSerializable {

    public ArrayList<Fireworks> fireworks = new ArrayList<Fireworks>();
    public long delay;

    public Frame(long delay) {
        this.delay = delay;
    }

    public long getDelay() {
        return delay;
    }

    public void play(boolean highest) {
        for ( Fireworks fw : fireworks ) {
            fw.play(highest);
        }
    }

    public void add(Fireworks fw) {
        fireworks.add(fw);
    }

    public void remove(int i) {
        fireworks.remove(i);
    }

    public void remove(Fireworks fw) {
        fireworks.remove(fw);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("delay", delay);
        map.put("fireworks", fireworks);

        return map;
    }

    public static Frame deserialize(Map<String, Object> args) {
        long delay = ((Number) args.get("delay")).longValue();
        Frame frame = new Frame(delay);
        for ( Fireworks fw : (ArrayList<Fireworks>) args.get("fireworks") ) {
            frame.add(fw);
        }
        return frame;
    }
}
