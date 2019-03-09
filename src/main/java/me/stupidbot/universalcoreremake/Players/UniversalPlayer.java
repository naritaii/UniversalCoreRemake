package me.stupidbot.universalcoreremake.Players;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;

public class UniversalPlayer {

    private File pFileLoc;
    private FileConfiguration pFile;

    UniversalPlayer(File pFileLoc, FileConfiguration pFile) {
        this.pFileLoc = pFileLoc;
        this.pFile = pFile;
    }

    public File getPlayerDataFile() {
        return pFileLoc;
    }

    public FileConfiguration loadPlayerDataFile() {
        return pFile;
    }

    public void savePlayerDataFile() {
        try {
            pFile.save(pFileLoc);
        } catch (IOException e) { // TODO Properly handle errors
            e.printStackTrace();
        }
    }

    public void setPlayerDataName(String s) {
        pFile.set("Name.Name", s);
    }

    public String getPlayerDataName() {
        return (String) pFile.get("Name.Name");
    }

    public void setPlayerDataFirstPlayed(String s) {
        pFile.set("Stats.FirstJoin", s);
    }

    public String getPlayerDataFirstPlayed() {
        return (String) pFile.get("Stats.FirstJoin");
    }
    public void setPlayerDataLastPlayed(String s) {
        pFile.set("Stats.LastPlayed", s);
    }

    public String getPlayerDataLastPlayed() {
        return (String) pFile.get("Stats.LastPlayed");
    }
}