# FireworkShow++
Create a customizable Firework show with minecraft fireworks!

This plugin makes use of the bukkit serialization system, editing a firework show in the data files is a bad idea!

Original Creator: [iGufGuf](https://mcdev.igufguf.com)
Being Maintained (Possibly forever?) by [Jackietkfrost](https://am-x2.com) and [Zuwel](https://github.com/Zuwel)

Updated for version 1.16.3, Hasn't been tested on earlier versions.

## Commands
Aliases: /fireworkshow - /fws

* **/fws play <showname>**  
  - Start a firework show
  - fireworkshow.play

* **/fws create <showname>**  
  - Create a new firework show
  - fireworkshow.create

* **/fws delete <showname>**  
  - Delete a fireworksshow
  - fireworkshow.delete

* **/fws addframe <showname> <delay>**  
  - Add an empty frame to the show, delay is in ticks (1 second is 20 ticks)*
  - fireworkshow.addframe

* **/fws delframe <showname> <frameid>**  
  - Delete a frame
  - fireworkshow.delframe

* **/fws dupframe <showname> <frameid>**  
  - Duplicate a frame
  - fireworkshow.dupframe

* **/fws nwfw <showname>**  
  - Add the firework in your main hand to the last frame on your current position
  - fireworkshow.newfw
  
* **/fws nwfw <showname> <frameid>**  
  - Add the firework you're holding to the given frame on your current position
  - fireworkshow.newfw
