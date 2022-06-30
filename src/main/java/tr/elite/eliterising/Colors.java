package tr.elite.eliterising;

import org.bukkit.ChatColor;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static tr.elite.eliterising.EliteRising.*;

public class Colors {
    String lan;
    HashMap<String, String> colorsTR = new HashMap<>();
    HashMap<String, String> colorsEN = new HashMap<>();
    Colors(String lan) {
        this.lan = lan;
        colorsEN.put("red","Red");
        colorsEN.put("blue","Blue");
        colorsEN.put("green","Green");
        colorsEN.put("yellow","Yellow");
        colorsEN.put("aqua","Aqua");
        colorsEN.put("gold","Gold");
        colorsEN.put("dark_purple","Purple");
        colorsEN.put("light_purple","Pink");
        colorsEN.put("gray","Gray");
        colorsEN.put("white","White");
        colorsEN.put("black","Black");
        colorsEN.put("dark_red","Dark Red");
        colorsEN.put("dark_blue","Dark Blue");
        colorsEN.put("dark_green","Dark Green");
        colorsEN.put("dark_aqua","Dark Aqua");
        colorsEN.put("dark_gray","Dark Gray");

        colorsTR.put("red","Kırmızı");
        colorsTR.put("blue","Mavi");
        colorsTR.put("green","Yeşil");
        colorsTR.put("yellow","Sarı");
        colorsTR.put("aqua","Turkuaz");
        colorsTR.put("gold","Altın");
        colorsTR.put("dark_purple","Mor");
        colorsTR.put("light_purple","Pembe");
        colorsTR.put("gray","Gri");
        colorsTR.put("white","Beyaz");
        colorsTR.put("black","Siyah");
        colorsTR.put("dark_red","Koyu Kırmızı");
        colorsTR.put("dark_blue","Koyu Mavi");
        colorsTR.put("dark_green","Koyu Yeşil");
        colorsTR.put("dark_aqua","Koyu Turkuaz");
        colorsTR.put("dark_gray","Koyu Gri");
    }

    public String getValue(ChatColor color) {
        HashMap<String, String> colors = Objects.equals(this.lan, "tr") ? colorsTR : colorsEN;
        return colors.get(color.name().toLowerCase(Locale.ENGLISH));
    }

    public ChatColor reverseValue(String name) {
        HashMap<String, String> colors = Objects.equals(this.lan, "tr") ? colorsTR : colorsEN;
        ArrayList<String> keys = new ArrayList<>(colors.keySet());
        ArrayList<String> vals = new ArrayList<>(colors.values());
        int i = vals.indexOf(name);
        return keyColor(keys.get(i));
    }
}
