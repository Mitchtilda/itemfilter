package com.Mitch.itemfilter.utils;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemUtils {


    public static ItemStack getItemStack(ConfigurationSection section) {
        Material material = Material.valueOf(section.getString("material"));
        if (material.equals(Material.AIR)) return new ItemStack(Material.AIR);
        ItemBuilder builder = new ItemBuilder(material)
                .setName(StringUtils.colour(section.getString("title")))
                .setLore(StringUtils.colourList(section.getStringList("lore")).toArray(new String[]{}))
                .setAmount(section.getInt("amount"))
                .glow(section.getBoolean("glow"));

        if (Material.valueOf(section.getString("material")).equals(Material.PLAYER_HEAD)) {
            if (section.getString("skull-texture") != null)
                builder.setSkullTexture(section.getString("skull-texture"));
        }

        ItemStack stack = builder.build();
        int modelData = section.getInt("custom-model-data");
        if (modelData != 0) {
            ItemMeta meta = stack.getItemMeta();
            meta.setCustomModelData(section.getInt("custom-model-data"));
            meta.setUnbreakable(true); // On versions 1.11 and above

            stack.setItemMeta(meta);
        }


        return stack;
    }

    public static boolean isValidMaterial(String material) {
        Material mat = null;
        try {
            mat = Material.valueOf(material);
        } catch (IllegalArgumentException ignore) { }

        return mat != null;
    }
}
