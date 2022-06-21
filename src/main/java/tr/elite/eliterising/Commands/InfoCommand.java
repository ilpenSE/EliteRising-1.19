package tr.elite.eliterising.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tr.elite.eliterising.EliteRising;

import static org.bukkit.Bukkit.getLogger;

public class InfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String txt =
                "$Welcome to EliteRising. Commands are: %/start $, %/elteam $, %/mode$. You can start the game using the command called %/start$. You can create or organize (at only normal team mode) the teams using %/elteam$. And; after update that v1.2, you can edit ONLY start modes. What can you do: %Create a new start mode $, %Edit all CUSTOM start modes. (You CANNOT operate main 5 modes) $, %Delete or list custom start modes (Again you can't remove main modes)";
        if (!(sender instanceof Player)) {
            getLogger().info(txt.replaceAll("\\$","").replaceAll("%",""));
            return true;
        }
        Player player = (Player) sender;

        EliteRising.sendMessage(
                txt.replaceAll("\\$",ChatColor.AQUA + "").replaceAll("%",ChatColor.GOLD + "")
                ,player
        );
        return true;
    }
}
