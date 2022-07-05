package tr.elite.eliterising;

import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import tr.elite.eliterising.Commands.*;
import tr.elite.eliterising.Events.*;
import tr.elite.eliterising.TabCompletes.*;

import java.util.*;

import static org.bukkit.Bukkit.*;

/**
 *  <h1>EliteRising</h1>
 *  <p>EliteRising <span style="font-weight:bold">v1.5</span> lava rising package. (EN/TR)
 *  <p>Sadece Emir, Founder of <span style="font-weight:bold">EliteDevelopment</span></p>
 *  <p>VIA: <a href="https://www.spigotmc.org/resources/the-floor-is-lava.70255/">TheFloorIsLava</a></p>
 *  <p>NOTE: This plugin is modded package of TheFloorIsLava</p>
 *  <p>Commands: {@link CommandStart}, {@link CommandTeam}, {@link CommandMode}, {@link CommandVersion}, {@link CommandInfo}, {@link CommandConfig}</p>
 */
public final class EliteRising extends JavaPlugin {
    public static EliteRising instance;
    FileConfiguration configuration = getConfig();

    public static Location spawn;
    public LavaUtils lavaUtils;
    public static int size;
    public static int startLevel;
    public static int increaseAmount;
    public static boolean IS_RISING = false;
    public static boolean IS_STARTED = false;
    public static boolean IS_FINISHED = false;
    public static String TEMPLATE = ChatColor.GOLD + "[" + ChatColor.DARK_RED + "EliteRising" + ChatColor.GOLD + "] " + ChatColor.AQUA;
    public static HashMap<String,ArrayList<String>> CUSTOM_GAME_MODES = new HashMap<>();
    public static Version VERSION;
    public static Language LANGUAGE;
    public static Colors COLORS;
    public static HashMap<String, CommandExecutor> EXECUTORS = new HashMap<>();
    public static HashMap<String, TabCompleter> COMPLETERS = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        configuration.options().copyDefaults(true);
        saveConfig();

        size = configuration.getInt("borderSize",220);
        startLevel = configuration.getInt("startHeight",-50);
        increaseAmount = configuration.getInt("increaseAmount",2);
        LANGUAGE = new Language(configuration.getString("language","en"));
        COLORS = new Colors(configuration.getString("language","en"));
        VERSION = new Version(configuration.getString("version","1.5"));

        if (Material.getMaterial(getConfigration("block",configuration)) == null) {
            log(LANGUAGE.getValue("eliterising.main.invalidBlock").replace("$",configuration.getString("block")));
            configuration.set("block", "LAVA");
        }

        spawn = getServer().getWorlds().get(0).getSpawnLocation();

        getServer().getWorlds().get(0).getWorldBorder().setCenter(spawn);
        getServer().getWorlds().get(0).getWorldBorder().setSize(size);
        getServer().setSpawnRadius(0);

        Location bottomRight = spawn.clone().subtract((double) size / 2D, 0, (double) size / 2D);
        Location topLeft = spawn.clone().add((double) size / 2D, 0, (double) size / 2D);

        lavaUtils = new LavaUtils(bottomRight, topLeft, startLevel, increaseAmount);

        getWorlds().get(0).setPVP(false);
        for (char ch : "c9ae4126bd57f380".toCharArray()) {
            teamColors.add(ChatColor.getByChar(ch));
        }
        setTakeDamage(false);

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
        EXECUTORS.put("eldavet",new CommandDavet());
        EXECUTORS.put("mode",new CommandMode());
        EXECUTORS.put("config",new CommandConfig());
        EXECUTORS.put("stats",new CommandStats());
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
        COMPLETERS.put("eldavet",new EmptyCompleter());
        COMPLETERS.put("stats",new EmptyCompleter());
        COMPLETERS.put("mode",new ModeComplete());
        COMPLETERS.put("config",new ConfigComplete());
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
        broadcastMessage(TEMPLATE + msg);
    }

    public static void sendError(String msg, Player p) {
        p.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "EliteRising/" + LANGUAGE.getValue("eliterising.main.error") + ChatColor.GOLD + "] " + ChatColor.RED + msg);
    }

    public static void clearAllInventories() {
        for (Player p : getOnlinePlayers()) {
            p.getInventory().clear();
        }
    }

    public static boolean isInteger(String o) {
        int i = 0;
        for (char ch : o.toCharArray()) {
            i += equalsOneMore(ch,'1','2','3','4','5','6','7','8','9','0') ? 1 : 0;
        }
        return o.length() == i;
    }

    public static void setConfig(String f,String s) {
        instance.configuration.set(f,s);
    }

    public static void log(Object o) {
        System.out.println("[EliteRising] " + o);
    }

    public static Integer getMaxValue(HashMap<Player, Integer> h) {
        return Collections.max(h.values());
    }

    public static Player reverse(HashMap<Player, Integer> h, Integer val) {
        ArrayList<Player> keys = new ArrayList<>(h.keySet());
        ArrayList<Integer> vals = new ArrayList<>(h.values());
        int i = vals.indexOf(val);
        return keys.get(i);
    }

    public static ArrayList<String> getStartModesName() {
        return new ArrayList<>(CUSTOM_GAME_MODES.keySet());
    }

    public static boolean existStartMode(String name) {
        int i = 0;
        for (String s : CUSTOM_GAME_MODES.keySet()) {
            i += s.equals(name) ? 1 : 0;
        }
        return i > 0;
    }

    public static boolean existStartModeConfig(String name) {
        return instance.getConfig().get("custom_modes.gameModes." + name) != null;
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

    public static boolean containsOneMore(PlayerInventory inventory, ItemStack... item) {
        int i = 0;
        for (ItemStack s : item) {
            i += inventory.contains(s) ? 1 : 0;
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