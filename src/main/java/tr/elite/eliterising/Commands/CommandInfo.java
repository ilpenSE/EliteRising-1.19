package tr.elite.eliterising.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tr.elite.eliterising.EliteRising;

import static org.bukkit.Bukkit.getLogger;

public class CommandInfo implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String txt = EliteRising.LANGUAGE.getValue("eliterising.cmd.info.generic");
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
