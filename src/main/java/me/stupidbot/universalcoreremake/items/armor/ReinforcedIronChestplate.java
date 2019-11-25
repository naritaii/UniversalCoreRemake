package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ReinforcedIronChestplate extends ItemStack {
    public ReinforcedIronChestplate() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.IRON_CHESTPLATE).name("&rReinforced Iron Chestplate").unbreakable(true)
                                .flag(ItemFlag.values())
                                .lore("&7Health: &a+12")
                                .lore("&7Defense: &a+18").build(),
                        "HEALTH", "12"),
                "DEFENSE", "18"),
                "ITEM", "REINFORCED_IRON_CHESTPLATE"));
    }
}