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


            sendMessage((colorise(LANGUAGE.getValue("eliterising.events.join.info1"))),player);
            sendMessage((colorise(LANGUAGE.getValue("eliterising.events.join.info2"))),player);
            sendMessage((colorise(LANGUAGE.getValue("eliterising.events.join.info3"))),player);
            sendMessage((colorise(LANGUAGE.getValue("eliterising.events.join.info4"))),player);
            sendMessage((colorise(LANGUAGE.getValue("eliterising.events.join.info5"))),player);
            sendMessage((colorise(LANGUAGE.getValue("eliterising.events.join.info6"))),player);
        }
        e.setJoinMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " " + LANGUAGE.getValue("eliterising.events.join.generic") + "!");
    }

    private String colorise(String s) {
        return (s.replaceAll("%",ChatColor.AQUA + "").replaceAll("&",ChatColor.GOLD + ""));
    }
}