package com.igufguf.fireworkshow.objects.fireworks;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

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
public class NormalFireworks extends Fireworks {

    private FireworkMeta meta;

    public NormalFireworks(FireworkMeta meta, Location loc) {
        super(loc);
        this.meta = meta;
    }

    public FireworkMeta getMeta() {
        return meta;
    }

    @Override
    public void play(boolean highest) {
        Location newLoc = loc;
        if(highest) { newLoc = newLoc.getWorld().getHighestBlockAt(newLoc).getLocation().add(0.5,1,0.5); } // If the highest parameter is true for the show, it will spawn the entity at the highest block location.
        Firework firework = (org.bukkit.entity.Firework) loc.getWorld().spawnEntity(newLoc, EntityType.FIREWORK);
        firework.setFireworkMeta(meta);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("meta", meta);
        map.put("location", loc);

        return map;
    }

    public static NormalFireworks deserialize(Map<String, Object> args) {
        FireworkMeta meta = (FireworkMeta) args.get("meta");
        NormalFireworks nf = new NormalFireworks(meta, (Location) args.get("location"));
        return nf;
    }
}
