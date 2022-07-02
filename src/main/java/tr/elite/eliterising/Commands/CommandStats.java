package tr.elite.eliterising.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static org.bukkit.Bukkit.*;
import static tr.elite.eliterising.EliteRising.*;
import static org.bukkit.Statistic.*;

public class CommandStats implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!IS_FINISHED) {
            if (sender instanceof Player) {
                sendError(LANGUAGE.getValue("eliterising.cmd.stats.gameFinish"),(Player) sender);
            } else {
                log(LANGUAGE.getValue("eliterising.cmd.stats.gameFinish"));
            }
            return true;
        }
        for (Player p : getOnlinePlayers()) {
            kills.put(p,p.getStatistic(PLAYER_KILLS));
            takenDamage.put(p,p.getStatistic(DAMAGE_TAKEN));
            dealtDamage.put(p,p.getStatistic(DAMAGE_DEALT));
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            sendMessage(
                    ChatColor.YELLOW + LANGUAGE.getValue("eliterising.cmd.stats.mostKill") + ": " + ChatColor.AQUA + (kills.size() == 0 ? LANGUAGE.getValue("eliterising.cmd.list.no") : reverse(kills,getMaxValue(kills)).getName() + " ("+ getMaxValue(kills) + " " + LANGUAGE.getValue("eliterising.cmd.stats.kills") + ")") + "\n" +
                         ChatColor.YELLOW + LANGUAGE.getValue("eliterising.cmd.stats.mostTD") + ": " + ChatColor.AQUA + (takenDamage.size() == 0 ? LANGUAGE.getValue("eliterising.cmd.list.no") : reverse(takenDamage,getMaxValue(takenDamage)).getName()  + " ("+getMaxValue(takenDamage) + " " + LANGUAGE.getValue("eliterising.cmd.stats.td") + ")") + "\n" +
                         ChatColor.YELLOW +LANGUAGE.getValue("eliterising.cmd.stats.mostDD") +  ": " + ChatColor.AQUA + (dealtDamage.size() == 0 ? LANGUAGE.getValue("eliterising.cmd.list.no") : reverse(dealtDamage,getMaxValue(dealtDamage)).getName() + " ("+getMaxValue(dealtDamage) + " " + LANGUAGE.getValue("eliterising.cmd.stats.dd") + ")") + "\n" +
                         ChatColor.YELLOW + LANGUAGE.getValue("eliterising.cmd.stats.mostAssist") + ": " + ChatColor.AQUA + (assists.size() == 0 ? LANGUAGE.getValue("eliterising.cmd.list.no") : reverse(assists,getMaxValue(assists)).getName() + " (" + getMaxValue(assists) + " " + LANGUAGE.getValue("eliterising.cmd.stats.assists") + ")") + "\n"
            ,player);
        } else {
            log(LANGUAGE.getValue("eliterising.cmd.stats.mostKill") + ": " + (kills.size() == 0 ? LANGUAGE.getValue("eliterising.cmd.list.no") : reverse(kills,getMaxValue(kills)).getName() + " ("+ getMaxValue(kills) + LANGUAGE.getValue("eliterising.cmd.stats.kills") + ")") + "\n" +
                      LANGUAGE.getValue("eliterising.cmd.stats.mostTD") + ": " + (takenDamage.size() == 0 ? LANGUAGE.getValue("eliterising.cmd.list.no") : reverse(takenDamage,getMaxValue(takenDamage)).getName()  + " ("+getMaxValue(takenDamage) +LANGUAGE.getValue("eliterising.cmd.stats.td") + ")") + "\n" +
                      LANGUAGE.getValue("eliterising.cmd.stats.mostDD") +  ": " + (dealtDamage.size() == 0 ? LANGUAGE.getValue("eliterising.cmd.list.no") : reverse(dealtDamage,getMaxValue(dealtDamage)).getName() + " ("+getMaxValue(dealtDamage) + LANGUAGE.getValue("eliterising.cmd.stats.dd") + ")") + "\n" +
                      LANGUAGE.getValue("eliterising.cmd.stats.mostAssist") + ": " + (assists.size() == 0 ? LANGUAGE.getValue("eliterising.cmd.list.no") : reverse(assists,getMaxValue(assists)).getName() + " (" + getMaxValue(assists) + LANGUAGE.getValue("eliterising.cmd.stats.assists") + ")") + "\n"
            );
        }
        return true;
    }

    public static HashMap<Player,Integer> kills = new HashMap<>();
    public static HashMap<Player,Integer> assists = new HashMap<>();
    public static HashMap<Player,Integer> takenDamage = new HashMap<>();
    public static HashMap<Player,Integer> dealtDamage = new HashMap<>();
}
