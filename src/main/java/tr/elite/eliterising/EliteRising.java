package tr.elite.eliterising;

import jdk.vm.ci.meta.Local;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;
import tr.elite.eliterising.Commands.*;
import tr.elite.eliterising.Events.*;
import tr.elite.eliterising.TabCompletes.*;

import java.io.InputStream;
import java.util.*;

import static org.bukkit.Bukkit.*;

/**
 *  <h1>EliteRising</h1>
 *  <p>EliteRising <span style="font-weight:bold">v1.3</span> lava rising package. (EN/TR)
 *  <p>Sadece Emir, Founder of <span style="font-weight:bold">EliteDevelopment</span></p>
 *  <p>VIA: <a href="https://www.spigotmc.org/resources/the-floor-is-lava.70255/">TheFloorIsLava</a></p>
 *  <p>NOTE: This plugin is modded package of TheFloorIsLava</p>
 *  <p>Commands: {@link CommandStart}, {@link CommandTeam}, {@link CommandMode}, {@link CommandVersion}, {@link CommandInfo}</p>
 */
public final class EliteRising extends JavaPlugin {
    public static EliteRising instance;
    FileConfiguration configuration = getConfig();

    public static Location spawn;
    private LavaUtils lavaUtils;
    public int size;
    private int startLevel;
    private int increaseAmount;
    public static boolean IS_RISING = false;
    public static boolean IS_STARTED = false;
    public static String TEMPLATE = ChatColor.GOLD + "[" + ChatColor.DARK_RED + "EliteRising" + ChatColor.GOLD + "] " + ChatColor.RESET;
    public static ArrayList<HashMap<String,ArrayList<String>>> CUSTOM_START_MODES = new ArrayList<>();
    public static Version VERSION = new Version("1.3");
    public static Language LANGUAGE;
    public static Colors COLORS;
    public static HashMap<String, CommandExecutor> EXECUTORS = new HashMap<>();
    public static HashMap<String, TabCompleter> COMPLETERS = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        configuration.options().copyDefaults(true);
        saveConfig();

        size = configuration.getInt("borderSize",200);
        startLevel = configuration.getInt("startHeight",-50);
        increaseAmount = configuration.getInt("increaseAmount",2);
        LANGUAGE = new Language(configuration.getString("language","en"));
        COLORS = new Colors(configuration.getString("language","en"));

        if (Material.getMaterial(getConfigration("block",configuration)) == null) {
            log(LANGUAGE.getValue("eliterising.main.invalidBlock").replaceAll("\\$",configuration.getString("block")));
            configuration.set("block", "LAVA");
        }

        spawn = getServer().getWorlds().get(0).getSpawnLocation();

        getServer().getWorlds().get(0).getWorldBorder().setCenter(spawn);
        getServer().getWorlds().get(0).getWorldBorder().setSize(size);
        getServer().setSpawnRadius(0);

        Location bottomRight = spawn.clone().subtract((double) size / 2D, 0, (double) size / 2D);
        Location topLeft = spawn.clone().add((double) size / 2D, 0, (double) size / 2D);

        lavaUtils = new LavaUtils(bottomRight, topLeft, startLevel, increaseAmount);

        Bukkit.getWorlds().get(0).setPVP(false);
        for (char ch : "c9ae4126bd57f380".toCharArray()) {
            teamColors.add(ChatColor.getByChar(ch));
        }
        setTakeDamage(false);

        /**
         * ELITERISING V1.3 PATCH NOTES
         *
         * ADDITIONS
         * -Added one more language support (EN/TR)
         * -You can add ANY language at config.yml (but first values must be same with TR/EN)
         * -Added /elversion command shows version of EliteRising
         * -Added /lang command changes or gets language of EliteRising
         * -Added TR/DE commands
         *
         * CHANGES & FIXES
         * -Changed DEFAULT values:
         * -Grace period time upgraded to 12 minutes from 10 minutes
         * -Border size upgraded to 220 blocks from 200
         * -Start height upgraded to -50 from -54
         * -Increase amount upgraded to 2 from 1 block
         * -Fixed if 1 player start game, that player's inventory didn't delete.
         */

        // GAME RULES
        getServer().getWorlds().get(0).setGameRule(GameRule.FIRE_DAMAGE,false);
        getServer().getWorlds().get(0).setGameRule(GameRule.DROWNING_DAMAGE,false);
        getServer().getWorlds().get(0).setGameRule(GameRule.FALL_DAMAGE,false);
        getServer().getWorlds().get(0).setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS,false);
        getServer().getWorlds().get(0).setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,true);
        getServer().getWorlds().get(0).setGameRule(GameRule.KEEP_INVENTORY,false);

        // EXECUTORS
        EXECUTORS.put("start",new CommandStart());
        EXECUTORS.put("info",new CommandInfo());
        EXECUTORS.put("elversion",new CommandVersion());
        EXECUTORS.put("elteam",new CommandTeam());
        EXECUTORS.put("davet",new CommandDavet());
        EXECUTORS.put("mode",new CommandMode());
        EXECUTORS.put("lang",new CommandLang());
        for (String command : EXECUTORS.keySet()) {
            instance.getCommand(command).setExecutor(EXECUTORS.get(command));
            for (String alias : instance.getCommand(command).getAliases()) {
                instance.getCommand(alias).setExecutor(EXECUTORS.get(command));
            }
        }

        // TAB COMPLETERS
        COMPLETERS.put("start",new StartComplete());
        COMPLETERS.put("info",new EmptyCompleter());
        COMPLETERS.put("elversion",new EmptyCompleter());
        COMPLETERS.put("elteam",new TeamComplete());
        COMPLETERS.put("davet",new EmptyCompleter());
        COMPLETERS.put("mode",new ModeComplete());
        COMPLETERS.put("lang",new LangComplete());
        for (String command : COMPLETERS.keySet()) {
            instance.getCommand(command).setTabCompleter(COMPLETERS.get(command));
            for (String alias : instance.getCommand(command).getAliases()) {
                instance.getCommand(alias).setTabCompleter(COMPLETERS.get(command));
            }
        }

        // EVENTS
        Listener[] events = new Listener[]{
                new PlayerDeath(),
                new PlayerChat(),
                new PlayerDamage(),
                new PlayerJoin(),
                new PlayerMove(),
                new PlayerQuit(),
                new PortalCreate(),
                new WardenSpawn()
        };
        for (Listener ls : events) {
            getServer().getPluginManager().registerEvents(ls,instance);
        }
    }

    public static void sendMessage(String msg, Player p) {
        p.sendMessage(TEMPLATE + msg);
    }

    public static void sendMessage(String msg) {
        Bukkit.broadcastMessage(TEMPLATE + msg);
    }

    public static void sendError(String msg, Player p) {
        p.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "EliteRising/" + LANGUAGE.getValue("eliterising.main.error") + ChatColor.GOLD + "] " + ChatColor.RED + msg);
    }

    public static void clearAllInventories() {
        if (getOnlinePlayers().size() == 1) {
            Player lastP = new ArrayList<>(getOnlinePlayers()).get(0);
            lastP.getInventory().clear();
        } else {
            for (Player p : getOnlinePlayers()) {
                if (p.getGameMode().equals(GameMode.SURVIVAL)) {
                    p.getInventory().clear();
                }
            }
        }
    }

    public static void log(Object o) {
        System.out.println("[EliteRising] " + o);
    }

    public static String removeFirstLastValues(String s) {
        return s.substring(1,s.length() - 1);
    }

    public static ArrayList<String> getStartModesName() {
        ArrayList<String> res = new ArrayList<>();
        for (HashMap<String,ArrayList<String>> h : CUSTOM_START_MODES) {
            res.add(removeFirstLastValues(String.valueOf(Collections.singletonList(h.keySet()).get(0)))); // modeName
        }
        return res;
    }

    public static boolean existStartMode(String name) {
        int i = 0;
        for (HashMap<String,ArrayList<String>> h : CUSTOM_START_MODES) {
            i += h.containsKey(name) ? 1 : 0;
        }
        return i > 0;
    }

    public static void destroyItems() {
        World world = getWorlds().get(0);
        List<Entity> entities = world.getEntities();
        for (Entity entity : entities) {
            if (entity instanceof Item) {
                entity.remove();
            }
        }
    }

    public static ChatColor keyColor(String key) {
        return ChatColor.valueOf(key.toUpperCase(Locale.ENGLISH));
    }

    public static void setGameModes(GameMode gm) {
        for (Player player : getOnlinePlayers()) {
            player.setGameMode(gm);
        }
    }

    public static String getConfigration(String s, FileConfiguration config) {
        return Objects.requireNonNull(config.getString(s));
    }

    public Location getSpawn() {
        return spawn;
    }

    public LavaUtils getLavaUtils() {
        return lavaUtils;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public Material getBlock() {
        return Material.getMaterial(Objects.requireNonNull(configuration.getString("block")));
    }

    public static ArrayList<ChatColor> teamColors = new ArrayList<>();

    public static boolean equalsOneMore(Object query,Object... values) {
        int i = 0;
        for (Object o : values) {
            i += query.equals(o) ? 1 : 0;
        }
        return i > 0;
    }

    public static boolean containsOneMore(String query,String... values) {
        int i = 0;
        for (String s : values) {
            i += query.contains(s) ? 1 : 0;
        }
        return i > 0;
    }

    public static ArrayList<String> removeFirstValues(int values, String[] args) {
        ArrayList<String> argues = new ArrayList<>(Arrays.asList(args));
        int i = 0;
        while (i < values) {
            argues.remove(0);
            i++;
        }
        return argues;
    }

    public static ArrayList<ChatColor> getColors(boolean isTeamColor) {
        if (isTeamColor) {
            return teamColors;
        } else {
            ArrayList<ChatColor> colors = new ArrayList<>();
            for (char ch : "123456780abcdef".toCharArray()) {
                colors.add(ChatColor.getByChar(ch));
            }
            return colors;
        }
    }

    public static boolean keyContains(UUID key, HashMap<UUID,String> map) {
        ArrayList<UUID> uuids = new ArrayList<>(map.keySet());
        return uuids.contains(key);
    }

    public static void setTakeDamage(boolean value) {
        TAKE_DAMAGE = value;
    }

    public static boolean TAKE_DAMAGE = false;

    public static ArrayList<Player> getPlayers() {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1).toLowerCase(Locale.ROOT);
    }
}