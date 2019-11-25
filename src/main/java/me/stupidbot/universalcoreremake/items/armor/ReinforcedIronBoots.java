package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ReinforcedIronBoots extends ItemStack {
    public ReinforcedIronBoots() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.IRON_BOOTS).name("&rReinforced Iron Boots").unbreakable(true)
                                .flag(ItemFlag.values())
                                .lore("&7Health: &a+5")
                                .lore("&7Defense: &a+6").build(),
                        "HEALTH", "5"),
                "DEFENSE", "6"),
                "ITEM", "REINFORCED_IRON_BOOTS"));
    }
}