package tr.elite.eliterising.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tr.elite.eliterising.EliteRising;

import static org.bukkit.Bukkit.*;
import static tr.elite.eliterising.EliteRising.*;
import static tr.elite.eliterising.Teams.*;

public class CommandInvite implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("Sender must be a real player!");
            return true;
        }
        Player player = (Player) sender;

        if (IS_RISING) {
            sendError("Invite command is just usable before lava rises!",player);
            return true;
        }

        /* ERROR CODES
         * 1 ERROR CODE = INVITER ISN'T ONLINE.
         */
        String executor = args[0];
        if (executor.equalsIgnoreCase("kabul")) {
            // /davet kabul INVITER
            Player inviter = getPlayer(args[1]);
            if (inviter == null) {
                EliteRising.sendError("Invalid Invite. (1)",player);
                return true;
            }
            String join_team = getTeam(inviter);
            if (getTeam(player) != null) {
                dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + player.getName() + " " +
                        "{\"text\":\"[\",\"color\":\"gold\",\"extra\":[{\"text\":\"EliteRising\",\"color\":\"dark_red\"},{\"text\":\"] \",\"color\":\"gold\"},{\"text\":\"While joining to team called \",\"color\":\"yellow\"},{\"text\":\""+join_team+"\",\"color\":\"gold\"},{\"text\":\", you will leave your current team. Do you accept?\",\"color\":\"yellow\"},{\"text\":\"[YES]\",\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"I accept\"},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/davet reKabul "+player.getName()+" "+inviter.getName()+"\"}},{\"text\":\" \",\"color\":\"white\"},{\"text\":\"[NOPE]\",\"color\":\"red\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"I do not accept\"},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/davet ret "+inviter.getName()+"\"}}]}");
                return true;
            }
            addPlayer(player,join_team);
            EliteRising.sendMessage(ChatColor.GREEN + "Welcome to your new team!",player);
            EliteRising.sendMessage(ChatColor.GREEN + "Your team invite has accepted!",inviter);
        } else if (executor.equalsIgnoreCase("ret")) {
            Player inviter = Bukkit.getPlayer(args[1]);
            if (inviter == null) {
                EliteRising.sendError("Invalid Invite. (1)",player);
                return true;
            }
            EliteRising.sendMessage(ChatColor.RED + "Rejected invite.",player);
            EliteRising.sendMessage(ChatColor.RED + "Your team invite has rejected!",inviter);
        } else if (executor.equalsIgnoreCase("reKabul")) {
            // /davet reKabul INVITED INVITER
            Player inviter = Bukkit.getPlayer(args[2]);
            if (inviter == null) {
                EliteRising.sendError("Invalid Invite. (1)",player);
                return true;
            }
            removePlayer(player,getTeam(player));
            addPlayer(player,getTeam(inviter));
            EliteRising.sendMessage(ChatColor.GREEN + "You has accepted the invite!",player);
            EliteRising.sendMessage(ChatColor.GREEN + "Your team invite has accepted!",inviter);
        }
        return true;
    }
}
