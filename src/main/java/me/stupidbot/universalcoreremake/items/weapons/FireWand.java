package me.stupidbot.universalcoreremake.items.weapons;

import me.stupidbot.universalcoreremake.enchantments.UniversalEnchantment;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FireWand extends ItemStack {
    public FireWand() {
        super(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        ItemMetadata.setMeta(
                                new ItemBuilder(Material.BLAZE_ROD).name("&dFire Wand")
                                        .enchantment(UniversalEnchantment.BEING_FIREBALL_IS_SUFFERING)
                                        .lore("&7Damage: &a+14-16")
                                        .lore("")
                                        .lore("&c&lMUTATION:&6 " + UniversalEnchantment.BEING_FIREBALL_IS_SUFFERING.getName())
                                        .lore(UniversalEnchantment.getDescription(UniversalEnchantment.BEING_FIREBALL_IS_SUFFERING))
                                        .lore("")
                                        .lore("&7&oI always wanted to")
                                        .lore("&7&obe a magical girl〜").build(),
                                "DAMAGE", "14_16"),
                        "ITEM", "FIRE_WAND"),
                "NO_STACKING", "1"));
    }
}
