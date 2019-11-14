package me.stupidbot.universalcoreremake.items;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BrokenStick extends ItemStack {
    public BrokenStick() {
        super(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(new ItemStack(Material.STICK)).name("&rBroken Stick")
                                .lore("&7Damage: &a+2-3")
                                .lore("")
                                .lore("&7&oMaybe this stick was once the hilt of a")
                                .lore("&7&olegendary sword! Or part of a chew toy...").build(),
                        "DAMAGE", "2_3"),
                "ITEM", "BROKEN_STICK"));
    }
}