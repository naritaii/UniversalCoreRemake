package me.stupidbot.universalcoreremake.managers;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.items.UniversalItem;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import me.stupidbot.universalcoreremake.utilities.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StatsManager implements Listener {
    public final Map<UUID, List<ItemStack>> equipment = new ConcurrentHashMap<>();

    public StatsManager() {
        Bukkit.getScheduler().runTaskTimer(UniversalCoreRemake.getInstance(), () ->
                Bukkit.getOnlinePlayers().forEach(p -> {
                    List<ItemStack> currentEquipment = getEquipment(p);
                    List<ItemStack> cachedEquipment = equipment.getOrDefault(p.getUniqueId(), Collections.emptyList());

                    if (cachedEquipment.size() != currentEquipment.size() || !cachedEquipment.containsAll(currentEquipment) ||
                    !currentEquipment.containsAll(cachedEquipment)) {
                        equipment.put(p.getUniqueId(), currentEquipment);
                        recalculateStats(p);
                    }
                }), 0, 10);
        modifyOverStack(net.minecraft.server.v1_8_R3.Item.getById(282), 64); // Mushroom Soup
        modifyOverStack(net.minecraft.server.v1_8_R3.Item.getById(413), 64); // Rabbit Soup
    }

    private List<ItemStack> getEquipment(Player p) {
        List<ItemStack> equipment = new ArrayList<>(Arrays.asList(p.getEquipment().getArmorContents()));
        if (p.getItemInHand() != null &&
                p.getItemInHand().getType() != Material.AIR &&
                !p.getItemInHand().getType().toString().endsWith("_HELMET") &&
                !p.getItemInHand().getType().toString().endsWith("_CHESTPLATE") &&
                !p.getItemInHand().getType().toString().endsWith("_LEGGINGS") &&
                !p.getItemInHand().getType().toString().endsWith("_BOOTS") &&
                p.getItemInHand().getType() != Material.SKULL_ITEM &&
                !ItemMetadata.hasMeta(p.getItemInHand(), "HAT"))
            equipment.add(p.getItemInHand());
        return equipment;
    }

    private final Set<UUID> slimeSet = new HashSet<>();
    private void recalculateStats(Player p) {
        if (equipment.get(p.getUniqueId()).containsAll(Arrays.asList(UniversalItem.SLIME_HELMET,
                UniversalItem.SLIME_CHESTPLATE, UniversalItem.SLIME_LEGGINGS, UniversalItem.SLIME_BOOTS))) {
            if (!slimeSet.contains(p.getUniqueId())) {
                slimeSet.add(p.getUniqueId());
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 0,
                        true, true), true);
            }
        }  else if (slimeSet.contains(p.getUniqueId())) {
            slimeSet.remove(p.getUniqueId());
            p.removePotionEffect(PotionEffectType.JUMP);
        }

        updateHealth(p);
        updateStamina(p);
        p.setWalkSpeed(getSpeed(p));
    }


    @EventHandler(priority = EventPriority.LOW)
    public void OnFoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void OnPlayerItemConsume(PlayerItemConsumeEvent e) {
        try {
            ItemStack i = e.getItem();
            if (i.getType() != Material.POTION) {
                Player p = e.getPlayer();
                e.setCancelled(true);
                p.setItemInHand(ItemUtils.removeItem(p.getItemInHand(), 1));
                int stamina = BaseFoodStamina.valueOf(i.getType().toString()).getBaseFoodStamina();
                addStamina(p, stamina);
                TextUtils.sendActionbar(p, "&3Stamina &a+" + stamina +
                        " &e(" + getStamina(p) + "/" + getMaxStamina(p) + ")");
            }
        } catch (IllegalArgumentException ex) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);

        equipment.put(p.getUniqueId(), getEquipment(p));
        recalculateStats(p);

        if (up.firstJoin()) {
            p.setHealth(getMaxHealth(p));
            setStamina(p, getMaxStamina(p));
            // Making player type "/spawn" and executing messages from a different thread as temporary bug fixes
            p.performCommand("spawn");
            UniversalCoreRemake.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(UniversalCoreRemake.getInstance(), () -> {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC"));

                TextUtils.sendCenteredMessage(p, "&lWelcome to Corrupt Prisons,&7 "
                        + up.getNameColor() + p.getName() + "&f&l!");
                p.sendMessage("");
                TextUtils.sendCenteredMessage(p, "&eTalk to &aQuest Master&e to get started, the Corrupt");
                TextUtils.sendCenteredMessage(p, "&euniverse has many lands to discover, strange creatures");
                TextUtils.sendCenteredMessage(p, "&eto find, secrets to uncover, and peculiar characters to");
                TextUtils.sendCenteredMessage(p, "&emeet! Collect resources, upgrade your gear, explore a");
                TextUtils.sendCenteredMessage(p, "&ewhole new planet, and complete quests to advance your");
                TextUtils.sendCenteredMessage(p, "&eway through Corrupt Prisons!");
                TextUtils.sendCenteredMessage(p, "&bHave fun!");

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC"));
            });
        } else
            try {
                if (new Date().getTime() - UniversalPlayer.getSimpleDateFormat().parse(up.getLastPlayed())
                        .getTime() >= 6.48e+7) { // 18 hours
                    up.setStamina(getMaxStamina(p));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&a&lALL STAMINA RECOVERED!&7&o All stamina is recovered when you've been " +
                                    "offline for 18+ hours."));
                }
            } catch (ParseException pe) {
                pe.printStackTrace();
            }

    }

    @EventHandler
    public void OnQuit(PlayerQuitEvent e) {
        equipment.remove(e.getPlayer().getUniqueId());
    }

    private void addStamina(Player p, int i) {
        setStamina(p, Math.min(getStamina(p) + i, getMaxStamina(p)));
    }

    public void removeStamina(Player p, int i) {
        setStamina(p, Math.max(getStamina(p) - i, 0));
    }

    public int getStamina(Player p) {
        return UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).getStamina();
    }

    private void setStamina(Player p, int i) {
        UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).setStamina(i);
        updateStamina(p);
    }

    private void updateStamina(Player p) {
        p.setFoodLevel((int) Math.floor((getStamina(p) * 20d) / getMaxStamina(p)));
    }

    public int getMaxStamina(Player p) {
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        int boost = 25;
        for (ItemStack i : equipment.get(p.getUniqueId())) {
            Map<String, String> meta = ItemMetadata.getMeta(i);
            if (meta.containsKey("STAMINA"))
                boost += Integer.parseInt(meta.get("STAMINA"));
        }

        int staminaPerLevel = 5;
        return ((up.getLevel() - 1) * staminaPerLevel) + boost;
    }

    public float getDisplayedSpeed(Player p) {
        float base = getSpeed(p);

        for (PotionEffect effect : p.getActivePotionEffects())
            if (effect.getType() == PotionEffectType.SPEED) {
                base += (effect.getAmplifier() + 1) * 0.2f;
                break;
            }

        if (slimeSet.contains(p.getUniqueId()))
            base += 0.15f;

        return (base + 0.8f) * 100;
    }

    private float getSpeed(Player p) {
        float spd = 0.2f;

        for (ItemStack i : equipment.get(p.getUniqueId())) {
            Map<String, String> meta = ItemMetadata.getMeta(i);
            if (meta.containsKey("SPEED"))
                spd += Float.parseFloat(meta.get("SPEED"));
        }

        if (slimeSet.contains(p.getUniqueId()))
            spd += 0.15f;

        return spd;
    }

    private double getMaxHealth(Player p) {
        double boost = 0d;
        for (ItemStack i : equipment.get(p.getUniqueId())) {
            Map<String, String> meta = ItemMetadata.getMeta(i);
            if (meta.containsKey("HEALTH"))
                boost += Double.parseDouble(meta.get("HEALTH"));
        }

        return 100d + boost;
    }

    private void updateHealth(Player p) {
        double maxHealth = getMaxHealth(p);
        p.setMaxHealth(maxHealth);
        p.setHealthScale(Math.floor(maxHealth / 10) * 2);
    }

    private final Random r = new Random();

    @EventHandler
    public void OnDamageEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            Map<String, String> m = ItemMetadata.getMeta(p.getItemInHand());
            if (m.containsKey("DAMAGE")) {
                double damage;
                String[] range = m.get("DAMAGE").split("_");
                if (range.length == 1)
                    damage = Double.parseDouble(range[0]);
                else {
                    double rangeMin = Double.parseDouble(range[0]);
                    double rangeMax = Double.parseDouble(range[1]);
                    damage = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
                }
                e.setDamage(damage);
            }
        } else if (e.getDamager() instanceof Arrow) {
            Arrow a = (Arrow) e.getDamager();
            ItemStack bow = (ItemStack) a.getMetadata("BOW").get(0).value();
            Map<String, String> m = ItemMetadata.getMeta(bow);
            if (m.containsKey("DAMAGE")) {
                double damage;
                String[] range = m.get("DAMAGE").split("_");
                if (range.length == 1)
                    damage = Double.parseDouble(range[0]);
                else {
                    double rangeMin = Double.parseDouble(range[0]);
                    double rangeMax = Double.parseDouble(range[1]);
                    damage = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
                }
                e.setDamage(damage);
            }
        }

        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            e.setDamage(calculateDamage(p, e.getDamage()));
        }
    }

    @EventHandler
    public void OnFireArrow(EntityShootBowEvent e) {
        e.getProjectile().setMetadata("BOW", new FixedMetadataValue(UniversalCoreRemake.getInstance(), e.getBow()));
    }

    private double calculateDamage(Player p, double damage) {
        int defensePoints = getDefense(p);
        return damage * (1 - (defensePoints / 100d));
    }

    public int getDefense(Player p) {
        int defensePoints = 0;
        for (ItemStack i : equipment.get(p.getUniqueId())) {
            Map<String, String> meta = ItemMetadata.getMeta(i);
            if (meta.containsKey("DEFENSE"))
                defensePoints += Integer.parseInt(meta.get("DEFENSE"));
        }

        return defensePoints;
    }

    @SuppressWarnings("SameParameterValue")
    private void modifyOverStack(net.minecraft.server.v1_8_R3.Item item, int amount) {
        try {
            Field field = net.minecraft.server.v1_8_R3.Item.class.getDeclaredField("maxStackSize");
            field.setAccessible(true);
            field.setInt(item, amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public enum BaseFoodStamina {
        ROTTEN_FLESH(5), APPLE(20), BREAD(50), RAW_FISH(75), MUSHROOM_SOUP(125),
        RAW_CHICKEN(175);

        final int baseFoodStamina;

        BaseFoodStamina(int baseFoodStamina) {
            this.baseFoodStamina = baseFoodStamina;
        }

        public int getBaseFoodStamina() {
            return baseFoodStamina;
        }
    }
}