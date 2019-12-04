package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.RewardManager;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.upperlevel.spigot.book.BookUtil;

class Facebook {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        RewardManager rm = UniversalCoreRemake.getRewardManager();
        if (s instanceof Player)
            BookUtil.openPlayer((Player) s, BookUtil.writtenBook()
                .author("Corrupt Prisons")
                .title("Facebook Page")
                .pages(
                        new BookUtil.PageBuilder()
                                .add(new TextComponent(TextUtils.centerMessage("&9&lFACEBOOK PAGE", 56)))
                                .newLine()
                                .newLine()
                                .add(new TextComponent("Follow and like our Facebook page for updates and giveaways."))
                                .newLine()
                                .newLine()
                                .newLine()
                                .newLine()
                                .newLine()
                                .add(BookUtil.TextBuilder.of(TextUtils.centerMessage("&7&l&nCLICK HERE", 56))
                                        .onClick(BookUtil.ClickAction.openUrl("https://www.facebook.com/Corrupt-Prisons-101026687999857/"))
                                        .onHover(BookUtil.HoverAction.showText(
                                                BookUtil.TextBuilder.of("Click to visit our Facebook page!")
                                                        .color(ChatColor.YELLOW)
                                                        .build()))
                                        .build())
                                .build()
                )
                .build());
        else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bFollow and like our " +
                    "Facebook page for updates and giveaways &ehttps://www.facebook.com/Corrupt-Prisons-101026687999857/"));
        return true;
    }
}