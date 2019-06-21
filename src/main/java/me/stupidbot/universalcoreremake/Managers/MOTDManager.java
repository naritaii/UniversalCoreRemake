package me.stupidbot.universalcoreremake.Managers;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.io.File;
import java.io.IOException;

public class MOTDManager implements Listener {
    private final String folderPath = UniversalCoreRemake.getInstance().getDataFolder().toString();
    private final String dataPath = folderPath + File.separator + "motd.yml";
    private String motd;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPing(ServerListPingEvent e) {
        e.setMotd(motd);
    }

    public void reload() {
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
        StringBuilder motd = new StringBuilder();
        if (data.get("MOTD.FirstLine") == null)
            data.set("MOTD.FirstLine", "&d&lCORRUPT PRISONS&5 1.8-1.14.2");
        if (data.get("MOTD.SecondLine") == null)
            data.set("MOTD.SecondLine", "&e&kt&f GRAND OPENING &e&kt");

        motd.append(TextUtils.centerMessage(data.getString("MOTD.FirstLine"), 127));
        motd.append("\n");
        motd.append(TextUtils.centerMessage(data.getString("MOTD.SecondLine"), 127));

        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.motd = ChatColor.translateAlternateColorCodes('&', motd.toString());
    }
}
