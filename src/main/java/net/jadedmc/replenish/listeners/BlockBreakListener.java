package net.jadedmc.replenish.listeners;

import net.jadedmc.replenish.Replenish;
import net.jadedmc.replenish.regions.Region;
import net.jadedmc.replenish.regions.RegionBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    private final Replenish plugin;

    public BlockBreakListener(Replenish plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        // Get the region the broken block is in.
        Region region = plugin.getRegionManager().getFromLocation(event.getBlock().getLocation());

        // Exit if the region configured.
        if(region == null) {
            return;
        }

        // Get the block being broken.
        RegionBlock regionBlock = region.getBlock(event.getBlock());

        // Exit if the block is not configured.
        if(regionBlock == null) {
            return;
        }

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            // Replace the block with the replacement material.
            event.getBlock().setType(regionBlock.getReplacement());

            // Makes sure the block isn't waiting for a regeneration already.
            if(!plugin.getRegionManager().hasReplacement(event.getBlock())) {
                // Mark that the block has a regeneration coming.
                plugin.getRegionManager().addReplacement(event.getBlock());

                // Delay the regeneration to the delay length.
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    event.getBlock().setType(regionBlock.getRandomRegeneration());
                    plugin.getRegionManager().removeReplacement(event.getBlock());
                }, (long) (regionBlock.getDelay()*20.0));
            }
        }, 1);
    }
}