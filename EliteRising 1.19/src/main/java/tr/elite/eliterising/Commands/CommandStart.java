package tr.elite.eliterising.Commands;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import tr.elite.eliterising.EnchantmentNullException;
import tr.elite.eliterising.LavaUtils;
import tr.elite.eliterising.StartModes;
import tr.elite.eliterising.TeamModes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static org.bukkit.Bukkit.*;
import static tr.elite.eliterising.EliteRising.*;
import static tr.elite.eliterising.Teams.*;

public class CommandStart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            getLogger().info("Sender must be a player!");
            return true;
        }
        Player player = (Player) sender;
        if (!(player.hasPermission("start"))) {
            sendError("You have no permission to run this command",player);
            return true;
        }

        if (IS_STARTED || IS_RISING) {
            sendError("Game already started!",player);
            return true;
        }

        if (args.length >= 1) {
            if (equalsOneMore(args[0],"normal","elytra","overpowered","archery","build")) {
                START_MODE = StartModes.getModeByName(args[0]);
            } else {
                if (existStartMode(args[0])) {
                    CUSTOM_MODE_NAME = args[0];
                } else {
                    sendError("Please enter a valid start mode.",player);
                    return true;
                }
            }
        }

        if (args.length >= 2) {
            TEAM_MODE = TeamModes.getModeByName(args[1]);
            if (TEAM_MODE == null) {
                sendError("Please enter a valid team mode.",player);
                return true;
            }
        }

        clearAllInventories();
        setTakeDamage(false);

        ItemStack netherite_pickaxe = getPickaxe();

        if (isCustom()) {
            for (HashMap<String, ArrayList<String>> h : CUSTOM_START_MODES) {
                for (ItemStack item : StartModes.parseItems(h.get(args[0]))) {
                    for (Player p : getOnlinePlayers()) {
                        p.getInventory().addItem(item);
                    }
                }
            }
        } else {
            for (ItemStack item : START_MODE.getItems()) {
                for (Player p : getOnlinePlayers()) {
                    p.getInventory().addItem(item);
                }
            }
        }

        if (getPlayers().size() % TEAM_MODE.getNumber() > 0) {
            sendError("Number of players is not compatible to this mode.",player);
            return true;
        }
        if (TEAM_MODE != TeamModes.NORMAL) {
            distributeTeams(TEAM_MODE);
        }

        getServer().getWorlds().get(0).setTime(1000);

        bbs.setTitle(ChatColor.GOLD + "Remaining Time " + ChatColor.YELLOW + gracePeriod + " Minutes");

        for (Player players : Bukkit.getOnlinePlayers()) {
            bbs.addPlayer(players);
            bbs.setVisible(true);
            players.setGameMode(GameMode.SURVIVAL);

            for (PotionEffect pot : players.getActivePotionEffects()) {
                players.removePotionEffect(pot.getType());
            }

            players.getInventory().addItem(netherite_pickaxe);
            players.setHealth(20);
            players.setSaturation(20);
            players.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 255, false, false));

            players.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "START!",capitalize(isCustom() ? CUSTOM_MODE_NAME : START_MODE.name()) + " Mode", 5, 100, 5);
        }

        setGameModes(GameMode.SURVIVAL);

        if (task == null) {
            task = new BukkitRunnable() {
                int seconds = gracePeriod * 60;
                @Override
                public void run() {
                    if ((seconds -= 1) == 0) {
                        task.cancel();
                    } else {
                        int sec = seconds % 60;
                        int min = seconds / 60;
                        int hrs = min / 60;
                        double progress = seconds / ((double) gracePeriod * 60);
                        bbs.setProgress(progress);
                        String TITLE = ChatColor.YELLOW + "Remaining Time " + ChatColor.GOLD + (hrs > 0 ? hrs + " Hours " : "") + (min > 0 ? min + " Minutes " : "") + (sec > 0 ? sec + " Seconds" : "");
                        bbs.setTitle(TITLE);
                    }
                }
            }.runTaskTimer(instance, 0L, 20L);
        }

        getScheduler().scheduleSyncDelayedTask(instance, () -> {
            doLava();
            setTakeDamage(true);
            IS_RISING = true;
            getWorlds().get(0).setPVP(true);
            getServer().getWorlds().get(0).setGameRule(GameRule.FIRE_DAMAGE,true);
            getServer().getWorlds().get(0).setGameRule(GameRule.DROWNING_DAMAGE,true);
            getServer().getWorlds().get(0).setGameRule(GameRule.FALL_DAMAGE,true);
        }, 20L * 60L * ((long) gracePeriod));

        IS_STARTED = true;
        return true;
    }

    public static StartModes START_MODE = StartModes.NORMAL;
    public static String CUSTOM_MODE_NAME = "";
    public static TeamModes TEAM_MODE = TeamModes.NORMAL;
    private BukkitTask task;
    public static int gracePeriod = instance.getConfiguration().getInt("gracePeriod",10);

    public static BossBar bbs = createBossBar("EliteRising", BarColor.YELLOW, BarStyle.SEGMENTED_10);

    public static ItemStack getPickaxe() {
        ItemStack netherite_pickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
        netherite_pickaxe.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);
        netherite_pickaxe.addEnchantment(Enchantment.DIG_SPEED, 5);
        netherite_pickaxe.addEnchantment(Enchantment.DURABILITY, 3);
        ItemMeta m = netherite_pickaxe.getItemMeta();
        assert m != null;
        m.setDisplayName(ChatColor.GOLD + "Elite" + ChatColor.YELLOW + "Rising");
        netherite_pickaxe.setItemMeta(m);
        return netherite_pickaxe;
    }

    private void lavaTimer() {
        getScheduler().scheduleSyncDelayedTask(instance, this::doLava, 0L);
    }

    public static boolean isCustom() {
        return !Objects.equals(CUSTOM_MODE_NAME, "");
    }

    private void doLava() {
        bbs.setProgress(1);
        bbs.setTitle(ChatColor.YELLOW + "Lava will rise after 10 seconds!");

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 9 saniye kaldı
            bbs.setProgress(0.9);
            bbs.setTitle(ChatColor.YELLOW + "Lava will rise after 9 seconds!");
        }, 20L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 8 saniye kaldı
            bbs.setProgress(0.8);
            bbs.setTitle(ChatColor.YELLOW + "Lava will rise after 8 seconds!");
        }, 20L * 2L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 7 saniye kaldı
            bbs.setProgress(0.7);
            bbs.setTitle(ChatColor.YELLOW + "Lava will rise after 7 seconds!");
        }, 20L * 3L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 6 saniye kaldı
            bbs.setProgress(0.6);
            bbs.setTitle(ChatColor.YELLOW + "Lava will rise after 6 seconds!");
        }, 20L * 4L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 5 saniye kaldı
            bbs.setProgress(0.5);
            bbs.setTitle(ChatColor.YELLOW + "Lava will rise after 5 secondsk!");
        }, 20L * 5L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 4 saniye kaldı
            bbs.setProgress(0.4);
            bbs.setTitle(ChatColor.YELLOW + "Lava will rise after 4 seconds!");
        }, 20L * 6L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 3 saniye kaldı
            bbs.setProgress(0.3);
            bbs.setTitle(ChatColor.YELLOW + "Lava will rise after 3 seconds!");
        }, 20L * 7L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 2 saniye kaldı
            bbs.setProgress(0.2);
            bbs.setTitle(ChatColor.YELLOW + "Lava will rise after 2 seconds!");
        }, 20L * 8L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // 1 saniye kaldı
            bbs.setProgress(0.1);
            bbs.setTitle(ChatColor.YELLOW + "Lava will rise after 1 second!");
        }, 20L * 9L);

        getScheduler().scheduleSyncDelayedTask(instance, () -> { // Lav Yükseliyor!
            bbs.setProgress(1);
            bbs.setTitle(ChatColor.DARK_RED + "Lava Rising!");
            bbs.setColor(BarColor.RED);

            LavaUtils lavaUtils = instance.getLavaUtils();

            World world = lavaUtils.bottomRight.getWorld();
            Location edgeMin = lavaUtils.bottomRight;
            Location edgeMax = lavaUtils.topLeft;

            if (edgeMin.getBlockY() >= 320) {
                IS_RISING = false;
                setTakeDamage(false);
                bbs.setColor(BarColor.RED);
                bbs.setTitle(ChatColor.DARK_RED + "Lava Maximum Level!");
                bbs.setProgress(1);
            } else {
                for (int x = edgeMin.getBlockX(); x <= edgeMax.getBlockX(); x++) {
                    for (int y = edgeMin.getBlockY(); y <= edgeMax.getBlockY(); y++) {
                        for (int z = edgeMin.getBlockZ(); z <= edgeMax.getBlockZ(); z++) {
                            Block block = new Location(world, x, y, z).getBlock();

                            if (block.getType() == Material.AIR) {
                                block.setType(instance.getBlock());
                            }
                        }
                    }
                }
                lavaUtils.IncreaseCurrentLevel();
                lavaTimer();

                bbs.setColor(BarColor.YELLOW);
            }
        }, 20L * 10L);
    }
}