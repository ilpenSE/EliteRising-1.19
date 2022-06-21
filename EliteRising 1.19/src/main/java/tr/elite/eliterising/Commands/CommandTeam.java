package tr.elite.eliterising.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tr.elite.eliterising.EliteRising;
import tr.elite.eliterising.Language;
import tr.elite.eliterising.TeamModes;

import java.util.ArrayList;
import java.util.Arrays;

import static org.bukkit.Bukkit.*;
import static tr.elite.eliterising.EliteRising.*;
import static tr.elite.eliterising.Teams.*;
import static tr.elite.eliterising.Commands.CommandStart.*;

public class CommandTeam implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            getLogger().info("[EliteRising] Command sender must be a real player!");
            return true;
        }
        Player player = (Player) sender;
        ChatColor teamColor = getColors(true).get(0);
        Language lan = new Language();
        String teamName = lan.getEN(teamColor.name());

        if (!(IS_STARTED)) {
            sendError("The game must be started for the team command to work!",player);
            return true;
        }

        if (TEAM_MODE != TeamModes.NORMAL) {
            sendError("Commands related by team just work on normal team mode!",player);
            return true;
        }

        if (args.length < 1) {
            sendError("Please select an operation.",player);
            return true;
        }

        String operator = args[0];

        if (!equalsOneMore(operator,"list","create","leave","invite","message","msg")) {
            sendError("Please select a valid operation.",player);
            return true;
        }

        if (operator.equalsIgnoreCase("create")) {
            if (IS_RISING) {
                sendError("The team create command is just usable before lava rises!",player);
                return true;
            }
            if (hasTeam(player)) {
                sendError("You have already a team!",player);
                return true;
            }
            addPlayer(player,teamName);
            teamColors.remove(lan.toEN(teamName));
            EliteRising.sendMessage(ChatColor.GREEN + "Team successfully created!",player);
        } else if (operator.equalsIgnoreCase("leave")) {
            if (IS_RISING) {
                sendError("The team leave command is just usable before lava rises!",player);
                return true;
            }
            if (!hasTeam(player)) {
                sendError("You must have a team to leave!",player);
                return true;
            }
            removePlayer(player,getTeam(player));
            EliteRising.sendMessage(ChatColor.RED + "Successfully left!",player);
        } else if (operator.equalsIgnoreCase("invite")) {
            if (IS_RISING) {
                sendError("The team invite command is just usable before lava rises!",player);
                return true;
            }
            if (!hasTeam(player)) {
                sendError("You have no team!",player);
                return true;
            }
            Player toInvitePlayer = Bukkit.getPlayer(args[1]);
            if (toInvitePlayer == null) {
                sendError("Player was not found.",player);
                return true;
            }
            dispatchCommand(Bukkit.getConsoleSender(),"tellraw " + toInvitePlayer.getName() + " {\"text\":\"[\",\"color\":\"gold\",\"extra\":[{\"text\":\"EliteRising\",\"color\":\"dark_red\"},{\"text\":\"]\",\"color\":\"gold\"},{\"text\":\" "+player.getName()+" \",\"color\":\"red\"},{\"text\":\"sent invite to you.\",\"color\":\"yellow\"},{\"text\":\"\\n\"},{\"text\":\"[ACCEPT]\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/davet kabul "+ player.getName() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Accept the team invite.\"}},{\"text\":\" \"},{\"text\":\"[REJECT]\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/davet ret "+ player.getName() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Reject the team invite.\"}}]}");
            sendMessage("Team invite has successfully sent to " + toInvitePlayer.getName() + "!",player);
        } else if (operator.equalsIgnoreCase("msg") || operator.equalsIgnoreCase("message")) {
            if (!hasTeam(player)) {
                sendError("You have no team to message!",player);
                return true;
            }
            String msg = "";
            ArrayList<String> strings = new ArrayList<>(Arrays.asList(args));
            for (String s : strings) {
                if (!(strings.get(0).equals(s))) {
                    msg += s + " ";
                }
            }
            String modified_msg = ChatColor.GOLD + "[" + getTeamColor(getTeam(player)) + "Team" + ChatColor.GOLD + "] " + ChatColor.RED + player.getName() + ChatColor.YELLOW + ": " + ChatColor.AQUA + capitalize(msg);
            for (Player p : getTeamPlayers(getTeam(player))) {
                p.sendMessage(modified_msg);
            }
        } else if (operator.equalsIgnoreCase("list")) {
            ArrayList<String> teams = new ArrayList<>(getTeams());
            if (teams.size() == 0) {
                sendMessage("All Teams: No Team" + ChatColor.RESET +
                        "\nYour Team: " + ((!hasTeam(player)) ? "No" : getTeamColor(getTeam(player)) + getTeam(player)),player);
                return true;
            }
            ArrayList<String> allTeams = new ArrayList<>();
            for (String team : teams) {
                allTeams.add(getTeamColor(team) + team);
            }
            String list = allTeams.toString().substring(1).replaceAll("]","");
            sendMessage("All Teams: " + list + ChatColor.RESET +
                    "\nYour Team: " + ((!hasTeam(player)) ? "No" : getTeamColor(getTeam(player)) + getTeam(player)),player);
        }
        return true;
    }
}
