package me.stupidbot.universalcoreremake.items.weapons;

import me.stupidbot.universalcoreremake.enchantments.UniversalEnchantment;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class CorruptedSword extends ItemStack {
    public CorruptedSword() {
        super(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.GOLD_SWORD).name("&rCorrupted Sword")
                                .enchantment(UniversalEnchantment.GLOW).flag(ItemFlag.values())
                                .lore("&7Damage: &a+10")
                                .lore("")
                                .lore("&7&oA sword that shines with")
                                .lore("&7&othe souls of lives long past").build(),
                        "DAMAGE", "10"),
                "ITEM", "CORRUPTED_SWORD"));
    }
}