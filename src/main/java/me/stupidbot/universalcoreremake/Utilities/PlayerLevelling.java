package me.stupidbot.universalcoreremake.Utilities;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.util.DynamicLocation;
import me.stupidbot.universalcoreremake.Effects.LevelUp;
import me.stupidbot.universalcoreremake.Managers.UniversalPlayers.UniversalPlayer;
import me.stupidbot.universalcoreremake.Managers.UniversalPlayers.UniversalPlayerManager;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerLevelling implements Listener {
    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PlayerLevelling.updateUI(p);
    }

    public static void giveXp(Player p, int amount) {
        UniversalPlayer up = UniversalPlayerManager.getUniversalPlayer(p);

        int currentXp = up.setDataXp(up.getDataXp() + amount);
        up.setDataTotalXp(up.getDataTotalXp() + amount);

        if (currentXp >= xpToNextLevel(up.getDataLevel()))
            levelUp(p);
        else
            updateUI(p);
    }

    private static void levelUp(Player p) {
        UniversalPlayer up = UniversalPlayerManager.getUniversalPlayer(p);
        int xp = up.getDataXp();
        int lvl = up.getDataLevel();
        int oldLvl = lvl;

        while (true) {
            int xpNeeded = xpToNextLevel(lvl);
            if (xp >= xpNeeded) {
                xp -= xpNeeded;
                lvl++;
            } else
                break;
        }
        Effect lvlAnim = new LevelUp(UniversalCoreRemake.getEffectManager());
        lvlAnim.setDynamicOrigin(new DynamicLocation(p));
        lvlAnim.setTargetPlayer(p);
        lvlAnim.start();
        TextUtils.sendSubtitle(p, "&a" + TextUtils.toRoman(oldLvl) + " &e\u27A2 &a" + TextUtils.toRoman(lvl),
                5, 80, 0);

        p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1.5f);

        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l=-------------------------------------------="));
        TextUtils.sendCenteredMessage(p, "&a&k3&6 LEVEL UP! &a&k3");
        p.sendMessage("");
        TextUtils.sendCenteredMessage(p, "&7You are now level &a" + lvl + "&7!");
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l=-------------------------------------------="));


        up.setDataXp(xp);
        up.setDataLevel(lvl);
        updateUI(p);
    }

    private static void updateUI(Player p) {
        UniversalPlayer up = UniversalPlayerManager.getUniversalPlayer(p);
        int lvl = up.getDataLevel();

        p.setLevel(lvl);
        p.setExp((up.getDataXp() * 1f) / xpToNextLevel(lvl));
    }

    public static int xpToNextLevel(int lvl) {
        return (int) ((Math.pow(++lvl, 2) * 3));
    }

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent e) {
        e.setKeepLevel(true);
    }
}