package tr.elite.eliterising;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static tr.elite.eliterising.EliteRising.*;

/**
 * <h1>Game Modes</h1>
 * <p>
 *     This class contains everything about customizable or not customizable game modes. DEFINITELY Don't change this file. <br></br>
 *     Methods: {@link #getModeByName(String)}, {@link #getItems()}, {@link #parseItems(String)}, {@link #parseMainItems(ArrayList)}
 * </p>
 * FKA (Formerly Known As): Start Modes <br></br>
 * @author EliteDev, SadeceEmir
 * @since EL-1.2 <br></br>
 * @see tr.elite.eliterising.Commands.CommandMode
 */
public enum GameModes {
    STANDART (new ArrayList<>(Arrays.asList("minecraft:apple/4"))),

    ELYTRA (new ArrayList<>(Arrays.asList("minecraft:elytra","minecraft:firework_rocket/5"))),

    OVERPOWERED (new ArrayList<>(Arrays.asList("minecraft:diamond_helmet{Enchantments:[{\"id\":\"protection\",\"level\":\"2\"}]}","minecraft:diamond_chestplate{Enchantments:[{\"id\":\"protection\",\"level\":\"2\"}]}","minecraft:diamond_leggings{Enchantments:[{\"id\":\"protection\",\"level\":\"2\"}]}","minecraft:diamond_boots{Enchantments:[{\"id\":\"protection\",\"level\":\"2\"}]}","minecraft:golden_apple/8"))),

    ARCHERY (new ArrayList<>(Arrays.asList("minecraft:bow{Enchantments:[{\"id\":\"punch\",\"level\":\"1\"},{\"id\":\"power\",\"level\":\"3\"}]}","minecraft:arrow/10"))),

    BUILD (new ArrayList<>(Arrays.asList("minecraft:cobblestone/64","minecraft:cobblestone/64","minecraft:cobblestone/64","minecraft:diamond_axe")));

    ArrayList<String> items;
    GameModes(ArrayList<String> items) {
        this.items = items;
    }

    public ArrayList<ItemStack> getItems() {
        return parseMainItems(items);
    }

    public static ArrayList<ItemStack> parseMainItems(ArrayList<String> items) {
        ArrayList<ItemStack> res = new ArrayList<>();
        for (String item : items) {
            String name = item.contains("/") ? item.split("/")[0] : item; // amountsuz ama nbtli
            int amount = item.contains("/") ? Integer.parseInt(item.split("/")[1]) : 1;
            ItemStack stack;
            if (name.contains("{")) {
                String clearName = name.split("\\{")[0];
                String nbt = name.substring(clearName.length()); // minecraft:item{...} - minecraft:item -> {Enchantments:[{},{}]}
                String enchants = nbt.substring("{Enchantments:[".length()).substring(0,nbt.substring("{Enchantments:[".length()).length() - 2);
                // "id:prot , level:3" , "id:thor,level:2"

                stack = new ItemStack(Material.valueOf(clearName.substring(10).toUpperCase(Locale.ENGLISH)),amount);

                for (int i = 0;i < enchants.split("},").length;i++) { // str = [  "{..." , "{...}"  ]
                    String str = enchants.split("},")[i];
                    String stat = str.replaceAll("\\{","").replaceAll("}","").replaceAll("\"","");
                    Enchantment ench = new EnchantmentWrapper(stat.split(",")[0].split(":")[1]);
                    int lvl = Integer.parseInt(stat.split(",")[1].split(":")[1]);
                    stack.addEnchantment(ench,lvl);
                }
            } else {
                stack = new ItemStack(Material.valueOf(name.substring(10).toUpperCase(Locale.ENGLISH)),amount);
            }
            res.add(stack);
        }
        return res;
    }

    public static ArrayList<ItemStack> parseItems(String name) {
        ArrayList<ItemStack> res = new ArrayList<>();
        if (instance.getConfig().get("custom_modes.gameModes." + name) == null) {
            return null;
        }
        MemorySection ms = (MemorySection) instance.getConfig().get("custom_modes.gameModes." + name);
        assert ms != null;
        Map<String,Object> h = ms.getValues(false);
        if (h.isEmpty()) {
            return null;
        }
        for (String key : h.keySet()) {
            Material material = Material.valueOf(key);
            ItemStack stack = new ItemStack(material);
            MemorySection nbt = (MemorySection) h.get(key);
            for (String k : nbt.getValues(false).keySet()) {
                if (k.equals("amount")) {
                    stack.setAmount(Integer.parseInt((String) nbt.getValues(false).get(k)));
                } else if (k.equals("enchantments")) {
                    MemorySection m = (MemorySection) nbt.getValues(false).get(k);
                    for (String ench : m.getValues(false).keySet()) {
                        Enchantment enchantment = new EnchantmentWrapper(ench);
                        int lvl = Integer.parseInt((String) m.getValues(false).get(ench));
                        stack.addEnchantment(enchantment,lvl);
                    }
                } else if (k.equals("durability")) {
                    int d = Integer.parseInt((String) nbt.getValues(false).get(k));
                    ItemMeta meta = stack.getItemMeta();
                    assert meta != null;
                    Damageable damageable = (Damageable) meta;
                    damageable.setDamage(d);
                    stack.setItemMeta(meta);
                } else if (k.equals("unbreakable")) {
                    boolean b = Boolean.getBoolean((String) nbt.getValues(false).get(k));
                    ItemMeta meta = stack.getItemMeta();
                    assert meta != null;
                    meta.setUnbreakable(b);
                }
            }
            res.add(stack);
        }
        return res;
    }

    public static ArrayList<String> getConfigGameModes() {
        MemorySection ms = (MemorySection) instance.getConfig().get("custom_modes.gameModes");
        assert ms != null;
        return  new ArrayList<>(ms.getValues(false).keySet());
    }

    public static GameModes getModeByName(String name) {
        return GameModes.valueOf(name.toUpperCase(Locale.ENGLISH));
    }
}
