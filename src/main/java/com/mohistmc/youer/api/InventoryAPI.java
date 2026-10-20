package com.mohistmc.youer.api;

import com.mohistmc.youer.bukkit.inventory.YouerModsInventory;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/11 2:39:19
 */
public class InventoryAPI {

    public static boolean isMods(Inventory bukkit) {
        return ((CraftInventory) bukkit).getInventory() instanceof YouerModsInventory;
    }

    public static String getModsInvName(Inventory bukkit) {
        if (((CraftInventory) bukkit).getInventory() instanceof YouerModsInventory mohistModsInventory) {
            return mohistModsInventory.getContainer().getClass().getSimpleName();
        }
        return ((CraftInventory) bukkit).getInventory().getClass().getSimpleName();
    }
}
