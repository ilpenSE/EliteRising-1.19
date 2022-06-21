package tr.elite.eliterising;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;

public class Language {
    private final HashMap<String,String> ENGLISH = new HashMap<>();
    public Language() {
        ENGLISH.put("RED","Red");
        ENGLISH.put("BLUE","Blue");
        ENGLISH.put("GREEN","Green");
        ENGLISH.put("YELLOW","Yellow");
        ENGLISH.put("AQUA","Aqua");
        ENGLISH.put("GOLD","Gold");
        ENGLISH.put("DARK_PURPLE","Purple");
        ENGLISH.put("LIGHT_PURPLE","Light Purple");
        ENGLISH.put("GRAY","Gray");
        ENGLISH.put("WHITE","White");
        ENGLISH.put("BLACK","Black");
        ENGLISH.put("DARK_RED","Dark Red");
        ENGLISH.put("DARK_BLUE","Dark Blue");
        ENGLISH.put("DARK_GREEN","Dark Green");
        ENGLISH.put("DARK_AQUA","Dark Aqua");
        ENGLISH.put("DARK_GRAY","Dark Gray");
    }

    public String getEN(String query) {
        return ENGLISH.get(query);
    }

    public ChatColor toEN(String value) {
        ArrayList<String> keys = new ArrayList<>(ENGLISH.keySet());
        ArrayList<String> vals = new ArrayList<>(ENGLISH.values());
        int i = vals.indexOf(value);
        return ChatColor.valueOf(keys.get(i));
    }
}
