package tr.elite.eliterising.Events;

import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Iterator;

import static tr.elite.eliterising.Commands.CommandStart.*;
import static tr.elite.eliterising.EliteRising.*;
import static tr.elite.eliterising.Teams.*;
import static tr.elite.eliterising.Events.PlayerQuit.*;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (IS_STARTED) {
            if (bbs.getPlayers().contains(player)) {
                bbs.removePlayer(player);
            }
            bbs.addPlayer(player);
            bbs.setVisible(true);

            if (oldTeams.containsKey(player.getUniqueId()) || keyContains(player.getUniqueId(),oldTeams)) {
                String team = oldTeams.get(player.getUniqueId());
                addPlayer(player,team);
                oldTeams.remove(player.getUniqueId(),team);
            }
        } else {
            player.setGameMode(GameMode.ADVENTURE);

            Iterator<Advancement> iterator = Bukkit.getServer().advancementIterator();
            while (iterator.hasNext()) {
                AdvancementProgress progress = e.getPlayer().getAdvancementProgress(iterator.next());
                for (String criteria : progress.getAwardedCriteria()) {
                    progress.revokeCriteria(criteria);
                }
            }

            player.sendMessage(ChatColor.AQUA + "Welcome to" + ChatColor.GOLD + " EliteRising");
            player.sendMessage(ChatColor.AQUA + "In order to start the game: " + ChatColor.GOLD + "/start <Start Mode> <Team Mode>");
            player.sendMessage(ChatColor.AQUA + "For Team commands: " + ChatColor.GOLD + "/elteam <create|leave|invite|message|list>");
            player.sendMessage(ChatColor.AQUA + "For customizable start modes: " + ChatColor.GOLD + "/mode <create|edit|list|info|delete> <items... (if only first argument is create or edit)>");
            player.sendMessage(ChatColor.AQUA + "You want to message general, you can use " + ChatColor.GOLD + "!" + ChatColor.AQUA + " to message's start. But you don't have to use any prefix for team");
            player.sendMessage(ChatColor.AQUA + "Developed by " + ChatColor.GOLD + "Sadece Emir.");
        }
        e.setJoinMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " joined!");
    }
}