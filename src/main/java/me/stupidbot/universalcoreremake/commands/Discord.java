package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.RewardManager;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.upperlevel.spigot.book.BookUtil;

@SuppressWarnings("unused")
class Discord {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        RewardManager rm = UniversalCoreRemake.getRewardManager();
        if (s instanceof Player)
            BookUtil.openPlayer((Player) s, BookUtil.writtenBook()
                .author("Corrupt Prisons")
                .title("Discord")
                .pages(
                        new BookUtil.PageBuilder()
                                .add(new TextComponent(TextUtils.centerMessage("&5&lDISCORD", 56)))
                                .newLine()
                                .newLine()
                                .add(new TextComponent("Join our Discord for updates, giveaways, and to " +
                                        "directly give feedback to admins and the community!"))
                                .newLine()
                                .newLine()
                                .newLine()
                                .add(BookUtil.TextBuilder.of(TextUtils.centerMessage("&9&l&nCLICK HERE", 56))
                                        .onClick(BookUtil.ClickAction.openUrl("https://www.discord.gg/YQsxZh8"))
                                        .onHover(BookUtil.HoverAction.showText(
                                                BookUtil.TextBuilder.of("Click for an invite link to our Discord server!")
                                                        .color(ChatColor.YELLOW)
                                                        .build()))
                                        .build())
                                .newLine()
                                .newLine()
                                .add(BookUtil.TextBuilder.of(TextUtils.centerMessage("&d&l&nSYNC ACCOUNT", 56))
                                        .onClick(BookUtil.ClickAction.runCommand("/sync Discord"))
                                        .onHover(BookUtil.HoverAction.showText(
                                                BookUtil.TextBuilder.of("Click to run /sync Discord to link your Minecraft and Discord accounts!")
                                                        .color(ChatColor.YELLOW)
                                                        .build()))
                                        .build())
                                .build()
                )
                .build());
        else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bJoin our Discord for " +
                    "updates, giveaways, and to directly give feedback to admins and the community! &ehttps://www.discord.gg/YQsxZh8"));
        return true;
    }
}