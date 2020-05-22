package com.Mitch.itemfilter.listeners;

import com.Mitch.itemfilter.ItemFilter;
import com.Mitch.itemfilter.datastore.PlayerData;
import com.Mitch.itemfilter.datastore.PlayerDataHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public final class LoadUnloadData implements Listener {

    private final ItemFilter plugin;

    public LoadUnloadData(final ItemFilter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPreJoin(AsyncPlayerPreLoginEvent e) {
        if (!(e.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED)) return;

        PlayerDataHandler handler = plugin.getPlayerDataHandler();
        UUID uuid = e.getUniqueId();

        if (handler.purgeDataTask.containsKey(uuid)) {
            handler.purgeDataTask.get(uuid).cancel();
            handler.purgeDataTask.remove(uuid);
            return;
        }
        // Load PlayerData
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                plugin.getPlayerDataHandler().getPlayerData(e.getUniqueId()));
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        PlayerData data = plugin.getPlayerDataHandler().getPlayerData(uuid);

        BukkitTask id =  Bukkit.getScheduler().runTaskLater(plugin, () -> {

            if (data == null) return;

            plugin.getPlayerDataHandler().purgeDataTask.remove(uuid);
            plugin.getPlayerDataHandler().removeLocalData(data);

        }, 20L * 120);

        // Stores the Data for 2 Minutes and then Deletes it from LocalCache if player has not rejoined to prevent login spam crashes
        plugin.getPlayerDataHandler().purgeDataTask.put(uuid, id);
    }

}
