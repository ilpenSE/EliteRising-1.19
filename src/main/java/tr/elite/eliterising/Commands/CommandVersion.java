package tr.elite.eliterising.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static tr.elite.eliterising.EliteRising.*;

public class CommandVersion implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            log(LANGUAGE.getValue("eliterising.cmd.version.suc") + ": " + VERSION.getFull(true));
            return true;
        }
        Player p = (Player) sender;
        sendMessage(LANGUAGE.getValue("eliterising.cmd.version.suc") + ": " + VERSION.getFull(true));

        return true;
    }
}
