package me.stupidbot.universalcoreremake.items.weapons;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class InfestedSword extends ItemStack {
    public InfestedSword() {
        super(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.GOLD_SWORD).name("&9Infested Sword").unbreakable(true)
                                .enchantment(Enchantment.FIRE_ASPECT, 2).flag(ItemFlag.values())
                                .lore("&7Damage: &a+8")
                                .lore("")
                                .lore("&7Fire Aspect II")
                                .lore("&7Set enemies on fire for a short time")
                                .lore("")
                                .lore("&7&oCrafted from the &c&oThe Beast's&7&o remains").build(),
                        "DAMAGE", "8"),
                "ITEM", "INFESTED_SWORD"));
    }
}