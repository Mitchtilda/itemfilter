package com.Mitch.itemfilter.listeners;

import com.Mitch.itemfilter.ItemFilter;
import com.Mitch.itemfilter.datastore.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PickupEvent implements Listener {

    private final ItemFilter plugin;

    public PickupEvent(final ItemFilter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player))  return;

        Player player = (Player)e.getEntity();
        PlayerData playerData = plugin.getPlayerDataHandler().getPlayerData(player.getUniqueId());

        // Checks if filter is enabled
        if (!playerData.isEnabled()) return;

        // Checks if material is on the blacklist
        if (playerData.getBlacklistedMaterials().contains(e.getItem().getItemStack().getType())) {
            e.setCancelled(true);
        }
    }
}
