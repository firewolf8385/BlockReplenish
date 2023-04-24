package net.jadedmc.replenish.listeners;

import net.jadedmc.replenish.Replenish;
import net.jadedmc.replenish.regions.Region;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * This listens to the BlockPlaceEvent event, which is called every time a player places a block.
 * We use this to prevent building in configured regions.
 */
public class BlockPlaceListener implements Listener {
    private final Replenish plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public BlockPlaceListener(Replenish plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event BlockPlaceEvent.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // Ignore for players in creative mode.
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }

        // Get the region the placed block is in.
        Region region = plugin.getRegionManager().getFromLocation(event.getBlock().getLocation());

        // Exit if the region configured.
        if(region == null) {
            return;
        }

        // Exit if the plugin should not stop building.
        if(!region.shouldPreventBuilding()) {
            return;
        }

        // Cancels the event.
        event.setCancelled(true);
    }
}
