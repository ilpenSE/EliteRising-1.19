package tr.elite.eliterising.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffectType;

public class WardenSpawn implements Listener {
    @EventHandler
    public void onWardenSpawns(EntitySpawnEvent e) {
        if (e.getEntity().getName().toLowerCase().contains("warden")) {
            e.setCancelled(true);
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPotionEffect(PotionEffectType.DARKNESS)) {
                p.removePotionEffect(PotionEffectType.DARKNESS);
            }
        }
    }
}
