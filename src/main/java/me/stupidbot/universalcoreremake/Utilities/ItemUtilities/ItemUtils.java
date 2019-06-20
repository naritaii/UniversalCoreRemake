package me.stupidbot.universalcoreremake.Utilities.ItemUtilities;

import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public class ItemUtils {
    public static void addItemSafe(Player p, org.bukkit.inventory.ItemStack[] items) {
        boolean invFull = false;
        for (org.bukkit.inventory.ItemStack item : items) {
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
    public static void addItemSafe(Player p, org.bukkit.inventory.ItemStack item) {
        addItemSafe(p, new org.bukkit.inventory.ItemStack[]{ item });
    }

    static org.bukkit.inventory.ItemStack setMetadata(org.bukkit.inventory.ItemStack item, String metadata, Object value) {
        return CraftItemStack.asBukkitCopy(setMetadata(CraftItemStack.asNMSCopy(item), metadata, value));
    }

    private static ItemStack setMetadata(ItemStack item, String metadata, Object value) {
        if (item.getTag() == null)
            item.setTag(new NBTTagCompound());

        setTag(item.getTag(), metadata, value);

        return item;
    }

    static boolean hasMetadata(org.bukkit.inventory.ItemStack item, String metadata) {
        return hasMetadata(CraftItemStack.asNMSCopy(item), metadata);
    }

    private static boolean hasMetadata(ItemStack item, String metadata) {
        return item.getTag() != null && item.getTag().hasKey(metadata);
    }

    static Object getMetadata(org.bukkit.inventory.ItemStack item, String metadata) {
        return getMetadata(CraftItemStack.asNMSCopy(item), metadata);
    }

    private static Object getMetadata(ItemStack item, String metadata) {
        if (!hasMetadata(item, metadata))
            return null;

        return getObject(item.getTag().get(metadata));
    }

    private static NBTTagCompound setTag(NBTTagCompound tag, String tagString, Object value) {
        NBTBase base = null;

        if (value instanceof Boolean) {
            base = new NBTTagByte((byte) ((Boolean) value ? 1 : 0));
        } else if (value instanceof Long) {
            base = new NBTTagLong((Long) value);
        } else if (value instanceof Integer) {
            base = new NBTTagInt((Integer) value);
        } else if (value instanceof Byte) {
            base = new NBTTagByte((Byte) value);
        } else if (value instanceof Double) {
            base = new NBTTagDouble((Double) value);
        } else if (value instanceof Float) {
            base = new NBTTagFloat((Float) value);
        } else if (value instanceof String) {
            base = new NBTTagString((String) value);
        } else if (value instanceof Short) {
            base = new NBTTagShort((Short) value);
        }

        if (base != null) {
            tag.set(tagString, base);
        }

        return tag;
    }

    @SuppressWarnings("unchecked")
    private static Object getObject(NBTBase tag) {
        if (tag instanceof NBTTagEnd) {
            return null;
        } else if (tag instanceof NBTTagLong) {
            return ((NBTTagLong) tag).c();
        } else if (tag instanceof NBTTagByte) {
            return ((NBTTagByte) tag).f();
        } else if (tag instanceof NBTTagShort) {
            return ((NBTTagShort) tag).e();
        } else if (tag instanceof NBTTagInt) {
            return ((NBTTagInt) tag).d();
        } else if (tag instanceof NBTTagFloat) {
            return ((NBTTagFloat) tag).h();
        } else if (tag instanceof NBTTagDouble) {
            return ((NBTTagDouble) tag).g();
        } else if (tag instanceof NBTTagByteArray) {
            return ((NBTTagByteArray) tag).c();
        } else if (tag instanceof NBTTagString) {
            return ((NBTTagString) tag).a_();
        } else if (tag instanceof NBTTagList) {
            List<NBTBase> list = null;
            try {
                Field field = tag.getClass().getDeclaredField("list");
                field.setAccessible(true);
                list = (List<NBTBase>) field.get(tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (list == null)
                return null;
            List<Object> toReturn = Lists.newArrayList();
            for (NBTBase base : list) {
                toReturn.add(getObject(base));
            }
            return toReturn;
        } else if (tag instanceof NBTTagCompound) {
            return tag;
        } else if (tag instanceof NBTTagIntArray) {
            return ((NBTTagIntArray) tag).c();
        }

        return null;
    }

    // Bukkit's serialize won't work with these custom nbt items so use these methods instead
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