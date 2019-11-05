package me.stupidbot.universalcoreremake.managers;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.io.File;
import java.io.IOException;

public class MOTDManager implements Listener { // TODO Player count hover text/hide online players
    public MOTDManager() {
        reload();
    }

    private final String folderPath = UniversalCoreRemake.getInstance().getDataFolder().toString();
    private final String dataPath = folderPath + File.separator + "motd.yml";
    private String cachedMotd = null;
    public String motd;
    public boolean setMotd;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPing(ServerListPingEvent e) {
        if (!setMotd)
            if (cachedMotd == null || !e.getMotd().equals(cachedMotd) || this.motd == null) { // this.motd should never == null if both of the others are true but just in case
                cachedMotd = e.getMotd();
                String[] motd = cachedMotd.split("\n");

                if (motd.length > 1)
                    this.motd = TextUtils.centerMessage(motd[0], 127) +
                            "\n" +
                            TextUtils.centerMessage(motd[1], 127);
                else
                    this.motd = TextUtils.centerMessage(motd[0], 127);
            }

        e.setMotd(this.motd);
    }

    public void reload() {
        File path = new File(folderPath);
        File file = new File(dataPath);

        if (!path.exists())
            //noinspection ResultOfMethodCallIgnored
            path.mkdirs();
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        if (data.get("MOTD.FirstLine") == null)
            data.set("MOTD.FirstLine", "&d&lCORRUPT PRISONS&5 1.8-1.14.2");
        if (data.get("MOTD.SecondLine") == null)
            data.set("MOTD.SecondLine", "&e&kt&f GRAND OPENING &e&kt");
        if (data.get("MOTD.Set") == null)
            data.set("MOTD.Set", false);

        setMotd = data.getBoolean("MOTD.Set");
        String motd = null;
        if (setMotd)
            motd = TextUtils.centerMessage(data.getString("MOTD.FirstLine"), 127) +
                    "\n" +
                    TextUtils.centerMessage(data.getString("MOTD.SecondLine"), 127);
        // Else we wait for another plugin to try and set it.

        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.motd = motd;
    }
}
