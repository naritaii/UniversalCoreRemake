package me.stupidbot.universalcoreremake.Utilities;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class BlockMetadata {
    private static String folderPath = UniversalCoreRemake.getInstance().getDataFolder().toString();
    private static String dataPath = folderPath + File.separator + "block_metadata.yml";

    static HashMap<Location, HashMap<String, String>> blocksMetas = new HashMap<Location, HashMap<String, String>>();

    // Won't save value to file if value = null
    // Will save b to file if all b's values are null but won't load and therefore be deleted on next save
    public void setMetadata(Block b, String metadata, String value) {
        Location loc = b.getLocation();
        if (blocksMetas.containsKey(loc)) {
            HashMap<String, String> metaDataMap = blocksMetas.get(loc);

            metaDataMap.put(metadata, value);
            blocksMetas.put(loc, metaDataMap);
        } else {
            HashMap<String, String> metaDataMap = new HashMap<String, String>();

            metaDataMap.put(metadata, value);
            blocksMetas.put(loc, metaDataMap);
        }
    }

    public boolean hasMetadata(Block b, String metadata) {
        Location loc = b.getLocation();
        if (blocksMetas.containsKey(loc)) {
            HashMap<String, String> metaDataMap = blocksMetas.get(loc);
            return metaDataMap.containsKey(metadata) && metaDataMap.get(metadata) != null;
        } else
            return false;
    }

    public String getMetadata(Block b, String metadata) {
        Location loc = b.getLocation();
        if (blocksMetas.containsKey(loc)) {
            HashMap<String, String> metaDataMap = blocksMetas.get(loc);
            return metaDataMap.get(metadata);
        } else
            return null;
    }

    public void deleteAllMetadata(Block b) {
        blocksMetas.remove(b.getLocation());
    }

    public static void onEnable() {
        File path = new File(folderPath);
        File file = new File(dataPath);

        if (!path.exists())
            path.mkdirs();
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        FileConfiguration data = YamlConfiguration.loadConfiguration(file);

        if (data.get("Blocks") != null) {
            int i = 0;
            while (true) {
                String locF = (String) data.get("Block." + i + ".Location");
                if (locF == null)
                    break;
                String[] locS = locF.split(",");
                Location loc = new Location(Bukkit.getWorld(locS[0]), Integer.valueOf(locS[1]), Integer.valueOf(locS[2]),
                        Integer.valueOf(locS[3]));

                String metadatasF = ((String) data.get("Block." + i + ".Metadata"));
                if (metadatasF == null)
                    continue;
                String[] metadatas = metadatasF.split(",");

                String valuesF = (String) data.get("Block." + i + ".Values");
                if (valuesF == null)
                    continue;
                String[] values = valuesF.split(",");


                HashMap<String, String> metas = new HashMap<String, String>();
                for (int s = 0; s < metadatas.length - 1; s++)
                    try {
                        metas.put(metadatas[s], values[s]);
                    } catch (NullPointerException e) {
                        continue;
                    }
                blocksMetas.put(loc, metas);

                i++;
            }
        }
    }

    public static void onDisable() {
        if (!blocksMetas.isEmpty()) {
            File path = new File(folderPath);
            File file = new File(dataPath);

            if (!path.exists())
                path.mkdirs();
            if (!file.exists())
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            FileConfiguration data = YamlConfiguration.loadConfiguration(file);
            int i = 0;
            for (Location key : blocksMetas.keySet()) {
                data.set("Block." + i + ".Location", key.getWorld() + "," + key.getBlockX() + "," + key.getBlockY()
                        + "," + key.getBlockZ());

                String metas = "";
                for (String meta : blocksMetas.get(key).keySet()) {
                    if (!metas.equals(""))
                        metas += ",";
                    metas += meta;
                }
                data.set("Block." + i + ".Metadata", metas.equals("") ? null : metas);

                String values = "";
                for (String value : blocksMetas.get(key).values()) {
                    if (!values.equals(""))
                        values += ",";
                    values += value;
                }
                data.set("Block." + i + ".Values", metas.equals("") ? null : values);

                i++;
            }
            try {
                data.save(dataPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}