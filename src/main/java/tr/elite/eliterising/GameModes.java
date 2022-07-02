package tr.elite.eliterising;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * <h1>Game Modes</h1>
 * <p>
 *     This class contains everything about customizable or not customizable game modes. DEFINITELY Don't change this file. <br></br>
 *     Methods: {@link #getModeByName(String)}, {@link #getItems()}, {@link #parseItems(ArrayList)}
 * </p>
 * FKA (Formerly Known As): Start Modes <br></br>
 * @author EliteDev, SadeceEmir
 * @since EL-1.2 <br></br>
 * @see tr.elite.eliterising.Commands.CommandMode
 */
public enum GameModes {
    NORMAL (new ArrayList<>(Arrays.asList("minecraft:apple/4"))),

    ELYTRA (new ArrayList<>(Arrays.asList("minecraft:elytra","minecraft:firework_rocket/5"))),

    OVERPOWERED (new ArrayList<>(Arrays.asList("minecraft:diamond_helmet{Enchantments:[{\"id\":\"protection\",\"level\":\"4\"}]}","minecraft:diamond_chestplate{Enchantments:[{\"id\":\"protection\",\"level\":\"4\"}]}","minecraft:diamond_leggings{Enchantments:[{\"id\":\"protection\",\"level\":\"4\"}]}","minecraft:diamond_boots{Enchantments:[{\"id\":\"protection\",\"level\":\"4\"}]}","minecraft:enchanted_golden_apple/16"))),

    ARCHERY (new ArrayList<>(Arrays.asList("minecraft:bow{Enchantments:[{\"id\":\"punch\",\"level\":\"1\"},{\"id\":\"power\",\"level\":\"3\"}]}","minecraft:arrow/10"))),

    BUILD (new ArrayList<>(Arrays.asList("minecraft:cobblestone/64","minecraft:cobblestone/64")));

    ArrayList<String> items;
    GameModes(ArrayList<String> items) {
        this.items = items;
    }

    public ArrayList<ItemStack> getItems() {
        return parseItems(items);
    }

    public static ArrayList<ItemStack> parseItems(ArrayList<String> items) {
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

    public static GameModes getModeByName(String name) {
        return GameModes.valueOf(name.toUpperCase(Locale.ENGLISH));
    }
}
