package me.stupidbot.universalcoreremake.utilities.item;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.enchantments.UniversalEnchantment;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ItemLevelling {
    private static ItemStack giveXp(ItemStack i, int amount) {
        Map<String, String> meta = ItemMetadata.getMeta(i);
        int currentXp = Integer.parseInt(meta.getOrDefault("XP", "0")) + amount;
        int totalXp = Integer.parseInt(meta.getOrDefault("TOTAL_XP", "0")) + amount;
        int lvl = Integer.parseInt(meta.getOrDefault("LEVEL", "1"));
        int neededXp = xpToNextLevel(lvl);

        ItemMetadata.setMeta(i, "TOTAL_XP", totalXp);
        if (currentXp >= neededXp) {
            int difference = currentXp;
            while (difference >= neededXp) {
                difference -= xpToNextLevel(lvl);
                ItemMetadata.setMeta(i, "XP", difference);
                ItemMetadata.setMeta(i, "LEVEL", ++lvl);
            }
        } else {
            ItemMetadata.setMeta(i, "XP", currentXp);
        }
        return updateItem(i);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static ItemStack giveXp(Player p, ItemStack i, int amount) {
        int oldLvl = Integer.parseInt(ItemMetadata.getMeta(i).getOrDefault("LEVEL", "1"));
        ItemStack item = giveXp(i, amount);
        int newLvl = Integer.parseInt(ItemMetadata.getMeta(item).getOrDefault("LEVEL", "1"));
        if (newLvl > oldLvl)
            UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).incrementItemsLevelled(newLvl - oldLvl);
        p.setItemInHand(item);
        return item;
    }

    public static ItemStack updateItem(ItemStack i) {
        Map<String, String> meta = ItemMetadata.getMeta(i);
        if (!meta.isEmpty()) {
            int lvl = Integer.parseInt(meta.getOrDefault("LEVEL", "1"));
            int currentXp = Integer.parseInt(meta.getOrDefault("XP", "0"));
            String name = meta.getOrDefault("CUSTOM_NAME", TextUtils.capitalizeFully(i.getType().toString()));
            String metaStr = i.getItemMeta().getLore().get(ItemMetadata.getMetaLine(i));
            i.getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
            ItemBuilder ib = new ItemBuilder(i).name("&r" + name + " &5&l" + lvl)
                    .clearLore()
                    .lore(metaStr, false)
                    .lore("&7XP: &a" + TextUtils.addCommas(currentXp) + "&7/&b" + TextUtils.addCommas(xpToNextLevel(lvl))
                            + " &7[" + TextUtils.getProgressBar(currentXp, xpToNextLevel(lvl), 18, "|",
                            "&a", "&8") + "&7]");

            Map<Enchantment, Integer> enchantments = i.getEnchantments();
            Enchantment mutation = null;
            int mutationLevel = -1;
            if (!enchantments.isEmpty()) {
                ib.lore("");

                for (Map.Entry<Enchantment, Integer> ench : enchantments.entrySet()) {
                    Enchantment e = ench.getKey();
                    int l = ench.getValue();
                    if (!UniversalEnchantment.MUTATIONS.contains(e)) {
                        ib.lore("&7" + (e != Enchantment.DIG_SPEED ? e.getName() : "Efficiency") + " " + TextUtils.toRoman(l));
                        String desc = UniversalEnchantment.getDescription(e);
                        if (desc != null)
                            for (String line : desc.split("\n")) // \n with .lore(...) breaks on some clients
                                ib.lore(line);
                    } else {
                        mutation = e;
                        mutationLevel = l;
                    }

                    ib.enchantment(e, l);
                }
            }

            ib.lore("");
            if (mutation != null) {
                ib.lore("&c&lMUTATION:&6 " + mutation.getName() +
                        (mutation.getMaxLevel() > 1 ? " " + TextUtils.toRoman(mutationLevel) : ""));
                String desc = UniversalEnchantment.getDescription(mutation);
                if (desc != null)
                    ib.lore(desc);
            } else
                ib.lore("&c&lMUTATION:&7 None")
                        .lore("&7Mutations are mysterious positive OR negative abilities");

            if (meta.containsKey("LORE")) { // "LORE:Line 1^Line &b2"
                ib.lore("");
                for (String l : meta.get("LORE").split("\\^")) // Using \n breaks custom item meta
                    ib.lore(l);
            }

            return ib.build();
        } else
            return i;
    }

    private static int xpToNextLevel(int lvl) {
        return (int) ((Math.pow(++lvl, 2) * 2));
    }
}