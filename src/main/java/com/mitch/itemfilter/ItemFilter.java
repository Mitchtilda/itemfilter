package com.Mitch.itemfilter;

import com.Mitch.itemfilter.commands.FilterCmd;
import com.Mitch.itemfilter.datastore.PlayerDataHandler;
import com.Mitch.itemfilter.gui.GuiHandler;
import com.Mitch.itemfilter.listeners.LoadUnloadData;
import com.Mitch.itemfilter.listeners.PickupEvent;
import com.Mitch.itemfilter.utils.FileUtil;
import lombok.Getter;
import me.mattstudios.mf.base.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class ItemFilter extends JavaPlugin {

    @Getter private CommandManager commandManager;
    @Getter private FileUtil fileUtil;
    @Getter private PlayerDataHandler playerDataHandler;
    @Getter private GuiHandler guiHandler;
    @Getter private Messages messages;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        register();

        getServer().getPluginManager().registerEvents(new LoadUnloadData(this), this);
        getServer().getPluginManager().registerEvents(new PickupEvent(this), this);

    }

    public void register() {
        this.fileUtil = new FileUtil();
        this.guiHandler = new GuiHandler(this);
        this.messages = getFileUtil().getFile(Messages.class).exists() ? getFileUtil().load(Messages.class) : new Messages();
        fileUtil.save(messages);

        commandManager = new CommandManager(this);
        // Commands
        commandManager.register(new FilterCmd(this));
        playerDataHandler = new PlayerDataHandler();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void error(String msg) {
        getLogger().log(Level.SEVERE, msg);
    }

    public void warn(String msg) {
        getLogger().log(Level.WARNING, msg);
    }

    public void info(String msg) {
        getLogger().log(Level.INFO, msg);
    }

    public static ItemFilter getInstance() { return getPlugin(ItemFilter.class); }
}
