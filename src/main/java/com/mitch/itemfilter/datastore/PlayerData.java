package com.Mitch.itemfilter.datastore;

import com.Mitch.itemfilter.ItemFilter;
import lombok.Getter;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {
    /* Generics */
    @Getter private final UUID uuid;
    @Getter private final List<Material> blacklistedMaterials = new ArrayList<>();
    @Getter private boolean enabled = false;


    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        ItemFilter.getInstance().getFileUtil().save(this, getJson());
    }

    public File getJson() {
        return new File(ItemFilter.getInstance().getDataFolder() + "/playerdata/" + uuid.toString() + ".json");
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
        ItemFilter.getInstance().getFileUtil().save(this, getJson());
    }

    /**
     * Removes a blacklisted item from the list
     *
     * @param material Material to remove from list
     * @return if list contained the material
     */
    public boolean removeBlackListedMaterial(Material material) {
        boolean result = blacklistedMaterials.remove(material);
        ItemFilter.getInstance().getFileUtil().save(this, getJson());
        return result;
    }

    /**
     * Adds a material to the blacklist
     *
     * @param material Material to add to the list
     * @return True if successfully added, false if already in list
     */
    public boolean addToBlackList(Material material) {
        if (isBlackListed(material)) return false;

        blacklistedMaterials.add(material);
        ItemFilter.getInstance().getFileUtil().save(this, getJson());
        return true;
    }

    /**
     * Checks if the material is blacklisted or not
     *
     * @param material Material to check
     * @return if it's blacklisted
     */
    public boolean isBlackListed(Material material) {
        return blacklistedMaterials.contains(material);
    }
}
