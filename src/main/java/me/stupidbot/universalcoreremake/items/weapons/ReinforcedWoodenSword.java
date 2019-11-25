package me.stupidbot.universalcoreremake.items.weapons;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ReinforcedWoodenSword extends ItemStack {
    public ReinforcedWoodenSword() {
        super(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.WOOD_SWORD).name("&rReinforced Wooden Sword").unbreakable(true)
                                .flag(ItemFlag.values())
                                .lore("&7Damage: &a+7").build(),
                        "DAMAGE", "7"),
                "ITEM", "REINFORCED_WOODEN_SWORD"));
    }
}