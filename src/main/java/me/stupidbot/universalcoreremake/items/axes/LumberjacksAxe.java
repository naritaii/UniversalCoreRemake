package me.stupidbot.universalcoreremake.items.axes;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemLevelling;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class LumberjacksAxe extends ItemStack {
    public LumberjacksAxe() {
        super(ItemLevelling.updateItem(ItemMetadata.setMeta(ItemMetadata.setMeta(ItemMetadata.setMeta(ItemMetadata.setMeta(
                new ItemBuilder(Material.STONE_AXE)
                        .enchantment(Enchantment.DIG_SPEED, 1).unbreakable(true).flag(ItemFlag.values()).build(),
                "CUSTOM_NAME", "&aLumberjack's Axe"),
                "LORE", "&7&oCrafted by your best friend~."),
                "STAMINA_USE", "1"),
                "ITEM", "LUMBERJACKS_AXE")));
    }
}
