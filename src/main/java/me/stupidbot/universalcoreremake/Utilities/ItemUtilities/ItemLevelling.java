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

    @SuppressWarnings("ConstantConditions")
    private static ItemStack giveXp(ItemStack i, int amount) {
        Map<String, String> meta = ItemMetadata.getMeta(i);
        int currentXp = Integer.parseInt(meta.getOrDefault("XP", "0")) + amount;
        int totalXp = Integer.parseInt(meta.getOrDefault("TOTAL_XP", "0")) + amount;
        int lvl = Integer.parseInt(meta.getOrDefault("LEVEL", "1"));
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

    @SuppressWarnings("ConstantConditions")
    public static ItemStack updateItem(ItemStack i) {
        Map<String, String> meta = ItemMetadata.getMeta(i);
        int lvl = Integer.parseInt(meta.getOrDefault("LEVEL", "1"));
        int currentXp = Integer.parseInt(meta.getOrDefault("XP", "0"));
        String name = meta.getOrDefault("CUSTOM_NAME", TextUtils.capitalizeFully(i.getType().toString()));
        String metaStr = i.getItemMeta().getLore().get(ItemMetadata.getMetaLine(i));

        return new ItemBuilder(i).name("&r" + name + " &5&l" + lvl)
                .clearLore()
                .lore(metaStr, false)
                .lore("&7XP: &b" + currentXp + "/" + xpToNextLevel(lvl)).build();
    }

    private static int xpToNextLevel(int lvl) {
        return (int) ((Math.pow(++lvl, 2) * 2));
    }

    public static List<Material> getPickaxes() {
        return pickaxes;
    }
}