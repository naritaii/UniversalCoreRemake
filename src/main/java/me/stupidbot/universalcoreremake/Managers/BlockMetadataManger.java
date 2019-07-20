package me.stupidbot.universalcoreremake.Managers;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class BlockMetadataManger {
    public BlockMetadataManger() {
        initialize();
    }

    private final String folderPath = UniversalCoreRemake.getInstance().getDataFolder().toString();
    private final String dataPath = folderPath  + File.separator + "data" + File.separator + "block_metadata.yml";

    private final HashMap<Location, HashMap<String, String>> blocksMetas = new HashMap<>();

    // Won't save value to file if value = null
    // Will save b to file if all b's values are null but won't load and therefore be deleted on next save
    public void setMeta(Block b, String metadata, String value) {
        Location loc = b.getLocation();
        if (blocksMetas.containsKey(loc)) {
            HashMap<String, String> metaDataMap = blocksMetas.get(loc);

            metaDataMap.put(metadata, value);
            blocksMetas.put(loc, metaDataMap);
        } else {
            HashMap<String, String> metaDataMap = new HashMap<>();

            metaDataMap.put(metadata, value);
            blocksMetas.put(loc, metaDataMap);
        }
    }

    boolean hasMeta(Block b, String metadata) {
        Location loc = b.getLocation();
        if (blocksMetas.containsKey(loc)) {
            HashMap<String, String> metaDataMap = blocksMetas.get(loc);
            return metaDataMap.containsKey(metadata) && metaDataMap.get(metadata) != null;
        } else
            return false;
    }

    public String getMeta(Block b, String metadata) {
        Location loc = b.getLocation();
        if (blocksMetas.containsKey(loc)) {
            HashMap<String, String> metaDataMap = blocksMetas.get(loc);
            return metaDataMap.get(metadata);
        } else
            return null;
    }

    public HashMap<String, String> getMeta(Block b) {
        Location loc = b.getLocation();
        return blocksMetas.getOrDefault(loc, null);
    }

    public void removeAllMeta(Block b) {
        blocksMetas.remove(b.getLocation());
    }

    public void initialize() {
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

        if (data.get("Block") != null) {
            int i = 0;
            while (true) {
                String locF = (String) data.get("Block." + i + ".Location");
                if (locF == null)
                    break;
                String[] locS = locF.split(",");
                Location loc = new Location(Bukkit.getWorld(locS[0]), Integer.parseInt(locS[1]), Integer.parseInt(locS[2]),
                        Integer.parseInt(locS[3]));

                String metadatasF = ((String) data.get("Block." + i + ".Metadata"));
                if (metadatasF == null)
                    continue;
                String[] metadatas = metadatasF.split(",");

                String valuesF = (String) data.get("Block." + i + ".Values");
                if (valuesF == null)
                    continue;
                String[] values = valuesF.split(",");


                HashMap<String, String> metas = new HashMap<>();
                for (int s = 0; s < metadatas.length; s++)
                    metas.put(metadatas[s], values[s]);
                blocksMetas.put(loc, metas);

                i++;
            }
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(UniversalCoreRemake.getInstance(), () -> {
            long startTime = System.nanoTime();

            save();

            long endTime = System.nanoTime();
            String s = ChatColor.translateAlternateColorCodes('&',
                    "&c[&fDEBUG&c]: &cSaved all custom block metadata data to file &a(took " +
                            TextUtils.addCommas((int) ((endTime - startTime) / 1000000)) + "ms)");

            Bukkit.broadcast(s, "universalcore.admin");
            System.out.println(s);
        }, (Duration.between(LocalDateTime.now(), LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS))
                .toMillis() / 1000) * 20, (20 * 60) * 60); // Run every hour
    }

    public void disable() {
        save();
    }

    private void save() {
        if (!blocksMetas.isEmpty()) {
            File path = new File(folderPath);
            File file = new File(dataPath);

            if (!path.exists())
                path.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileConfiguration data = YamlConfiguration.loadConfiguration(file);
            int i = 0;
            for (Location key : blocksMetas.keySet()) {
                data.set("Block." + i + ".Location", key.getWorld().getName() + "," + key.getBlockX() + "," + key.getBlockY()
                        + "," + key.getBlockZ());

                StringBuilder metas = new StringBuilder();
                for (String meta : blocksMetas.get(key).keySet()) {
                    if (metas.length() != 0)
                        metas.append(",");
                    metas.append(meta);
                }
                data.set("Block." + i + ".Metadata", metas.length() == 0 ? null : metas.toString());

                StringBuilder values = new StringBuilder();
                for (String value : blocksMetas.get(key).values()) {
                    if (values.length() != 0)
                        values.append(",");
                    values.append(value);
                }
                data.set("Block." + i + ".Values", values.length() == 0 ? null : values.toString());

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