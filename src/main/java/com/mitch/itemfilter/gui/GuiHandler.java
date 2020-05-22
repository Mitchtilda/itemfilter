package com.Mitch.itemfilter.gui;

import com.Mitch.itemfilter.ItemFilter;
import lombok.Getter;
import me.mattstudios.mfgui.gui.guis.BaseGui;
import java.util.HashMap;
import java.util.Map;

public class GuiHandler {

    private final ItemFilter plugin;
    @Getter
    private final Map<String, BaseGui> guiList = new HashMap<>();
    @Getter
    private final CategoryMenu categoryMenu;

    public GuiHandler(final ItemFilter plugin) {
        this.plugin = plugin;
        categoryMenu = new CategoryMenu(plugin);


    }
}
