package me.stupidbot.universalcoreremake.items.weapons;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class BasicBow extends ItemStack {
    public BasicBow() {
        super(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.BOW).name("&rBasic Bow").unbreakable(true)
                                .flag(ItemFlag.values())
                                .lore("&7Damage: &a+7")
                                .lore("")
                                .lore("&7&oSo basic it's pH is >14!")
                                .lore("&7&o'I'm not like the *OTHER* bows.'").build(),
                        "DAMAGE", "7"),
                "ITEM", "BASIC_BOW"));
    }
}