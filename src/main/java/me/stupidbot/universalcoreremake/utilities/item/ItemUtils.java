package me.stupidbot.universalcoreremake.utilities.item;

import com.google.common.io.BaseEncoding;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ItemUtils {
    public static ItemStack removeItem(ItemStack i, int removeAmount) {
            int itemAmount = i.getAmount();
            if (itemAmount <= removeAmount)
                return new org.bukkit.inventory.ItemStack(Material.AIR);
            else {
                i.setAmount(itemAmount - removeAmount);
                return i;
        }
    }

    public static void addItemSafe(Player p, List<ItemStack> items) {
        boolean invFull = false;
        for (ItemStack item : items) {
            invFull = p.getInventory().firstEmpty() == -1;

            if (!invFull)
                p.getInventory().addItem(item);
            else
                p.getWorld().dropItemNaturally(p.getLocation(), item);
        }
        if (invFull)
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYour inventory is full! Some items have been dropped!"));
    }

    public static void addItemSafe(Player p, ItemStack item) {
        addItemSafe(p, Collections.singletonList(item));
    }

    // Bukkit's serialize won't work for some things so use these methods instead
    public static String serializeItemStack(ItemStack itemStack) {
        if (itemStack == null) return "null";

        ByteArrayOutputStream outputStream = null;
        try {
            Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
            Constructor<?> nbtTagCompoundConstructor = nbtTagCompoundClass.getConstructor();
            Object nbtTagCompound = nbtTagCompoundConstructor.newInstance();
            Object nmsItemStack = getOBClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, itemStack);
            getNMSClass("ItemStack").getMethod("save", nbtTagCompoundClass).invoke(nmsItemStack, nbtTagCompound);
            outputStream = new ByteArrayOutputStream();
            getNMSClass("NBTCompressedStreamTools").getMethod("a", nbtTagCompoundClass, OutputStream.class).invoke(null, nbtTagCompound, outputStream);
        } catch(Exception e) {
            e.printStackTrace();
        }
        assert outputStream != null;

        //noinspection UnstableApiUsage
        return BaseEncoding.base64().encode(Objects.requireNonNull(outputStream).toByteArray());
    }

    public static ItemStack deserializeItemStack(String itemStackString) {
        if (itemStackString.equals("null")) return null;

        @SuppressWarnings("UnstableApiUsage") ByteArrayInputStream inputStream = new ByteArrayInputStream(BaseEncoding.base64().decode(itemStackString));

        Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
        Class<?> nmsItemStackClass = getNMSClass("ItemStack");
        ItemStack itemStack = null;
        try {
            Object nbtTagCompound = getNMSClass("NBTCompressedStreamTools").getMethod("a", InputStream.class).invoke(null, inputStream);
            Object craftItemStack = nmsItemStackClass.getMethod("createStack", nbtTagCompoundClass).invoke(null, nbtTagCompound);
            itemStack = (ItemStack) getOBClass("inventory.CraftItemStack").getMethod("asBukkitCopy", nmsItemStackClass).invoke(null, craftItemStack);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return itemStack;
    }

    private static Class<?> getNMSClass(String className) {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.replace(".", ",").split(",")[3];
        String classLocation = "net.minecraft.server." + version + "." + className;
        Class<?> nmsClass = null;
        try {
            nmsClass = Class.forName(classLocation);
        } catch(ClassNotFoundException e){
            e.printStackTrace();
            System.err.println("Unable to find reflection class " + classLocation + "!");
        }
        return nmsClass;
    }

    @SuppressWarnings("SameParameterValue")
    private static Class<?> getOBClass(String className) {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.replace(".", ",").split(",")[3];
        String classLocation = "org.bukkit.craftbukkit." + version + "." + className;
        Class<?> nmsClass = null;
        try {
            nmsClass = Class.forName(classLocation);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Unable to find reflection class " + classLocation + "!");
        }
        return nmsClass;
    }
}