package tr.elite.eliterising.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.*;
import static tr.elite.eliterising.EliteRising.*;
import static tr.elite.eliterising.Teams.*;
import static tr.elite.eliterising.Invite.*;

public class CommandDavet implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            log(LANGUAGE.getValue("eliterising.main.notReal"));
            return true;
        }
        Player player = (Player) sender;

        if (IS_RISING) {
            sendError(LANGUAGE.getValue("eliterising.cmd.invite.gameStart"),player);
            return true;
        }

        String executor = args[0];
        String code = args[1];
        if (!existInvite(code)) {
            sendError(LANGUAGE.getValue("eliterising.cmd.invite.expired"),player);
            return true;
        }
        if (executor.equalsIgnoreCase("accept")) {
            Player inviter = getInviter(code);
            if (inviter == null) {
                sendError(LANGUAGE.getValue("eliterising.cmd.invite.invalid"),player);
                return true;
            }
            String join_team = getTeam(inviter);
            if (getTeam(player) != null) {
                dispatchCommand(getConsoleSender(),"tellraw " + player.getName() + " " +
                        "{\"text\":\"[\",\"color\":\"gold\",\"extra\":[{\"text\":\"EliteRising\",\"color\":\"dark_red\"},{\"text\":\"] \",\"color\":\"gold\"},{\"text\":\""+LANGUAGE.getValue("eliterising.cmd.invite.JSON.generic").replace("$",join_team)+"\",\"color\":\"yellow\"},{\"text\":\""+LANGUAGE.getValue("eliterising.cmd.invite.JSON.yes")+"\",\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+LANGUAGE.getValue("eliterising.cmd.invite.JSON.yesHover")+"\"},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/eldavet reAccept "+code+"\"}},{\"text\":\" \",\"color\":\"white\"},{\"text\":\""+LANGUAGE.getValue("eliterising.cmd.invite.JSON.no")+"\",\"color\":\"red\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+LANGUAGE.getValue("eliterising.cmd.invite.JSON.noHover")+"\"},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/eldavet reject "+code+"\"}}]}");
                return true;
            }
            addPlayer(player,join_team);
            sendMessage(ChatColor.GREEN + LANGUAGE.getValue("eliterising.cmd.invite.welcomeTeam"),player);
            sendMessage(ChatColor.GREEN + LANGUAGE.getValue("eliterising.cmd.invite.accepted1"),inviter);
        } else if (executor.equalsIgnoreCase("reject")) {
            Player inviter = getInviter(code);
            if (inviter == null) {
                sendError(LANGUAGE.getValue("eliterising.cmd.invite.invalid"),player);
                return true;
            }
            sendMessage(ChatColor.RED + LANGUAGE.getValue("eliterising.cmd.invite.rejectedTeam"),player);
            sendMessage(ChatColor.RED + LANGUAGE.getValue("eliterising.cmd.invite.rejected1").replace("$",player.getName()),inviter);
            expire(code);
        } else if (executor.equalsIgnoreCase("reAccept")) {
            Player inviter = getInviter(code);
            if (inviter == null) {
                sendError(LANGUAGE.getValue("eliterising.cmd.invite.invalid"),player);
                return true;
            }
            removePlayer(player,getTeam(player));
            addPlayer(player,getTeam(inviter));
            sendMessage(ChatColor.GREEN + LANGUAGE.getValue("eliterising.cmd.invite.reAccept.suc1"),player);
            sendMessage(ChatColor.GREEN + LANGUAGE.getValue("eliterising.cmd.invite.reAccept.suc2").replace("$",player.getName()),inviter);
        }
        expire(code);
        return true;
    }
}
