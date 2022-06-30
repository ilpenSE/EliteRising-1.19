package tr.elite.eliterising.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tr.elite.eliterising.EliteRising;
import tr.elite.eliterising.TeamModes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static org.bukkit.Bukkit.*;
import static tr.elite.eliterising.EliteRising.*;
import static tr.elite.eliterising.Teams.*;
import static tr.elite.eliterising.Commands.CommandStart.*;

public class CommandTeam implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            log(LANGUAGE.getValue("eliterising.main.notReal"));
            return true;
        }
        Player player = (Player) sender;
        ChatColor teamColor = getColors(true).get(0);
        String teamName = COLORS.getValue(teamColor);

        if (!(IS_STARTED)) {
            sendError(LANGUAGE.getValue("eliterising.cmd.team.notStarted"),player);
            return true;
        }

        if (TEAM_MODE != TeamModes.NORMAL) {
            sendError(LANGUAGE.getValue("eliterising.cmd.team.nonNormal"),player);
            return true;
        }

        if (args.length < 1) {
            sendError(LANGUAGE.getValue("eliterising.cmd.team.nonOp"),player);
            return true;
        }

        String operator = args[0];

        if (!equalsOneMore(operator,"list","create","leave","invite","message","msg")) {
            sendError(LANGUAGE.getValue("eliterising.cmd.team.invalidOp"),player);
            return true;
        }

        if (operator.equalsIgnoreCase("create")) {
            if (IS_RISING) {
                sendError(LANGUAGE.getValue("eliterising.cmd.team.createRising"),player);
                return true;
            }
            if (hasTeam(player)) {
                sendError(LANGUAGE.getValue("eliterising.cmd.team.createHaveTeam"),player);
                return true;
            }
            addPlayer(player,teamName);
            teamColors.remove(COLORS.reverseValue(teamName));
            EliteRising.sendMessage(ChatColor.GREEN + LANGUAGE.getValue("eliterising.cmd.team.createSuc"),player);
        } else if (operator.equalsIgnoreCase("leave")) {
            if (IS_RISING) {
                sendError(LANGUAGE.getValue("eliterising.cmd.team.leaveRising"),player);
                return true;
            }
            if (!hasTeam(player)) {
                sendError(LANGUAGE.getValue("eliterising.cmd.team.leaveNoTeam"),player);
                return true;
            }
            removePlayer(player,getTeam(player));
            EliteRising.sendMessage(ChatColor.RED + LANGUAGE.getValue("eliterising.cmd.team.leaveSuc"),player);
        } else if (operator.equalsIgnoreCase("invite")) {
            if (IS_RISING) {
                sendError(LANGUAGE.getValue("eliterising.cmd.team.inviteRising"),player);
                return true;
            }
            if (!hasTeam(player)) {
                sendError(LANGUAGE.getValue("eliterising.cmd.team.inviteNoTeam"),player);
                return true;
            }
            Player toInvitePlayer = Bukkit.getPlayer(args[1]);
            if (toInvitePlayer == null) {
                sendError(LANGUAGE.getValue("eliterising.cmd.team.invite404Player"),player);
                return true;
            }
            dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + toInvitePlayer.getName() + " {\"text\":\"[\",\"color\":\"gold\",\"extra\":[{\"text\":\"EliteRising\",\"color\":\"dark_red\"},{\"text\":\"]\",\"color\":\"gold\"},{\"text\":\" "+player.getName()+" \",\"color\":\"red\"},{\"text\":\""+LANGUAGE.getValue("eliterising.cmd.team.invite.JSON.generic")+"\",\"color\":\"yellow\"},{\"text\":\"\\n\"},{\"text\":\""+LANGUAGE.getValue("eliterising.cmd.team.invite.JSON.accept")+"\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/davet kabul "+ player.getName() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+LANGUAGE.getValue("eliterising.cmd.team.invite.JSON.acceptHover")+"\"}},{\"text\":\" \"},{\"text\":\""+LANGUAGE.getValue("eliterising.cmd.team.invite.JSON.reject")+"\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/davet ret "+ player.getName() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""+LANGUAGE.getValue("eliterising.cmd.team.invite.JSON.rejectHover")+"\"}}]}");
            sendMessage(LANGUAGE.getValue("eliterising.cmd.team.inviteSuc").replace("$",toInvitePlayer.getName()),player);
        } else if (operator.equalsIgnoreCase("msg") || operator.equalsIgnoreCase("message")) {
            if (!hasTeam(player)) {
                sendError(LANGUAGE.getValue("eliterising.cmd.team.msg.noTeam"),player);
                return true;
            }
            String msg = "";
            ArrayList<String> strings = new ArrayList<>(Arrays.asList(args));
            for (String s : strings) {
                if (!(strings.get(0).equals(s))) {
                    msg += s + " ";
                }
            }
            String modified_msg = ChatColor.GOLD + "[" + getTeamColor(getTeam(player)) + LANGUAGE.getValue("eliterising.cmd.team.msg.team") + ChatColor.GOLD + "] " + ChatColor.RED + player.getName() + ChatColor.YELLOW + ": " + ChatColor.AQUA + capitalize(msg);
            for (Player p : getTeamPlayers(getTeam(player))) {
                p.sendMessage(modified_msg);
            }
        } else if (operator.equalsIgnoreCase("list")) {
            ArrayList<String> teams = new ArrayList<>(getTeams());
            if (teams.size() == 0) {
                sendMessage(LANGUAGE.getValue("eliterising.cmd.list.allTeams")+": " + LANGUAGE.getValue("eliterising.cmd.list.noTeams") + ChatColor.RESET +
                        "\n" + LANGUAGE.getValue("eliterising.cmd.list.urTeam") + ": " + ((!hasTeam(player)) ? LANGUAGE.getValue("eliterising.cmd.list.no") : getTeamColor(getTeam(player)) + getTeam(player)),player);
                return true;
            }
            ArrayList<String> allTeams = new ArrayList<>();
            for (String team : teams) {
                allTeams.add(getTeamColor(team) + team);
            }
            String list = allTeams.toString().substring(1).replaceAll("]","");
            sendMessage(LANGUAGE.getValue("eliterising.cmd.list.allTeams") + ": " + list + ChatColor.RESET +
                    "\n" + LANGUAGE.getValue("eliterising.cmd.list.urTeam") + ": " + ((!hasTeam(player)) ? LANGUAGE.getValue("eliterising.cmd.list.no") : getTeamColor(getTeam(player)) + getTeam(player)),player);
        }
        return true;
    }
}
