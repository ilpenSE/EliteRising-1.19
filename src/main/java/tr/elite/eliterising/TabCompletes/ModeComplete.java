package tr.elite.eliterising.TabCompletes;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

import static tr.elite.eliterising.EliteRising.*;

public class ModeComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("create");
            completions.add("edit");
            completions.add("delete");
            completions.add("list");
            completions.add("info");
        } else if (args.length == 2) {
            if (equalsOneMore(args[0],"edit","delete")) {
                completions = getStartModesName();
            }
        } else if (args.length > 2) {
            if (equalsOneMore(args[0],"create","edit")) {
                ArrayList<Material> illegalItems = new ArrayList<>();
                for (Material item : Material.values()) {
                    String itemName = item.name().toLowerCase(Locale.ENGLISH);
                    if (containsOneMore(itemName,"wall_","legacy","fire","water","lava","spawn","air","farm","command","bedrock")) {
                        illegalItems.add(item);
                    }
                }
                if (Objects.equals(args[args.length - 1], "")) {
                    for (Material item : Material.values()) {
                        if (!equalsOneMore(item, illegalItems)) {
                            completions.add("minecraft:" + item.name().toLowerCase(Locale.ENGLISH));
                        }
                    }
                }
            }
        }

        return completions;
    }
}
