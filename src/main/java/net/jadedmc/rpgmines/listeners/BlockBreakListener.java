package net.jadedmc.rpgmines.listeners;

import net.jadedmc.rpgmines.RPGMines;
import net.jadedmc.rpgmines.regions.Region;
import net.jadedmc.rpgmines.regions.RegionBlock;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;
import java.util.Collection;

/**
 * This listens to the BlockBreakEvent event, which is called every time a player breaks a block.
 * We use this to process blocks broken in configured regions.
 */
public class BlockBreakListener implements Listener {
    private final RPGMines plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public BlockBreakListener(RPGMines plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event PortalEnterEvent.
     */
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        // Exit if the player is in creative mode.
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }

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

            // Prevent breaking non-configured blocks if prevent building is set to true.
            if(region.shouldPreventBuilding()) {
               event.setCancelled(true);
            }

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

        // Process stacked crops, like bamboo, cactus, and sugar cane.
        Collection<Material> crops = Arrays.asList(Material.BAMBOO, Material.CACTUS, Material.SUGAR_CANE);
        Block above = event.getBlock().getRelative(BlockFace.UP);
        if(crops.contains(event.getBlock().getType()) && crops.contains(above.getType())) {
            event.getPlayer().breakBlock(above);
        }
    }
}