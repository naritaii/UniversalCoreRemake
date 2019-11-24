package me.stupidbot.universalcoreremake.items.weapons;

import me.stupidbot.universalcoreremake.enchantments.UniversalEnchantment;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class BoneSword extends ItemStack {
    public BoneSword() {
        super(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.IRON_SWORD).name("&rBone Sword")
                                .enchantment(UniversalEnchantment.KILLER_OF_DEATH).flag(ItemFlag.values())
                                .lore("&7Damage: &a+6")
                                .lore("")
                                .lore("&c&lMUTATION:&6 " + UniversalEnchantment.KILLER_OF_DEATH.getName())
                                .lore(UniversalEnchantment.getDescription(UniversalEnchantment.KILLER_OF_DEATH))
                                .lore("")
                                .lore("&7&oIt kills death things and")
                                .lore("&7&ois made of... bones? Ew...").build(),
                        "DAMAGE", "6"),
                "ITEM", "BONE_SWORD"));
    }
}