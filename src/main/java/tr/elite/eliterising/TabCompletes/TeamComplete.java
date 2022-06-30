package tr.elite.eliterising.TabCompletes;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import tr.elite.eliterising.EliteRising;

import java.util.ArrayList;
import java.util.List;

public class TeamComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("create");
            completions.add("leave");
            completions.add("invite");
            completions.add("message");
            completions.add("msg");
            completions.add("list");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("invite")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
            } else if (EliteRising.equalsOneMore(args[0],"message","msg")) {
                completions.add("");
            }
        }

        return completions;
    }
}
