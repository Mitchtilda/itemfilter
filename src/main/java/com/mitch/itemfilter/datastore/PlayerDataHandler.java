package com.Mitch.itemfilter.datastore;

import com.Mitch.itemfilter.ItemFilter;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerDataHandler {

    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    public Map<UUID, BukkitTask> purgeDataTask = new HashMap<>();


    public PlayerData getPlayerData(UUID uuid) {
        if (!exists(uuid)) {
            // If it's here and been directly called - that means something is accessing it before it has had time to load!
            // E.g Reload command has cleared the map of data.
            // To prevent NPEs it's loaded here, SYNCHRONOUSLY! So shouldn't be used heavily.
            new File(ItemFilter.getInstance().getDataFolder() + "/playerdata/").mkdir();

            final File jsonData = new File(ItemFilter.getInstance().getDataFolder() + "/playerdata/" + uuid.toString() + ".json");
            try {
                jsonData.createNewFile();
            } catch (IOException io) {
                io.printStackTrace();
            }
            PlayerData data = ItemFilter.getInstance().getFileUtil().loadPlayerData(jsonData);

            if (data == null) {
                PlayerData pData = new PlayerData(uuid);
                data = pData;
                ItemFilter.getInstance().getFileUtil().save(pData, jsonData);
            }

            playerDataMap.put(uuid, data);
        }
        return playerDataMap.get(uuid);
    }

    public void removeLocalData(PlayerData data) {
        playerDataMap.remove(data.getUuid());
    }

    public boolean exists(UUID uuid) {
        return playerDataMap.containsKey(uuid);
    }

}
