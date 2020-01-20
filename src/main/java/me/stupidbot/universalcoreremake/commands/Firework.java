package me.stupidbot.universalcoreremake.commands;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

class Firework {
    Map<UUID, Long> cooldowns = new HashMap<>();

    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s.hasPermission("universalcore.firework"))
            if (s instanceof Player) {
                Player p = (Player) s;

                if (!cooldowns.containsKey(p.getUniqueId()) ||
                        System.nanoTime() - cooldowns.get(p.getUniqueId()) > 3e+9) { // 3-second cooldown
                    cooldowns.put(p.getUniqueId(), System.nanoTime());
                    org.bukkit.entity.Firework fw = (org.bukkit.entity.Firework) p.getWorld().spawnEntity(p.getLocation(),
                            EntityType.FIREWORK);
                    FireworkMeta fwm = fw.getFireworkMeta();

                    Random r = new Random();

                    int rt = r.nextInt(5) + 1;
                    FireworkEffect.Type type = null;
                    if (rt == 1)
                        type = FireworkEffect.Type.BALL;
                    if (rt == 2)
                        type = FireworkEffect.Type.BALL_LARGE;
                    if (rt == 3)
                        type = FireworkEffect.Type.BURST;
                    if (rt == 4)
                        type = FireworkEffect.Type.CREEPER;
                    if (rt == 5)
                        type = FireworkEffect.Type.STAR;

                    int r1i = r.nextInt(17) + 1;
                    int r2i = r.nextInt(17) + 1;
                    Color c1 = getColor(r1i);
                    Color c2 = getColor(r2i);

                    assert type != null;
                    fwm.addEffect(FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2)
                            .with(type).trail(true).build());

                    fwm.setPower(1);

                    fw.setFireworkMeta(fwm);
                } else
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou must wait &6" +
                                    (int) (System.nanoTime() - cooldowns.get(p.getUniqueId()) / 1000000000) + "s&c " +
                                    "before using this command again!"));
            } else
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cOnly players may use this command!"));
        else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }

    private Color getColor(int i) {
        Color c = null;
        if (i == 1)
            c = Color.AQUA;
        if (i == 2)
            c = Color.BLACK;
        if (i == 3)
            c = Color.BLUE;
        if (i == 4)
            c = Color.FUCHSIA;
        if (i == 5)
            c = Color.GRAY;
        if (i == 6)
            c = Color.GREEN;
        if (i == 7)
            c = Color.LIME;
        if (i == 8)
            c = Color.MAROON;
        if (i == 9)
            c = Color.NAVY;
        if (i == 10)
            c = Color.OLIVE;
        if (i == 11)
            c = Color.ORANGE;
        if (i == 12)
            c = Color.PURPLE;
        if (i == 13)
            c = Color.RED;
        if (i == 14)
            c = Color.SILVER;
        if (i == 15)
            c = Color.TEAL;
        if (i == 16)
            c = Color.WHITE;
        if (i == 17)
            c = Color.YELLOW;
        return c;
    }
}