package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.RewardManager;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.upperlevel.spigot.book.BookUtil;

class Twitter {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        RewardManager rm = UniversalCoreRemake.getRewardManager();
        if (s instanceof Player)
            BookUtil.openPlayer((Player) s, BookUtil.writtenBook()
                .author("Corrupt Prisons")
                .title("Twitter")
                .pages(
                        new BookUtil.PageBuilder()
                                .add(new TextComponent(TextUtils.centerMessage("&b&lTWITTER", 56)))
                                .newLine()
                                .newLine()
                                .add(new TextComponent("Follow our Twitter for news and giveaways!"))
                                .newLine()
                                .newLine()
                                .newLine()
                                .newLine()
                                .newLine()
                                .newLine()
                                .add(BookUtil.TextBuilder.of(TextUtils.centerMessage("&8&l&nCLICK HERE", 56))
                                        .onClick(BookUtil.ClickAction.openUrl("https://twitter.com/CorruptPrisons"))
                                        .onHover(BookUtil.HoverAction.showText(
                                                BookUtil.TextBuilder.of("Click to visit our Twitter page!")
                                                        .color(ChatColor.YELLOW)
                                                        .build()))
                                        .build())
                                .build()
                )
                .build());
        else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bFollow our Twitter for " +
                    "news and giveaways! &ehttps://twitter.com/CorruptPrisons"));
        return true;
    }
}