package me.stupidbot.universalcoreremake.utilities;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.util.DynamicLocation;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.effects.LevelUp;
import me.stupidbot.universalcoreremake.events.LevelUpEvent;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerLevelling implements Listener {
    public static void giveXp(Player p, int amount) {
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);

        int currentXp = up.setXp(up.getXp() + amount);
        up.setTotalXp(up.getTotalXp() + amount);

        if (currentXp >= xpToNextLevel(up.getLevel()))
            levelUp(p);
        else
            updateUI(p);
    }

    private static void levelUp(Player p) {
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        int xp = up.getXp();
        int lvl = up.getLevel();
        int oldLvl = lvl;

        while (true) {
            int xpNeeded = xpToNextLevel(lvl);
            if (xp >= xpNeeded) {
                xp -= xpNeeded;
                lvl++;
            } else
                break;
        }
        Bukkit.getServer().getPluginManager().callEvent(new LevelUpEvent(p, lvl, xp));

        Effect lvlAnim = new LevelUp(UniversalCoreRemake.getEffectManager(), p, lvl);
        lvlAnim.setDynamicOrigin(new DynamicLocation(p));
        lvlAnim.start();
        TextUtils.sendSubtitle(p, "&a" + TextUtils.toRoman(oldLvl) + " &e\u27A2 &a" + TextUtils.toRoman(lvl),
                5, 80, 0);

        p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1.25f);
        levelMilestones(p, oldLvl, lvl);


        up.setXp(xp);
        up.setLevel(lvl);
        updateUI(p);
    }

    private static final Map<Integer, StringReward> levelRewards = new HashMap<Integer, StringReward>() {
        {
            int i = 1;
            put(++i, new StringReward("MONEY 5")); // 2
            put(++i, new StringReward("MONEY 10"));
            put(++i, new StringReward("MONEY 15"));
            put(++i, new StringReward("MONEY 20")); // 5
            put(++i, new StringReward("MONEY 50"));
            put(++i, new StringReward("MONEY 55"));
            put(++i, new StringReward("MONEY 60"));
            put(++i, new StringReward("MONEY 65"));
            put(++i, new StringReward("MONEY 100")); // 10
            put(++i, new StringReward("MONEY 110"));
            put(++i, new StringReward("MONEY 120"));
            put(++i, new StringReward("MONEY 130"));
            ++i;  // skip 14
            put(++i, new StringReward("MONEY 150", "MONEY 150")); // 15
        }
    };

    private static void levelMilestones(Player p, int oldLvl, int lvl) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8&l&m---------------------------------------------"));
        TextUtils.sendCenteredMessage(p, "&a&k3&6 LEVEL UP! &a&k3");
        p.sendMessage("");
        TextUtils.sendCenteredMessage(p, "&7You are now level &a" + levelTag(lvl));

        for (int i = ++oldLvl; i <= lvl; i++) {
            if (i == 2) {
                p.sendMessage("");
                TextUtils.sendCenteredMessage(p,
                        "&7Level up to unlock features and raise stats like stamina!");
            } else if (i == 5) {
                p.sendMessage("");
                TextUtils.sendCenteredMessage(p, "&7Hey, you're getting pretty good at this. I think");
                TextUtils.sendCenteredMessage(p, "&aQuest Master &7might have some &ejobs for you&7,");
                TextUtils.sendCenteredMessage(p, "&7type &c/spawn&7 to warp to spawn!");
            } else if (i == 14) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "\n  &aReward:\n    " +
                                "&8+&c&oi don't really feel like giving you a reward right now...\n    " +
                                "&8+&c&othings have just been... tough... at home and...\n    " +
                                "&8+&c&oplease don't hate me...\n    " +
                                "&8+&c&o...\n    " +
                                "&8+&c&oi-i promise i'll make it up to you!"));
            }

            if (levelRewards.containsKey(i)) {
                StringReward reward = levelRewards.get(i);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "\n  &aReward:"));
                for (String r : reward.asStrings())
                    if (r != null)
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "    &8+" + r));
                reward.giveNoMessage(p);
            }
        }

        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8&l&m---------------------------------------------"));
    }

    private static void updateUI(Player p) {
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        int lvl = up.getLevel();

        p.setLevel(lvl);
        p.setExp((up.getXp() * 1f) / xpToNextLevel(lvl));
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);

        if (up.firstJoin())
            up.setLevel(1);

        PlayerLevelling.updateUI(p);
    }

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent e) {
        e.setKeepLevel(true);
        e.setKeepInventory(true);

        Player p = e.getEntity();
        UniversalCoreRemake plugin = UniversalCoreRemake.getInstance();
        UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).incrementDeaths(1);

        Economy ecc = UniversalCoreRemake.getEconomy();
        double money = ecc.getBalance(p) / 2d;
        ecc.withdrawPlayer(p, money);

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (p.isDead())
                ((CraftPlayer) p).getHandle().playerConnection.a(
                        new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));

            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou lost &6$" + TextUtils.addCommas(money) + "&a for dying."));
        });
    }

    @EventHandler
    public void OnDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer != null)
            UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(killer).incrementKills(1);
    }
}