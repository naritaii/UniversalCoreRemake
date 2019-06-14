package me.stupidbot.universalcoreremake.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemLevelling {
    private static final List<Material> pickaxes =  Arrays.asList(Material.WOOD_PICKAXE, Material.STONE_PICKAXE,
            Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE);

    public static ItemStack giveXp(ItemStack i, int amount) {
        int currentXp;
        int lvl = Integer.parseInt((String) ItemUtils.getMetadata(i, "XPLevel"));
        if (ItemUtils.hasMetadata(i, "XP"))
            currentXp = Integer.parseInt((String) ItemUtils.getMetadata(i, "XP")) + amount;
        else
            currentXp = amount;
        if (!ItemUtils.hasMetadata(i, "XPLevel"))
            lvl = 1;
        
        ItemUtils.setMetadata(i, "XP", currentXp);

        if (currentXp  >= xpToNextLevel(lvl)) {
            ItemUtils.setMetadata(i, "XPLevel", ++lvl);
            return updateItem(i);
        } else
            return updateItem(i);
    }

    private static ItemStack updateItem(ItemStack i) {
        ItemMeta im = i.getItemMeta();
        int lvl = Integer.parseInt((String) ItemUtils.getMetadata(i, "XPLevel"));
        if (!ItemUtils.hasMetadata(i, "XPLevel"))
            lvl = 1;
        String name = TextUtils.capitalizeFully(i.getType().toString());
        if (ItemUtils.hasMetadata(i, "CustomName"))
            name = (String) ItemUtils.getMetadata(i, "CustomName");

        im.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                "&r" + name + " &5&l" + lvl));

        ArrayList<String> lore = new ArrayList<String>();

        im.setLore(lore);

        // TODO Change item lore and account for enchants/"mutations". Item XP will probably be called something like
        // TODO "corruption" on the client side despite just being labelled as XP in code.

        i.setItemMeta(im);
        return i;
    }

    private static int xpToNextLevel(int lvl) {
        return (int) ((Math.pow(++lvl, 2) * 2));
    }

    public static List<Material> getPickaxes() {
        return pickaxes;
    }
}