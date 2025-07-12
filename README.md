# EliteRising for 1.19
EliteRising is the best lava rising package in Minecraft. EliteRising has 1.19, 1.18 and 1.17 versions. You can download the .jar plugin or if you wanna develop this project, I drop here the open-source project.

~(I will add v1.6 update to 1.16/17/18/19 and v1.6 is will last update)~
There'll be no update (July 13th, 2025)

## FAQ // GUIDE

Q: HOW CAN I START GAME? \
A: You can start game with /start (aliases: başlat, starten) command. If you wanna play with team or game modes, you can put game modes and team modes respectively after command.

Q: WHAT IS GAME MODES? \
A: Game modes define the items on given when game start (already ex-name is start modes)

Q: WHAT IS GAME MODE'S CONTENT? \
A: Game Modes are **NORMAL (4 apple)**, **ELYTRA (1 elytra + 4 firework rocket)**, **OVERPOWERED (Full diamond armour enchanted with protection 4 + 16 enchanted golden apple)**, **BUILD (2 stacks of cobblestone)**, **ARCHERY (Punch 1, Power 3 bow + 10 arrow)**

Q: WHAT IS TEAM MODES? \
A: Team modes define how to distribute teams by a number. (but except normal mode) And team modes are: NORMAL, SOLO, DUO, TRIPLE, SQUAD, PENTA, HALF

Q: CAN YOU EXPLAIN TEAM MODES ESPECIALLY HALF MODE? \
A: You can use /elteam command at normal team mode, so this mode's name is normal. Everyone is alone in solo mode. Other modes like duo/triple/squad/penta distribute teams by a number (eg: duo is 2 so there are 2 players each a team). Half mode seperates the server by two and creates two teams called blue and red.

Q: HOW CAN I CUSTOMIZE GAME MODES? \
A: You can use /mode command for this. Then enter a custom mode name. Afterwards, enter a few items. But items' typing is important. Because if items don't type correctly, there will be a few problems. So: \
  If you wanna ***only enchantment***: *minecraft:item_name{Enchantments:[{id:ench_name,level:lvl},{...}, ...]}* \
  If you wanna ***only amount***: *minecraft:item_name/amount* \
  If you wanna ***both amount and enchantment***: *minecraft:item_name{Enchantments:[{id:ench_name,level:lvl},{...}, ...]}/amount* \
  If you dont wanna anything: *minecraft:item_name* (if you dont enter amount, that will be 1 automatically) \
And if you created a custom game mode, you can use that with /start command on start.

Q: HOW CAN I CHAT TO GENERAL? \
A: If you have got a team, you can use the prefix "!" to chat general. Already, if you don't have got a team your messages that you sent, it sends to general automatically.

Q: HOW CAN I CREATE TEAM? \
A: You can use /elteam create command for this. By the way, you don't have to enter a name. EliteRising is gonna select a name which comes from team color and a color for you.

Q: HOW CAN I CHANGE GAME LANGUAGE? \
A: You can use /config language set <lang> for this. But there are 2 languages for now.

Q: HOW CAN I CHANGE CONFIGS? \
A: You can use /config <config> <get/set> <value> for this. Already, the language set is like this and comes from here. You can change language, grace period time (at least 1 minute) and inviting expire time for now.

Q: HOW CAN I SHOW STATS? \
A: Unfortunately, you cannot see your private stats but you can see MVPs with kills, taken damage, assists and dealt damage after the game.

Q: HOW CAN I ADD CUSTOM GAME/TEAM MODES WITH CONFIG? \
A: Go to config.yml in plugins/EliteRising (if you don't see this directory, you must start a server) After that, you can add custom game modes to custom_modes/gameModes in config.yml, or you can add custom team modes to custom_modes/teamModes (Already there are 2 example in file for game and team modes) And, you cannot create custom team modes by using /mode command.

## v1.5 Patch Notes // MODES UPDATE PART II

### Addinations
-Added customizable team modes \
-Added customizable game/team modes to config.yml \
-Added sound when plays on winning game

### Changes & Fixes
-Changed version command \
-Buffed Build Game mode \
-Nerfed Overpowered Game mode \
-Fixed if grace period equals or under zero, game winner's game will crash. \
-Fixed check argument the grace period if under zero.

#### Buffs
CONTENT OF BUILD GAME MODE BUFF: \
(INCREASE) 2 >> 3 stacks of cobblestone \
(ADDINATION) +1 diamond axe (no enchantment)

#### Nerfs
CONTENT OF OVERPOWERED GAME MODE NERF: \
(REDUCE) Diamond Armor Protection 4 >> 2 \
(REMOVE & REDUCE) 16 ENCHANTED GOLDEN APPLE >> 8 GOLDEN APPLE (NO ENCH)

## v1.4 Patch Notes // STATS UPDATE
Many bug fixes, new commands

### Addinations

-Added /stats command which shows the MVP \
-Added MVP System \
-Added timed invites (expires after 1 minute but this time is customizable) \
-Added /config command sets or gets config.yml \
-Added assists

### Changes & Fixes

-Changed /lang command to /config \
-Changed start mode's name to game modes \
-Fixed showed title to dead player(s), if there is last player standing in a team \
-Fixed information text that seems when used /info command \
-Fixed welcome message

## v1.3 Patch Notes // LANGUAGE UPDATE
A few but important changes.

### Addinations

-Added one more language support (EN/TR) \
-You can add ANY language at config.yml (but first values must be same with TR/EN) \
-Added /elversion command shows version of EliteRising \
-Added /lang command changes or gets language of EliteRising \
-Added TR/DE commands

### Changes & Fixes

-Changed DEFAULT values: \
-Grace period time upgraded to 12 minutes from 10 minutes \
-Border size upgraded to 220 blocks from 200 \
-Start height upgraded to -50 from -54 \
-Increase amount upgraded to 2 from 1 block \
-Fixed if 1 player start game, that player's inventory didn't delete.

## v1.2 Patch Notes // CUSTOM MODES UPDATE
Customizable start modes, new 2 commands.

### Addinations

-Added **CUSTOMIZABLE** start modes. \
-Added /mode command which creates, edits or deletes customizable start modes. (But you cannot edit or delete 5 main modes that given by automatically) \
-Added /info command that gives more information about the package. \
-Added Warden Spawn event which Warden makes not spawn and removes the darkness effect. \
-Added new team mode called PENTA which distrubutes teams for five.

### Changes & Fixes

-~~Changed the game language to fully English. (I can add other languages at v1.3)~~ \
-Changed the command called /team_el to /elteam \
-Fixed bug the if winner team has dead player, showed title called "game over" to dead player(s) (GAME OVER -> WON)

## v1.1 Patch Notes (1.17-1.18) // TEAMS AND MODES UPDATE
Teams and start/team modes and a few changes

### Commands
-Added team called ~~/team_el~~ in English, ~~/takım~~ in Turkish command. \
-Team command's args are create, leave, message, list and invite (~~but Turkish for now~~) \
-You can run the team command in normal team mode.

### Modes
-Added team and start modes. \
-~~Modes aren't customizable for now.~~

#### Team Modes
Team modes are ~~6~~. These are: \
Normal, Solo, Duo, Triple, Squad, Half \
These defines how to distribute teams. For example, duo distributes teams foreach 2 players. Half mode seperates server by two and creates two teams called Red and Blue.

#### Start Modes
Start modes are 5. These're: \
Normal, Elytra, Overpowered (op), Build and Archery. \
Normal mode's content: 4 apple \
Elytra: Elytra and 5 firework rockets \
Overpowered (op): Full protection 4 diamond armour set and 16 ench. golden apple \
Build: 2 stacks of cobblestone \
Archery: Power 3 and Punch 1 bow and 10 arrows

### Changes
-Removed border shrinking. \
-Removed draw. \
-Added plugin icon. \
-Added tab complete on commands. \
-Removed help command \
-New improved chat. \
-Added welcome message (~~Turkish for now~~).

## About
DEVELOPED BY: ***Sadece Emir, Elite Development***

ORIGINAL CONCEPT: *This is not original project. EliteRising is moded package of [TheFloorIsLava](https://github.com/rtm516/TheFloorIsLava).*

FOLLOW ME EVERYWHERE: \
[Instagram](https://www.instagram.com/sadece.emir0/) \
[Youtube](https://www.youtube.com/channel/UC6IvUFue9GxZdcnE9oYdtbQ) \
[Twitter](https://twitter.com/SadeceEmir0)
