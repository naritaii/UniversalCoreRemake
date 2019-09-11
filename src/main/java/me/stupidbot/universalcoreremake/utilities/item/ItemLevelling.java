package me.stupidbot.universalcoreremake.utilities.item;

import me.stupidbot.universalcoreremake.enchantments.UniversalEnchantment;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemLevelling {
    private static final List<Material> pickaxes =  Arrays.asList(Material.WOOD_PICKAXE, Material.STONE_PICKAXE,
            Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE);

    private static ItemStack giveXp(ItemStack i, int amount) {
        Map<String, String> meta = ItemMetadata.getMeta(i);
        int currentXp = meta == null ? 0 : Integer.parseInt(meta.getOrDefault("XP", "0")) + amount;
        int totalXp = meta == null ? 0 : Integer.parseInt(meta.getOrDefault("TOTAL_XP", "0")) + amount;
        int lvl = meta == null ? 1 : Integer.parseInt(meta.getOrDefault("LEVEL", "1"));
        int neededXp = xpToNextLevel(lvl);

        ItemMetadata.setMeta(i, "TOTAL_XP", totalXp);
        if (currentXp  >= neededXp) { // TODO Account for multiple level ups
            ItemMetadata.setMeta(i, "XP", currentXp - neededXp);
            ItemMetadata.setMeta(i, "LEVEL", ++lvl);
            return updateItem(i);
        } else {
            ItemMetadata.setMeta(i, "XP", currentXp);
            return updateItem(i);
        }
    }

    public static ItemStack giveXp(Player p, ItemStack i, int amount) {
        ItemStack item = giveXp(i, amount);
        p.setItemInHand(item);
        return item;
    }

    private static ItemStack updateItem(ItemStack i) {
        Map<String, String> meta = ItemMetadata.getMeta(i);
        if (meta != null) {
            int lvl = Integer.parseInt(meta.getOrDefault("LEVEL", "1"));
            int currentXp = Integer.parseInt(meta.getOrDefault("XP", "0"));
            String name = meta.getOrDefault("CUSTOM_NAME", TextUtils.capitalizeFully(i.getType().toString()));
            String metaStr = i.getItemMeta().getLore().get(ItemMetadata.getMetaLine(i));
            ItemBuilder ib = new ItemBuilder(i).name("&r" + name + " &5&l" + lvl)
                    .clearLore()
                    .lore(metaStr, false)
                    .lore("&7XP: &a" + TextUtils.addCommas(currentXp) + "&7/&a" + TextUtils.addCommas(xpToNextLevel(lvl))
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
                    if (!UniversalEnchantment.MUTATIONS.contains(e))
                        ib.lore("&7" + e.getName() + " " + TextUtils.toRoman(l));
                    else {
                        mutation = e;
                        mutationLevel = l;
                    }

                    ib.enchantment(e, l);
                }

                ib.lore("");
                if (mutation != null)
                    ib.lore("&c&lMUTATION:&6 " + mutation.getName() +
                            (mutation.getMaxLevel() > 1 ? " " + TextUtils.toRoman(mutationLevel) : ""));
                else
                    ib.lore("&c&lMUTATION:&7 None\n&7Level me up and I may mutate!");

            }

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

    public static List<Material> getPickaxes() {
        return pickaxes;
    }
}