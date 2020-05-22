package com.Mitch.itemfilter.commands;

import com.Mitch.itemfilter.ItemFilter;
import com.Mitch.itemfilter.States;
import com.Mitch.itemfilter.datastore.PlayerData;
import com.Mitch.itemfilter.utils.StringUtils;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@Command("itemfilter")
@Alias("if")
public final class FilterCmd extends CommandBase {

    private final ItemFilter plugin;

    public FilterCmd(final ItemFilter plugin) {
        this.plugin = plugin;
    }

    @Default
    public void openFilter(Player player) {
        player.sendMessage(StringUtils.colourList(Arrays.asList(plugin.getMessages().help_cmd)).toArray(new String[0]));
    }

    @Permission("itemfilter.use")
    @SubCommand("edit")
    public void editCmd(Player player) {
        plugin.getGuiHandler().getCategoryMenu().open(player);
    }

    @Permission("itemfilter.use")
    @SubCommand("toggle")
    public void toggleCmd(Player player) {
        PlayerData data = plugin.getPlayerDataHandler().getPlayerData(player.getUniqueId());
        data.setEnabled(!data.isEnabled());

        String msg = plugin.getMessages().toggle;
        msg = msg.replaceAll("@state", data.isEnabled() ? "§a" + States.ON : "§c" + States.OFF);

        player.sendMessage(StringUtils.colour(msg));
    }

    @Permission("itemfilter.reload")
    @SubCommand("reload")
    public void reloadCmd(CommandSender sender) {
        plugin.reloadConfig();
        plugin.register();

        sender.sendMessage(StringUtils.colour(plugin.getMessages().reloaded));
    }

}
