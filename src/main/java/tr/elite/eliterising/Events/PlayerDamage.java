package tr.elite.eliterising.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import tr.elite.eliterising.Commands.CommandStats;
import tr.elite.eliterising.EliteRising;
import tr.elite.eliterising.Teams;

import static org.bukkit.Bukkit.*;
import java.util.HashMap;

import static tr.elite.eliterising.EliteRising.*;
import static tr.elite.eliterising.Commands.CommandStats.*;

public class PlayerDamage implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (IS_STARTED) {
            if ((e.getDamager() instanceof Player) && (e.getEntity() instanceof Player)) {
                Player damager = (Player) e.getDamager();
                Player defender = (Player) e.getEntity();
                String damagerTeam = Teams.getTeam(damager);
                String defenderTeam = Teams.getTeam(defender);
                if (damagerTeam == null || defenderTeam == null) {
                    return;
                }
                if (damagerTeam.equals(defenderTeam)) {
                    e.setCancelled(true);
                    EliteRising.sendError(LANGUAGE.getValue("eliterising.events.damage.hitTeammate"),(Player) e.getDamager());
                } else {
                    assisters.put(damager,defender);
                    getScheduler().scheduleSyncDelayedTask(instance,() -> {
                        if (defender.isDead()) {
                            if (assists.containsKey(damager)) {
                                assists.replace(damager,assists.get(damager) + 1);
                            } else {
                                assists.put(damager,1);
                            }
                        }
                        assisters.remove(damager);
                    },20L * 10L);
                }
            }
        }
        if (!TAKE_DAMAGE) {
            if (e.getEntity() instanceof Player) {
                e.setCancelled(true);
            }
        }
    }

    public static HashMap<Player,Player> assisters = new HashMap<>();
}
