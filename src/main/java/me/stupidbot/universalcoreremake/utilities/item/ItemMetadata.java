package me.stupidbot.universalcoreremake.utilities.item;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@SuppressWarnings("UnusedReturnValue")
public class ItemMetadata implements Listener {
    private static final String hiddenPl = ChatColor.translateAlternateColorCodes(
            '&', "&0[HIDDEN]");
    private static final String metaPl = "[META]";

    static { // Hide lore lines that start with [HIDDEN], doesn't work in creative mode
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(UniversalCoreRemake.getInstance(),
                ListenerPriority.NORMAL, PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS) {
            @Override
            public void onPacketSending(PacketEvent e) {
                e.setCancelled(false);
                if (e.getPacketType() == PacketType.Play.Server.SET_SLOT) {
                    PacketContainer packet = e.getPacket().deepClone();
                    StructureModifier<ItemStack> sm = packet
                            .getItemModifier();
                    for (int j = 0; j < sm.size(); j++) {
                        if (sm.getValues().get(j) != null) {
                            ItemStack item = sm.getValues().get(j);
                            if (item != null) {
                                if (item.hasItemMeta()) {
                                    ItemMeta itemMeta = item.getItemMeta();
                                    if (itemMeta.hasLore()) {
                                        List<String> newLore = new ArrayList<>();
                                        for (String s : itemMeta.getLore()) {
                                            if (!s.startsWith(hiddenPl)) {
                                                newLore.add(s);
                                            }
                                        }
                                        itemMeta.setLore(newLore);
                                        item.setItemMeta(itemMeta);
                                    }
                                }
                            }
                        }
                    }
                    e.setPacket(packet);
                }
                if (e.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
                    PacketContainer packet = e.getPacket().deepClone();
                    StructureModifier<ItemStack[]> sm = packet
                            .getItemArrayModifier();
                    for (int j = 0; j < sm.size(); j++) {
                        for (int i = 0; i < sm.getValues().size(); i++) {
                            if (sm.getValues().get(j) != null) {
                                for (ItemStack item : sm.getValues().get(j)) {
                                    if (item != null) {
                                        if (item.hasItemMeta()) {
                                            ItemMeta itemMeta = item.getItemMeta();
                                            if (itemMeta.hasLore()) {
                                                List<String> newLore = new ArrayList<>();
                                                for (String s : itemMeta.getLore()) {
                                                    if (!s.startsWith(hiddenPl)) {
                                                        newLore.add(s);
                                                    }
                                                }
                                                itemMeta.setLore(newLore);
                                                item.setItemMeta(itemMeta);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    e.setPacket(packet);
                }
            }
        });
    }

    // TODO Add a system to save savedItems to file OnDisable
    private final Map<UUID, List<String>> savedItems = new HashMap<>();
    private void giveSavedItems(Player p) {
        List<String> items = savedItems.get(p.getUniqueId());
        List<ItemStack> deserialize = new ArrayList<>();
        for (String i : items)
            deserialize.add(ItemUtils.deserializeItemStack(i));
        savedItems.remove(p.getUniqueId());
        ItemUtils.addItemSafe(p, deserialize);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&aYour items with hidden data have been returned!"));
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
        Player p = e.getPlayer();
        if (e.getNewGameMode() != GameMode.CREATIVE || savedItems.containsKey(p.getUniqueId()))
            if (savedItems.containsKey(p.getUniqueId()))
                giveSavedItems(p);
        if (e.getNewGameMode() == GameMode.CREATIVE) {
            PlayerInventory inv = p.getInventory();
            List<String> customMetaItems = new ArrayList<>();
            for (ItemStack i : inv.getContents())
                if (i != null && i.getItemMeta().hasLore())
                    for (String s : i.getItemMeta().getLore())
                        if (s.startsWith(hiddenPl)) {
                            customMetaItems.add(ItemUtils.serializeItemStack(i));
                            inv.remove(i);
                        }

            if (!customMetaItems.isEmpty()) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aItems with hidden data have been removed from your inventory so that no " +
                                "data is lost.\nYou'll get them back when you &eget out of creative mode." +
                                "\n&cIf the server shuts off while you're in creative mode you will lose your items " +
                                "&lPERMANENTLY!"));
                savedItems.put(p.getUniqueId(), customMetaItems);
            }
        }
    }


//    public static String getMetaAsString(ItemStack i) {
//        int metaLine = getMetaLine(i);
//        if (metaLine > -1)
//            return ChatColor.stripColor(i.getItemMeta().getLore().get(metaLine));
//        else
//            return null;
//    }

    public static Map<String, String> getMeta(ItemStack i) {
        int metaLine = getMetaLine(i);
        if (metaLine > -1) {
            String meta = i.getItemMeta().getLore().get(metaLine).replace(hiddenPl + metaPl, "");
            Map<String, String> map = new HashMap<>();
            for (String m : meta.split(",")) {
                String[] metaSplit = m.split(":");
                map.put(metaSplit[0], metaSplit[1]);
            }
            return map;
        } else
            return Collections.emptyMap();
    }

    public static boolean hasMeta(ItemStack i, String meta) {
        Map<String, String> metas = getMeta(i);
        if (!metas.isEmpty())
            return metas.containsKey(meta);
        else
            return false;
    }

    public static String getMeta(ItemStack i, String meta) {
        Map<String, String> metas = getMeta(i);
        if (!metas.isEmpty())
            return metas.get(meta);
        else
            return null;
    }

    public static ItemStack setMeta(ItemStack i, String meta, String value) {
        Map<String, String> metas = getMeta(i);
        if (!metas.isEmpty()) {
            int metaLine = getMetaLine(i);
            ItemMeta im = i.getItemMeta();
            List<String> lore = im.getLore();
            StringBuilder s = new StringBuilder(hiddenPl + metaPl);
            Iterator<Map.Entry<String, String>> it = metas.entrySet().iterator();
            boolean updated = false;

            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String m = entry.getKey();
                String v = entry.getValue();

                if (!m.equals(meta))
                    s.append(m)
                            .append(":")
                            .append(v);
                else {
                    s.append(meta)
                            .append(":")
                            .append(value);
                    updated = true;
                }

                if (it.hasNext())
                    s.append(",");
            }
            if (!updated)
                s.append(",")
                        .append(meta)
                        .append(":")
                        .append(value);

            lore.set(metaLine, s.toString());
            im.setLore(lore);
            i.setItemMeta(im);
            return i;
        } else
            return new ItemBuilder(i).lore(hiddenPl + metaPl + meta + ":" + value, false).build();
    }

    public static ItemStack setMeta(ItemStack i, String meta, int value) {
        return setMeta(i, meta, value + "");
    }

    public static ItemStack removeAllMeta(ItemStack i) {
        int l = getMetaLine(i);
        if (l > -1) {
            ItemMeta im = i.getItemMeta();
            List<String> lore = im.getLore();
            lore.remove(l);
            im.setLore(lore);
            i.setItemMeta(im);
            return i;
        } else
            return i;
    }

    public static int getMetaLine(ItemStack is) {
        List<String> lore = is.getItemMeta().getLore();
        if (lore != null)
            for (int i = 0; i < lore.size(); i++)
                if (lore.get(i).startsWith(hiddenPl + metaPl))
                    return i;
        return -1;
    }
}