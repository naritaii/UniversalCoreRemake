package me.stupidbot.universalcoreremake.managers.universalobjective;

import com.google.common.collect.ImmutableList;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.events.UniversalBlockBreakEvent;
import me.stupidbot.universalcoreremake.events.UniversalGainXpEvent;
import me.stupidbot.universalcoreremake.events.UniversalLevelUpEvent;
import me.stupidbot.universalcoreremake.events.universalobjective.UniversalObjectiveCompleteEvent;
import me.stupidbot.universalcoreremake.events.universalobjective.UniversalObjectiveIncrementEvent;
import me.stupidbot.universalcoreremake.events.universalobjective.UniversalObjectiveStartEvent;
import me.stupidbot.universalcoreremake.events.worldguard.RegionEnterEvent;
import me.stupidbot.universalcoreremake.events.worldguard.RegionLeftEvent;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.FileUtils;
import me.stupidbot.universalcoreremake.utilities.StringReward;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import me.stupidbot.universalcoreremake.utilities.item.ItemUtils;
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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjective.TaskType.*;

public class UniversalObjectiveManager implements Listener {
    public List<UniversalObjective> registeredObjectives = new ArrayList<>();
    public Map<String, Integer> registeredObjectivesDictionary;
    public Map<UUID, List<UniversalObjective>> trackedObjectives;
    public int totalAchievements;
    public List<UniversalObjective> achievements;

    public UniversalObjectiveManager() {
        instantiate();
    }

    private void instantiate() {
        registerObjectives();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void registerObjectives() {
        Bukkit.getScheduler().runTaskAsynchronously(UniversalCoreRemake.getInstance(), () -> {
            registeredObjectives.forEach(UniversalObjective::saveData);
            registeredObjectives = new ArrayList<>();
            registeredObjectivesDictionary = new HashMap<>();
            trackedObjectives = new ConcurrentHashMap<>();
            totalAchievements = 0;
            achievements = new ArrayList<>();
            // Register any hard coded objectives here too

            UniversalCoreRemake instance = UniversalCoreRemake.getInstance();
            File path = instance.getDataFolder();
            File file = new File(path.toString() + File.separator + "universal_objectives.yml");

            if (!path.exists())
                path.mkdirs();
            if (!file.exists())
                FileUtils.copy(instance.getResource("universal_objectives.yml"), file);

            FileConfiguration c = YamlConfiguration.loadConfiguration(file);

            for (String o : c.getConfigurationSection("Objectives").getKeys(false)) {
                String p = "Objectives." + o + ".";
                String id = c.getString(p + "ID") == null ? o : c.getString(p + "ID");
                String name = c.getString(p + "DisplayItem.DisplayName") == null ? TextUtils.capitalizeFully(id) :
                        ChatColor.translateAlternateColorCodes('&', c.getString(p + "DisplayItem.DisplayName")
                                .replace("%id_formatted%", TextUtils.capitalizeFully(id)));
                ItemBuilder item = new ItemBuilder(new ItemStack(
                        (c.getString(p + "DisplayItem.ItemMaterial") != null ?
                                Material.matchMaterial(c.getString(p + "DisplayItem.ItemMaterial")) :
                                Material.GLASS),
                        Math.max(1, c.getInt(p + "DisplayItem.Amount")),
                        (short) c.getInt(p + "DisplayItem.ItemData"))).name("&a" + name);
                List<String> rewards = c.getStringList(p + "StringRewards");
                StringReward stringReward = rewards.isEmpty() ? null : new StringReward(rewards.toArray(new String[0]));
                UniversalObjective.TaskType task = UniversalObjective.TaskType.valueOf(c.getString(p + "TaskType"));
                String[] taskInfo = c.getStringList(p + "TaskInfo").toArray(new String[0]);
                String description = c.getString(p + "Description");
                UniversalObjective.Catagory catagory = UniversalObjective.Catagory.valueOf(c.getString(p + "Catagory"));

                List<String> lore = c.getStringList(p + "DisplayItem.Lore");
                if (!lore.isEmpty())
                    for (String line : lore)
                        item.lore("&e" + line.replace("%description%", description));
                else
                    item.lore("&e" + description);


                registeredObjectivesDictionary.put(id, registeredObjectivesDictionary.size());
                UniversalObjective uo = new UniversalObjective(
                        task,
                        taskInfo,
                        id,
                        item.build(),
                        stringReward,
                        description,
                        catagory
                );
                registeredObjectives.add(uo);

                if (catagory == UniversalObjective.Catagory.ACHIEVEMENT) {
                    totalAchievements++;
                    achievements.add(uo);
                }

                Bukkit.getOnlinePlayers().forEach(this::updateTracking);
            }
        });
    }


    public void disable() {
        registeredObjectives.forEach(UniversalObjective::saveData);
    }

    /**
     * Increments by {@param amt} if {@param task} and {@param targetInfo}
     * are equal to that of a {@link UniversalObjective} tracking {@param p}
     */
    private void increment(UniversalObjective.TaskType task, String taskInfo, Player p, int amt) {
        ImmutableList.copyOf(trackedObjectives.getOrDefault(p.getUniqueId(), new ArrayList<>())).forEach((uo) -> {
            switch (task) {
                case MINE_BLOCK:
                    if (uo.getTask() == task) {
                        for (String i : uo.getTaskInfo()[2].split(","))
                            if (i.isEmpty() || i.equals(taskInfo)) {
                                int progress = uo.increment(p, amt);
                                int needed = getNeeded(uo);
                                UniversalObjectiveIncrementEvent event = new UniversalObjectiveIncrementEvent(p, uo, progress, progress - amt, needed);
                                Bukkit.getServer().getPluginManager().callEvent(event);
                                if (progress >= needed)
                                    reward(p, uo);
                                break;
                            }
                    }
                    break;

                case GIVE_TO_NPC:
                    if (uo.getTask() == task) {
                        String[] data = uo.getTaskInfo()[2].split(",");
                        if (data[0].equals(taskInfo)) { // Is the right NPC clicked? Yes? Then do item checks
                            boolean useMaterial = !(data[1].equalsIgnoreCase("UNIVERSALITEM") ||
                                    data[1].equalsIgnoreCase("UITEM"));
                            Material m = Material.matchMaterial(data[1]);
                            short itemData = 0;
                            try {
                                itemData = Short.parseShort(data[2]);
                            } catch (NumberFormatException | NullPointerException ignored) { }
                            int progress = uo.getProgress(p);

                            int amti = 0;
                            for (int i = 0; i < p.getInventory().getSize(); i++) {
                                ItemStack item = p.getInventory().getItem(i);
                                //noinspection deprecation
                                if ((useMaterial && item != null &&
                                        item.getType() == m &&
                                        item.getData().getData() == itemData) ||
                                        (!useMaterial && ItemMetadata.hasMeta(item, "ITEM") &&
                                                data[2].equalsIgnoreCase(ItemMetadata.getMeta(item, "ITEM")))) {
                                    int remove = Math.min(item.getAmount(), getNeeded(uo) - (progress + amti));
                                    amti += remove;

                                    p.getInventory().setItem(i, ItemUtils.removeItem(item, remove));

                                    if (amti + progress >= getNeeded(uo)) {
                                        uo.increment(p, amti);
                                        reward(p, uo);
                                        break;
                                    }
                                }
                            }
                            if (uo.getPlayersTracking().contains(p.getUniqueId())) {
                                uo.increment(p, amti);
                                UniversalObjectiveIncrementEvent event = new UniversalObjectiveIncrementEvent(p, uo, uo.getProgress(p), uo.getProgress(p) - amti, getNeeded(uo));
                                Bukkit.getServer().getPluginManager().callEvent(event);
                            }
                        }
                    }
                    break;

                   case REACH_LEVEL:
                    if (uo.getTask() == task && Integer.parseInt(uo.getTaskInfo()[2]) <= Integer.parseInt(taskInfo)) {
                        int progress = uo.increment(p, amt);
                        int needed = getNeeded(uo);
                        UniversalObjectiveIncrementEvent event = new UniversalObjectiveIncrementEvent(p, uo, progress, progress - amt, needed);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                        if (progress >= needed)
                            reward(p, uo);
                    }
                break;

                default:
                    if (uo.getTask() == task && uo.getTaskInfo()[2].equals(taskInfo)) {
                        int progress = uo.increment(p, amt);
                        int needed = getNeeded(uo);
                        UniversalObjectiveIncrementEvent event = new UniversalObjectiveIncrementEvent(p, uo, progress, progress - amt, needed);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                        if (progress >= needed)
                            reward(p, uo);
                    }
                    break;
            }
        });
    }

    public int getNeeded(UniversalObjective uo) {
        return Integer.parseInt(uo.getTaskInfo()[1]);
    }


    private void reward(Player p, UniversalObjective uo) {
        StringReward rewards = uo.getRewards();
        UniversalObjective.Catagory type = uo.getCategory();

        uo.removePlayer(p);
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        up.removeSelectedObjective(uo.getId()); // If objective was manually selected
        up.addCompletedObjective(uo.getId());
        up.removeObjectiveData(uo.getId()); // No longer needed data

        UniversalObjectiveCompleteEvent event = new UniversalObjectiveCompleteEvent(p, uo);
        Bukkit.getServer().getPluginManager().callEvent(event);

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
                    String[] asStrings = rewards.asStrings();
                    if (asStrings != null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&aReward:"));
                        for (String s : asStrings)
                            if (s != null)
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &8+" + s));
                        rewards.giveNoMessage(p);
                    }
                }
                break;

            case ACHIEVEMENT:
                String messageA = TextUtils.centerMessage("&e&k3&a\u2727>  Achievement Get: &6&n"
                        + TextUtils.capitalizeFully(uo.getId())
                        + "&a  <\u2727&e&k3", 154);
                p.spigot().sendMessage(new ComponentBuilder(messageA).event(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create())).create());

                if (rewards != null) {
                    String[] asStrings = rewards.asStrings();
                    if (asStrings != null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&aReward:"));
                        for (String s : asStrings)
                            if (s != null)
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &8+" + s));
                        rewards.giveNoMessage(p);
                    }
                }
                break;

            case TRIGGER:
                if (rewards != null)
                    rewards.give(p);
                break;

            default:
                String messaged = ChatColor.translateAlternateColorCodes('&',
                        "&a" + TextUtils.capitalizeFully(type.toString()) + ": &n" +
                                TextUtils.capitalizeFully(uo.getId()) + "&a completed!");
                p.spigot().sendMessage(new ComponentBuilder(messaged).event(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create())).create());

                if (rewards != null) {
                    String[] asStrings = rewards.asStrings();
                    if (asStrings != null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&aReward:"));
                        for (String s : asStrings)
                            if (s != null)
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &8+" + s));
                        rewards.giveNoMessage(p);
                    }
                }
                break;
        }
    }

    private void updateTracking(Player p) {
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        List<String> completed = up.getCompletedObjectives();
        if (up.firstJoin()) {
            up.addSelectedObjective("making_friends");
            UniversalObjective uo = registeredObjectives.get(registeredObjectivesDictionary.get("making_friends"));
            UniversalObjectiveStartEvent event = new UniversalObjectiveStartEvent(p, uo, getNeeded(uo));
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
        List<String> selected = up.getSelectedObjectives();


        Bukkit.getScheduler().runTaskAsynchronously(UniversalCoreRemake.getInstance(), () ->
                registeredObjectives.forEach((uo) -> {
                    uo.removePlayer(p);
                    if (!completed.contains(uo.getId()))
                        if (uo.getCategory() == UniversalObjective.Catagory.ACHIEVEMENT ||
                                uo.getCategory() == UniversalObjective.Catagory.TRIGGER) {
                            uo.addPlayer(p);
                            UniversalObjectiveStartEvent event = new UniversalObjectiveStartEvent(p, uo, getNeeded(uo));
                            Bukkit.getServer().getPluginManager().callEvent(event);
                        } else if (selected.contains(uo.getId())) {
                            uo.addPlayer(p);
                            UniversalObjectiveStartEvent event = new UniversalObjectiveStartEvent(p, uo, getNeeded(uo));
                            Bukkit.getServer().getPluginManager().callEvent(event);
                        }
                }));
    }

    @EventHandler(priority = EventPriority.LOW)
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
        increment(MINE_BLOCK, b, p, amt);
    }

    @EventHandler
    public void OnNPCClick(NPCRightClickEvent e) {
        Player p = e.getClicker();
        NPC npc = e.getNPC();
        String id = npc.getUniqueId().toString();
        increment(TALK_TO_NPC, id, p, 1);
        increment(GIVE_TO_NPC, id, p, 0); // 0 bc contextual
    }

    @EventHandler
    public void OnGainXP(UniversalGainXpEvent e) {
        Player p = e.getPlayer();
        increment(REACH_LEVEL, e.getLevel() + "", p, 1);
    }

    @EventHandler
    public void OnLevelUp(UniversalLevelUpEvent e) {
        Player p = e.getPlayer();
        String lvl = e.getLevel() + "";
        increment(REACH_LEVEL, lvl, p, 1);
    }

    @EventHandler
    public void OnRegionEnter(RegionEnterEvent e) {
        Player p = e.getPlayer();
        String region = e.getRegion().getId();
        increment(ENTER_REGION, region, p, 1);
    }

    @EventHandler
    public void OnRegionLeave(RegionLeftEvent e) {
        Player p = e.getPlayer();
        String region = e.getRegion().getId();
        increment(LEAVE_REGION, region, p, 1);
    }

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        increment(DIE, "GENERIC", p, 1);
    }
}