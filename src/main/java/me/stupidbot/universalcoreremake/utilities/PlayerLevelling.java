package me.stupidbot.universalcoreremake.utilities;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.util.DynamicLocation;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.effects.LevelUp;
import me.stupidbot.universalcoreremake.events.UniversalGainXpEvent;
import me.stupidbot.universalcoreremake.events.UniversalLevelUpEvent;
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

public class PlayerLevelling implements Listener {
    public static void giveXp(Player p, int amount) {
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);

        int currentXp = up.setXp(up.getXp() + amount);
        up.setTotalXp(up.getTotalXp() + amount);
        int lvl = up.getLevel();

        if (currentXp >= xpToNextLevel(lvl))
            levelUp(p);
        else
            updateUI(p);

        Bukkit.getServer().getPluginManager().callEvent(new UniversalGainXpEvent(p, lvl, currentXp));
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
                Bukkit.getServer().getPluginManager().callEvent(new UniversalLevelUpEvent(p, lvl, xp));
            } else
                break;
        }

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
                TextUtils.sendCenteredMessage(p, "&7type &c/hub&7 to teleport!");
            }

            if (UniversalCoreRemake.getRewardManager().levelRewards.containsKey(i)) {
                StringReward rewards = UniversalCoreRemake.getRewardManager().levelRewards.get(i);
                if (rewards != null) {
                    String[] asStrings = rewards.asStrings();
                    if (asStrings != null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "\n  &aReward:"));
                        for (String s : asStrings)
                            if (s != null)
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &8+" + s));
                        rewards.giveNoMessage(p);
                    }
                }
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

        if (up.firstJoin()) {
            up.setLevel(1);

            UniversalCoreRemake.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(UniversalCoreRemake.getInstance(), () -> {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC"));

                TextUtils.sendCenteredMessage(p, "&lWelcome to Corrupt Prisons,&7 "
                        + up.getNameColor() + p.getName() + "&f&l!");
                p.sendMessage("");
                TextUtils.sendCenteredMessage(p, "&eTalk to &aQuest Master&e to get started, the Corrupt");
                TextUtils.sendCenteredMessage(p, "&euniverse has many lands to discover, strange creatures");
                TextUtils.sendCenteredMessage(p, "&eto find, secrets to uncover, and peculiar characters to");
                TextUtils.sendCenteredMessage(p, "&emeet! Collect resources, upgrade your gear, explore a");
                TextUtils.sendCenteredMessage(p, "&ewhole new planet, and complete quests to advance your");
                TextUtils.sendCenteredMessage(p, "&eway through Corrupt Prisons!");
                TextUtils.sendCenteredMessage(p, "&bHave fun!");

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC"));
            });
        }

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
        double money = Math.floor((ecc.getBalance(p) / 2d) * 100d) / 100d;
        ecc.withdrawPlayer(p, money);

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (p.isDead())
                ((CraftPlayer) p).getHandle().playerConnection.a(
                        new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
            if (money > 0)
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cYou lost &6$" + TextUtils.addCommas(money) + "&c for dying."));
        });
    }

    @EventHandler
    public void OnDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer != null)
            UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(killer).incrementKills(1);
    }
}