package com.Mitch.itemfilter.gui;

import com.Mitch.itemfilter.ItemFilter;
import com.Mitch.itemfilter.utils.ItemUtils;
import com.Mitch.itemfilter.utils.StringUtils;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class CategoryMenu {

    private final Gui gui;

    public CategoryMenu(final ItemFilter plugin) {

        gui = new Gui(plugin, plugin.getConfig().getInt("category-menu.size"), StringUtils.colour(plugin.getConfig().getString("category-menu.title")));
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.getFiller().fill(new GuiItem(ItemUtils.getItemStack(plugin.getConfig().getConfigurationSection("category-menu.filler"))));

        // For each category in config, we make a path and destination for the category.
        for (String key : plugin.getConfig().getConfigurationSection("category-menu.items").getKeys(false)) {

            ConfigurationSection section = plugin.getConfig().getConfigurationSection("category-menu.items." + key);
            ItemStack item = ItemUtils.getItemStack(section);
            item = new ItemBuilder(item).setName(StringUtils.colour(section.getString("title"))).build();
            int slot = section.getInt("slot");

            gui.setItem(slot, new GuiItem(item, event -> {
                if (!(event.getWhoClicked() instanceof Player)) return;

                Player player = (Player) event.getWhoClicked();

                List<Material> materials = new ArrayList<>();

                // Validates list
                section.getStringList("items").forEach(material -> {
                    if (ItemUtils.isValidMaterial(material))
                        materials.add(Material.valueOf(material));
                    else
                        ItemFilter.getInstance().warn(String.format("Material: %s is invalid for path %s! Ignoring...", material, key));
                });
                new InnerCatMenu(plugin, key, materials, 0).open(player);

            }));
        }
    }

    public void open(final Player player) {
        gui.open(player);
    }
}
