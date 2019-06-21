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

    public static ItemStack giveXp(ItemStack i, int amount) {
        int currentXp = ItemUtils.hasMetadata(i, "XP") ?
                Integer.parseInt((String) ItemUtils.getMetadata(i, "XP")) + amount : amount;
        int lvl = ItemUtils.hasMetadata(i, "XPLevel")
                ? Integer.parseInt((String) ItemUtils.getMetadata(i, "XPLevel")) : 1;
        
        ItemUtils.setMetadata(i, "XP", currentXp);

        if (currentXp  >= xpToNextLevel(lvl)) {
            ItemUtils.setMetadata(i, "XPLevel", ++lvl);
            return updateItem(i);
        } else
            return updateItem(i);
    }

    public static ItemStack giveXp(Player p, ItemStack i, int amount) {
        ItemStack item = giveXp(i, amount);
        p.setItemInHand(item);
        return item;
    }

    private static ItemStack updateItem(ItemStack i) {
        int lvl = ItemUtils.hasMetadata(i, "XPLevel")
                ? Integer.parseInt((String) ItemUtils.getMetadata(i, "XPLevel")) : 1;
        int currentXp = ItemUtils.hasMetadata(i, "XP") ?
                Integer.parseInt((String) ItemUtils.getMetadata(i, "XP")) : 0;
        String name = TextUtils.capitalizeFully(i.getType().toString());
        if (ItemUtils.hasMetadata(i, "CustomName"))
            name = (String) ItemUtils.getMetadata(i, "CustomName");

        return new ItemBuilder(i).name("&r" + name + " &5&l" + lvl)
                .clearLore()
                .lore("&bXP: " + currentXp + "/" + xpToNextLevel(lvl)).build();
    }

    private static int xpToNextLevel(int lvl) {
        return (int) ((Math.pow(++lvl, 2) * 2));
    }

    public static List<Material> getPickaxes() {
        return pickaxes;
    }
}