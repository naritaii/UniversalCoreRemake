package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ReinforcedIronLeggings extends ItemStack {
    public ReinforcedIronLeggings() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.IRON_LEGGINGS).name("&rReinforced Iron Leggings").unbreakable(true)
                                .flag(ItemFlag.values())
                                .lore("&7Health: &a+6")
                                .lore("&7Defense: &a+7").build(),
                        "HEALTH", "6"),
                "DEFENSE", "7"),
                "ITEM", "REINFORCED_IRON_LEGGINGS"));
    }
}