package com.igufguf.fireworkshow.objects.fireworks;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

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
public abstract class Fireworks implements ConfigurationSerializable {

    public Location loc;

    public Fireworks(Location loc) {
        this.loc = loc;
    }

    public Fireworks(Fireworks f) { loc = f.getPos().clone(); }

    public abstract void play(boolean highest);

    public void setPos(Location loc) {
        this.loc = loc;
    }
    public Location getPos() {
        return loc;
    }

}
