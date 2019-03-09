package me.stupidbot.universalcoreremake.Players;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class UniversalPlayerManager {
    public UniversalPlayer CreateUniversalPlayer(Player p) {
        File pFileLoc = GetPlayerDataFile(p);
        FileConfiguration pFile = LoadPlayerDataFile(p);
        
        pFile.set("Name.Name", p.getName());

        return new UniversalPlayer(pFileLoc, pFile);
    }

    private File GetPlayerDataFile(Player p) {
        return new File(
                UniversalCoreRemake.getInstance().getDataFolder() + File.separator + "Player Data" + File.separator +
                        p.getUniqueId() + ".yml");
    }

    private FileConfiguration LoadPlayerDataFile(Player p) {
        File pFile = GetPlayerDataFile(p);

        if (!pFile.exists())
            try {
                pFile.createNewFile();
            } catch (IOException e) { // TODO Properly handle errors
                e.printStackTrace();
                return null;
            }

        return YamlConfiguration.loadConfiguration(pFile);
    }

    private void SavePlayerDataFile(UniversalPlayer uP) {

    }
}