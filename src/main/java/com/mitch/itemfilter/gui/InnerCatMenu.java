package com.Mitch.itemfilter.gui;

import com.Mitch.itemfilter.ItemFilter;
import com.Mitch.itemfilter.datastore.PlayerData;
import com.Mitch.itemfilter.utils.Pagination;
import com.Mitch.itemfilter.utils.StringUtils;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.BaseGui;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class InnerCatMenu {

    private final BaseGui gui;
    private final List<Material> materials;
    private final ItemFilter plugin;
    int page;
    private final String name;


    public InnerCatMenu(final ItemFilter plugin, String name, List<Material> items, int page) {
        this.materials = items;
        this.plugin = plugin;
        this.page = page;
        this.name = name;

        gui = new Gui(plugin, 6, StringUtils.colour(plugin.getConfig().getString("category-menu.items." + name + ".title")));
        gui.setDefaultClickAction(event -> event.setCancelled(true));
    }

    public void open(Player player) {

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            List<GuiItem> itemList = new ArrayList<>();
            PlayerData playerData = plugin.getPlayerDataHandler().getPlayerData(player.getUniqueId());
            ConfigurationSection will = plugin.getConfig().getConfigurationSection("format.will");
            ConfigurationSection willNot = plugin.getConfig().getConfigurationSection("format.willNot");

            materials.forEach(material -> {
                // Assigns correct formatting to materials
                if (playerData.getBlacklistedMaterials().contains(material)) {
                    itemList.add(willItems(material, willNot, playerData));
                } else {
                    itemList.add(willItems(material, will, playerData));
                }
            });

            Pagination<GuiItem> item = new Pagination<>(45, itemList);
            gui.addItem(item.getPage(page).toArray(new GuiItem[0]));

            gui.getFiller().fill(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));

            if ((page - 1) >= 0)
                gui.setItem(6,1, new GuiItem(new ItemBuilder(Material.ARROW).setName(StringUtils.colour(plugin.getMessages().prevPage)).build(), event -> {
                    event.setCancelled(true);
                    newInstance(page - 1).open(player);
                }));

            if ((page + 1) < item.totalPages())
                gui.setItem(6,9, new GuiItem(new ItemBuilder(Material.ARROW).setName(StringUtils.colour(plugin.getMessages().nextPage)).build(), event -> {
                    event.setCancelled(true);
                    newInstance(page + 1).open(player);

                }));

            Bukkit.getScheduler().runTask(plugin, () -> gui.open(player));
        });

    }

    private InnerCatMenu newInstance(int page) {
        return new InnerCatMenu(plugin, name, materials, page);
    }

    private GuiItem willItems(Material material, ConfigurationSection will, PlayerData forWhomData) {
        ItemBuilder item =  new ItemBuilder(material)
                .setName(StringUtils.colour(will.getString("prefix") + material))
                .setLore(StringUtils.colourList(will.getStringList("lore")));

        if (forWhomData.isBlackListed(material)) item.glow(true);

        return new GuiItem(item.build(), event -> {

            // Checks if clicker was a player
            if (!(event.getWhoClicked() instanceof Player)) return;

            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();

            // Play clicky sound :)))))
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);

            if (forWhomData.isBlackListed(material)) {
                forWhomData.removeBlackListedMaterial(material);
                gui.updateItem(slot, willItems(material, plugin.getConfig().getConfigurationSection("format.will"), forWhomData));
            } else {
                forWhomData.addToBlackList(material);
                gui.updateItem(slot, willItems(material, plugin.getConfig().getConfigurationSection("format.willNot"), forWhomData));
            }

        });
    }
}
