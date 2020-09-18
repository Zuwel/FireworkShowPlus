package com.zuwel.fireworkshowplus.objects.fireworks;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashMap;
import java.util.Map;


public class NormalFireworks extends Fireworks {

    private FireworkMeta meta;

    public NormalFireworks(FireworkMeta meta, Location loc) {
        super(loc);
        this.meta = meta;
    }

    public FireworkMeta getMeta() {
        return meta;
    }

    //TODO: Do test case for null pointer positioning on get highest. Make sure null CAN happen or CAN'T
    //  WARNING: This might break the plugin since no try catches.
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
        return new NormalFireworks(meta, (Location) args.get("location"));
    }
}
