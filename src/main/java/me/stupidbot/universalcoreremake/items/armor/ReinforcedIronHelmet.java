package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ReinforcedIronHelmet extends ItemStack {
    public ReinforcedIronHelmet() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.IRON_HELMET).name("&rReinforced Iron Helmet").unbreakable(true)
                                .flag(ItemFlag.values())
                                .lore("&7Health: &a+2")
                                .lore("&7Defense: &a+5").build(),
                        "HEALTH", "2"),
                "DEFENSE", "5"),
                "ITEM", "REINFORCED_IRON_HELMET"));
    }
}