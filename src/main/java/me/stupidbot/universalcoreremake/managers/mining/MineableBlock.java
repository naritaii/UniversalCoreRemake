package me.stupidbot.universalcoreremake.managers.mining;

import org.bukkit.Material;

public class MineableBlock {
    private final Material type;
    private final float durability;
    private final float regenerateTime;
    private final Material enhanceBlock;
    private final float enhanceChance;
    private final int baseXp;
    private final Material loot;
    private final int staminaBaseUsage;
    private final MiningManager.BreakBehavior onBreak;

    /**
     * @param durability       Time in seconds block takes to mine with hand and no "modifiers".
     * @param regenerateTime   Time in seconds block takes to regenerate.
     * @param enhanceBlock     Block to turn into when enhanced.
     * @param enhanceChance    Chance of turning block into enhanceBlock.
     * @param baseXp           XP given without any multipliers.
     * @param loot             Item dropped.
     * @param staminaBaseUsage Stamina taken without any multipliers.
     * @param onBreak          What to do when (@link MineableBlock) is broken.
     */
    MineableBlock(Material type, float durability, float regenerateTime, Material enhanceBlock, float enhanceChance,
                  int baseXp, Material loot, int staminaBaseUsage, MiningManager.BreakBehavior onBreak) {
        this.type = type;
        this.durability = durability;
        this.regenerateTime = regenerateTime;
        this.enhanceBlock = enhanceBlock;
        this.enhanceChance = enhanceChance;
        this.baseXp = baseXp;
        this.loot = loot;
        this.staminaBaseUsage = staminaBaseUsage;
        this.onBreak = onBreak;
    }

    public Material getType() {
        return type;
    }

    float getDurability() {
        return durability;
    }

    float getGetRegenerateTime() {
        return regenerateTime;
    }

    Material getEnhanceBlock() {
        return enhanceBlock;
    }

    float getEnhanceChance() {
        return enhanceChance;
    }

    int getBaseXp() {
        return baseXp;
    }

    Material getLoot() {
        return loot;
    }

    MiningManager.BreakBehavior getOnBreak() {
        return onBreak;
    }

    int getBaseStaminaUsage() {
        return staminaBaseUsage;
    }
}
