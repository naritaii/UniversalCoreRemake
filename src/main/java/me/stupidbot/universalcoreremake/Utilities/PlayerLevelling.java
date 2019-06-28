package me.stupidbot.universalcoreremake.Utilities;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.util.DynamicLocation;
import me.stupidbot.universalcoreremake.Effects.LevelUp;
import me.stupidbot.universalcoreremake.Managers.UniversalPlayers.UniversalPlayer;
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
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);

        int currentXp = up.setDataXp(up.getDataXp() + amount);
        up.setDataTotalXp(up.getDataTotalXp() + amount);

        if (currentXp >= xpToNextLevel(up.getDataLevel()))
            levelUp(p);
        else
            updateUI(p);
    }

    private static void levelUp(Player p) {
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
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
        Effect lvlAnim = new LevelUp(UniversalCoreRemake.getEffectManager(), p, lvl);
        lvlAnim.setDynamicOrigin(new DynamicLocation(p));
        lvlAnim.start();
        TextUtils.sendSubtitle(p, "&a" + TextUtils.toRoman(oldLvl) + " &e\u27A2 &a" + TextUtils.toRoman(lvl),
                5, 80, 0);

        p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1.25f);
        sendLevelUpMessages(p, oldLvl, lvl);


        up.setDataXp(xp);
        up.setDataLevel(lvl);
        updateUI(p);
    }

    private static void sendLevelUpMessages(Player p, int oldLvl, int lvl) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8&l&m---------------------------------------------"));
        TextUtils.sendCenteredMessage(p, "&a&k3&6 LEVEL UP! &a&k3");
        p.sendMessage("");
        TextUtils.sendCenteredMessage(p, "&7You are now level &a" + levelTag(lvl));

        for (int i = oldLvl; i <= lvl; i++)
            if (i == 1) {
                p.sendMessage("");
                TextUtils.sendCenteredMessage(p,
                        "&7Level up to unlock features and raise stats like stamina!");
            } else if (i == 5) {
                p.sendMessage("");
                TextUtils.sendCenteredMessage(p, "&7Hey, you're getting pretty good at this. I think");
                TextUtils.sendCenteredMessage(p, "&aQuest Master &7might have some &ejobs for you&7,");
                TextUtils.sendCenteredMessage(p, "&7type &c/spawn&7 to warp to spawn!");
            }

        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8&l&m---------------------------------------------"));
    }

    private static void updateUI(Player p) {
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        int lvl = up.getDataLevel();

        p.setLevel(lvl);
        p.setExp((up.getDataXp() * 1f) / xpToNextLevel(lvl));
    }

    public static int xpToNextLevel(int lvl) {
        return (int) ((Math.pow(++lvl, 2) * 3));
    }

    public static String levelTag(int lvl) {
        String s;
        if (lvl < 5)
            s = "&7[" + lvl + "\u2605]";
        else if (lvl < 10)
            s = "&6[" + lvl + "\u2605]";
        else if (lvl < 25)
            s = "&6[" + lvl + "\u2B50]";
        else
            s = "&f[" + lvl + "\u269D]";
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent e) {
        e.setKeepLevel(true);
    }
}