package tr.elite.eliterising.TabCompletes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ConfigComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("language");
            completions.add("grace_period");
            completions.add("invite_expire");
        } else if (args.length == 2) {
            completions.add("get");
            completions.add("set");
        } else if (args.length > 2) {
            completions.add("");
        }
        return completions;
    }
}
