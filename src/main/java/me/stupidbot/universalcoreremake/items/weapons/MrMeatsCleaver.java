package me.stupidbot.universalcoreremake.items.weapons;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class MrMeatsCleaver extends ItemStack {
    public MrMeatsCleaver() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.GOLD_AXE).name("&aMr. Meat's Cleaver").unbreakable(true).flag(ItemFlag.values())
                                .lore("&7Damage: &a+5-6")
                                .lore("")
                                .lore("&7&oGood for cutting flesh...").build(),
                        "DAMAGE", "5_6"),
                "ITEM", "MR_MEATS_CLEAVER"),
                "NO_MINING_XP", "1"));
    }
}