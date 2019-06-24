package me.stupidbot.universalcoreremake.Utilities.ItemUtilities;

import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ItemLevelling {
    private static final List<Material> pickaxes =  Arrays.asList(Material.WOOD_PICKAXE, Material.STONE_PICKAXE,
            Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE);

    @SuppressWarnings("ConstantConditions")
    private static ItemStack giveXp(ItemStack i, int amount) {
        int currentXp = ItemMetadata.hasMeta(i, "XP") ?
                Integer.parseInt(ItemMetadata.getMeta(i, "XP")) + amount : amount;
        int totalXp = ItemMetadata.hasMeta(i, "TOTAL_XP") ?
                Integer.parseInt(ItemMetadata.getMeta(i, "TOTAL_XP")) + amount : amount;
        int lvl = ItemMetadata.hasMeta(i, "LEVEL")
                ? Integer.parseInt(ItemMetadata.getMeta(i, "LEVEL")) : 1;

        ItemMetadata.setMeta(i, "TOTAL_XP", totalXp);
        if (currentXp  >= xpToNextLevel(lvl)) {
            ItemMetadata.setMeta(i, "XP", 0);
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
    private static ItemStack updateItem(ItemStack i) {
        int lvl = ItemMetadata.hasMeta(i, "LEVEL")
                ? Integer.parseInt(ItemMetadata.getMeta(i, "LEVEL")) : 1;
        int currentXp = ItemMetadata.hasMeta(i, "XP") ?
                Integer.parseInt(ItemMetadata.getMeta(i, "XP")) : 0;
        String name = TextUtils.capitalizeFully(i.getType().toString());
        if (ItemMetadata.hasMeta(i, "CUSTOM_NAME"))
            name = ItemMetadata.getMeta(i, "CUSTOM_NAME");
        String meta = i.getItemMeta().getLore().get(ItemMetadata.getMetaLine(i));

        return new ItemBuilder(i).name("&r" + name + " &5&l" + lvl)
                .clearLore()
                .lore(meta)
                .lore("&7XP: &b" + currentXp + "/" + xpToNextLevel(lvl)).build();
    }

    private static int xpToNextLevel(int lvl) {
        return (int) ((Math.pow(++lvl, 2) * 2));
    }

    public static List<Material> getPickaxes() {
        return pickaxes;
    }
}