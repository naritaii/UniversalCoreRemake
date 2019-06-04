package me.stupidbot.universalcoreremake;

import me.stupidbot.universalcoreremake.Utilities.ItemUtils;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemLevelling {
    public static ItemStack giveXp(ItemStack i, int amount) {
        int currentXp;
        int lvl = Integer.parseInt((String) ItemUtils.getMetadata(i, "XP.Level"));
        if (ItemUtils.hasMetadata(i, "XP.XP"))
            currentXp = Integer.parseInt((String) ItemUtils.getMetadata(i, "XP.XP")) + amount;
        else
            currentXp = amount;
        if (!ItemUtils.hasMetadata(i, "XP.Level"))
            lvl = 1;
        
        ItemUtils.setMetadata(i, "XP.XP", currentXp);

        if (currentXp  >= xpToNextLevel(lvl)) {
            lvl++;
            ItemUtils.setMetadata(i, "XP.Level", lvl);

            return updateItem(i);
        } else
            return updateItem(i);
    }

    private static ItemStack updateItem(ItemStack i) {
        ItemMeta im = i.getItemMeta();
        int lvl = Integer.parseInt((String) ItemUtils.getMetadata(i, "XP.Level"));
        if (!ItemUtils.hasMetadata(i, "XP.Level"))
            lvl = 1;

        im.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                "&r" + TextUtils.capitalizeFully(i.getType().toString()) + " &d&l" + lvl));

        i.setItemMeta(im);
        return i;
    }

    public static int xpToNextLevel(int lvl) {
        return (int) ((Math.pow(++lvl, 2) * 2));
    }
}