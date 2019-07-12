package me.stupidbot.universalcoreremake.Utilities.ItemUtilities;

import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemLevelling {
    private static final List<Material> pickaxes =  Arrays.asList(Material.WOOD_PICKAXE, Material.STONE_PICKAXE,
            Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE);

    private static ItemStack giveXp(ItemStack i, int amount) {
        Map<String, String> meta = ItemMetadata.getMeta(i); // TODO Just change this to use a single if statement when you get time
        int currentXp = meta == null ? 0 : Integer.parseInt(meta.getOrDefault("XP", "0")) + amount;
        int totalXp = meta == null ? 0 : Integer.parseInt(meta.getOrDefault("TOTAL_XP", "0")) + amount;
        int lvl = meta == null ? 1 : Integer.parseInt(meta.getOrDefault("LEVEL", "1"));
        int neededXp = xpToNextLevel(lvl);

        ItemMetadata.setMeta(i, "TOTAL_XP", totalXp);
        if (currentXp  >= neededXp) { // TODO Account for multiple level ups and asynchronicity
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

            if (meta.containsKey("ENCHANT")) { // "ENCHANT:SHARPNESS#2@UNBREAKING#5"
                                               // Might handle with Bukkit's Enchantment API instead to make adding
                                               // the enchantment glow effect simple
                ib.lore("");
                // TODO Enchants

            }

            ib.lore("");
            if (meta.containsKey("MUTATION")) {
                // TODO Mutations (Essentially item skills, you can pay to re-roll an item's mutation, some mutations
                // TODO do things like make you use more stamina while others can give you a speed boost for every X
                // TODO blocks mined, some mutations cannot be obtained through simply levelling up or are item-specific)
            } else
                ib.lore("&c&lMUTATION:&7 None (Level me up and I may mutate!)");

            if (meta.containsKey("LORE")) { // "LORE:Line 1@Line &b2"
                ib.lore("");
                for (String l : meta.get("LORE").split("@"))
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