package me.stupidbot.universalcoreremake.managers.universalobjective;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.events.UniversalBlockBreakEvent;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.StringReward;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class UniversalObjectiveManager implements Listener {
    public List<UniversalObjective> registeredObjectives = new ArrayList<>();
/*    privateList<UniversalObjective> STORY_QUESTObjectives = new ArrayList<>();
    private List<UniversalObjective> ACHIEVEMENTObjectives = new ArrayList<>();
    private List<UniversalObjective> MINE_BLOCKObjectives = new ArrayList<>();
    private List<UniversalObjective> TALK_TO_NPCObjectives = new ArrayList<>();*/

    public UniversalObjectiveManager() {
        instantiate();
    }

    private void instantiate() {
        registerObjectives();
    }

    public void registerObjectives() {
        registeredObjectives.forEach((UniversalObjective::saveData));
        registeredObjectives = new ArrayList<>();

/*        STORY_QUESTObjectives = new ArrayList<>();
        ACHIEVEMENTObjectives = new ArrayList<>();

        MINE_BLOCKObjectives = new ArrayList<>();
        TALK_TO_NPCObjectives = new ArrayList<>();*/

        // Register any hard coded objectives here too

        UniversalCoreRemake instance = UniversalCoreRemake.getInstance();
        File path = instance.getDataFolder();
        File file = new File(path.toString() + File.separator + "universal_objectives.yml");

        if (!path.exists())
            path.mkdirs();
        if (!file.exists())
            copy(instance.getResource("universal_objectives.yml"), file);

        FileConfiguration c = YamlConfiguration.loadConfiguration(file);

        for (String o : c.getConfigurationSection("Objectives").getKeys(false)) {
            String p = "Objectives." + o + ".";
            String id = c.getString(p + "ID");
            ItemBuilder item = new ItemBuilder(new ItemStack(
                    Material.valueOf(c.getString(p + "DisplayItem.ItemMaterial")),
                    (short) c.getInt(p + "DisplayItem.ItemData")))
                    .name(c.getString(p + "DisplayItem.DisplayName")
                            .replace("%id_formatted%", TextUtils.capitalizeFully(id)));
            List<String> rewards =  c.getStringList(p + "StringRewards");
            StringReward stringReward = rewards.isEmpty() ? null : new StringReward(rewards.toArray(new String[0]));

            List<String> lore = c.getStringList(p + "DisplayItem.Lore");
            if (!lore.isEmpty())
                for (String line : lore)
                    item.lore(line);


            registeredObjectives.add(new UniversalObjective(
                    UniversalObjective.TaskType.valueOf(c.getString(p + "TaskType")),
                    c.getStringList(p + "TaskInfo").toArray(new String[0]),
                    id,
                    item.build(),
                    stringReward,
                    c.getString(p + "Description"),
                    UniversalObjective.Catagory.valueOf(c.getString(p + "Catagory"))
            ));

            Bukkit.getOnlinePlayers().forEach(this::updateTracking);
        }
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disable() {
        registeredObjectives.forEach((UniversalObjective::saveData));
    }

    /**
     *           Increments by {@param amt} if {@param task} and {@param targetInfo}
     *           are equal to that of a {@link UniversalObjective} tracking {@param p}
     */
    private void increment(UniversalObjective.TaskType task, String taskInfo, Player p, int amt) {
        registeredObjectives.forEach((UniversalObjective uo) -> { // TODO Use a dictionary map instead if possible
            switch (uo.getTask()) {
                case MINE_BLOCK:
                    if (uo.getPlayersTracking().contains(p.getUniqueId()))
                        for (String i : uo.getTaskInfo()[2].split(","))
                            if (i.equals(taskInfo)) {
                                int progress = uo.increment(p, amt);
                                if (progress >= Integer.parseInt(uo.getTaskInfo()[1]))
                                    reward(p, uo);
                                break;
                            }
                    break;

                default:
                    if (uo.getPlayersTracking().contains(p.getUniqueId()) &&
                            uo.getTaskInfo()[2].equals(taskInfo)) {
                        int progress = uo.increment(p, amt);
                        if (progress >= Integer.parseInt(uo.getTaskInfo()[1]))
                            reward(p, uo);
                    }
                    break;
            }
        });
    }


    private void reward(Player p, UniversalObjective uo) {
        StringReward rewards = uo.getRewards();
        UniversalObjective.Catagory type = uo.getCategory();

        uo.removePlayer(p);
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        up.addCompletedObjective(uo.getId());
        up.removeSelectedObjective(uo.getId()); // If objective was manually selected
        up.removeObjectiveData(uo.getId()); // No longer needed data

        p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
        String hover = ChatColor.translateAlternateColorCodes('&',
                "&d&l" + TextUtils.capitalizeFully(uo.getId()) + "\n&e" + uo.getDescription());
        switch (type) {
            case STORY_QUEST:
                String message = ChatColor.translateAlternateColorCodes('&',
                        "&aStory Quest: &n" + TextUtils.capitalizeFully(uo.getId()) + "&a completed!");
                p.spigot().sendMessage(new ComponentBuilder(message).event(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create())).create());

                if (rewards != null) {
                    for (String s : rewards.asStrings())
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &8+" + s));
                    rewards.giveNoMessage(p);
                }
                break;

            case ACHIEVEMENT:
                String messageA = TextUtils.centerMessage("&e&k3&a\u2727>  Achievement Get: &6&n"
                        + TextUtils.capitalizeFully(uo.getId())
                        + "&a  <\u2727&e&k3", 154);
                p.spigot().sendMessage(new ComponentBuilder(messageA).event(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create())).create());

                if (rewards != null) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRewards:"));
                    for (String s : rewards.asStrings())
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &8+" + s));
                    rewards.giveNoMessage(p);
                }
                break;

            default:
                String messaged = ChatColor.translateAlternateColorCodes('&',
                        "&a" + TextUtils.capitalizeFully(type.toString()) + ": &n" +
                                TextUtils.capitalizeFully(uo.getId()) + "&a completed!");
                p.spigot().sendMessage(new ComponentBuilder(messaged).event(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create())).create());

                if (rewards != null) {
                    for (String s : rewards.asStrings())
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &8+" + s));
                    rewards.giveNoMessage(p);
                }
                break;
        }
    }

    private void updateTracking(Player p) {
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        List<String> completed = up.getCompletedObjectives();
        if (up.firstJoin())
            up.addSelectedObjective("getting_started");
        List<String> selected = up.getSelectedObjectives();

        registeredObjectives.forEach((uo) -> {
            uo.removePlayer(p);
            if (!completed.contains(uo.getId()))
                if (uo.getCategory() == UniversalObjective.Catagory.ACHIEVEMENT)
                        uo.addPlayer(p);
                else if (selected.contains(uo.getId()))
                    uo.addPlayer(p);
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        updateTracking(p);
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent e) {
        registeredObjectives.forEach((UniversalObjective uo) -> uo.removePlayer(e.getPlayer()));
    }

    // Objective listeners
    @EventHandler
    public void OnUniversalBlockBreak(UniversalBlockBreakEvent e) {
        Player p = e.getPlayer();
        int amt = e.getAmount();
        String b = e.getBlock().getType().toString();
        increment(UniversalObjective.TaskType.MINE_BLOCK, b, p, amt);
    }

    @EventHandler
    public void OnNPCClick(NPCLeftClickEvent e) {
        Player p = e.getClicker();
        NPC npc = e.getNPC();
        String id = npc.getUniqueId().toString();
        increment(UniversalObjective.TaskType.TALK_TO_NPC, id, p, 1);
    }

    @EventHandler
    public void OnNPCClick(NPCRightClickEvent e) {
        Player p = e.getClicker();
        NPC npc = e.getNPC();
        String id = npc.getUniqueId().toString();
        increment(UniversalObjective.TaskType.TALK_TO_NPC, id, p, 1);
    }
}
