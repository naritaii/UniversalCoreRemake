package me.stupidbot.universalcoreremake.Managers;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.xml.bind.Marshaller;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class StaminaManager implements Listener {

    private Map<UUID, Integer> staminas = new HashMap<>();


    public int getStamina(Player p){
        return staminas.get(p.getUniqueId());
    }

    public void setStamina(Player p, int i){
        staminas.put(p.getUniqueId(), i);
    }

    public void initialize(){
//        Bukkit.getScheduler().runTaskTimerAsynchronously(UniversalCoreRemake.getInstance(),  () -> { }, 0, (20 * 60) * 10);
//        staminas.keySet().stream().filter(id -> staminas.get(id) < getMaxStamina(Bukkit.getPlayer(id))).collect(Collectors.toList()) .forEach(staminas.put(id, staminas.get(id) + 1));
    }

    public int getMaxStamina(Player p){
        return 100;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        staminas.put(e.getPlayer().getUniqueId(), 100);
    }

    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void PlayerInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();

        if(e.getAction() == Action.RIGHT_CLICK_AIR){
            p.sendMessage("Stamina: " + getStamina(p));
        }else if(e.getAction() == Action.LEFT_CLICK_AIR){
            setStamina(p, new Random().nextInt(getMaxStamina(p)));
        }

    }

}
