package net.jadedmc.rpgmines.regions;

import net.jadedmc.rpgmines.RPGMines;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class represents a WorldGuard region with configured breakable blocks.
 */
public class Region {
    private final String id;
    private final Collection<RegionBlock> blocks = new ArrayList<>();
    private final boolean preventBuilding;
    private final boolean disableVanillaDrops;

    /**
     * Create the Region object.
     * @param plugin Instance of the plugin.
     * @param id Id of the region.
     */
    public Region(RPGMines plugin, String id) {
        this.id = id;

        preventBuilding = plugin.getSettingsManager().getConfig().getBoolean("Regions." + id + ".settings.PreventBuilding");

        if(plugin.getSettingsManager().getConfig().isSet("Regions." + id + ".settings.DisableVanillaDrops")) {
            disableVanillaDrops = plugin.getSettingsManager().getConfig().getBoolean("Regions." + id + ".settings.DisableVanillaDrops");
        }
        else {
            disableVanillaDrops = false;
        }

        ConfigurationSection section = plugin.getSettingsManager().getConfig().getConfigurationSection("Regions." + id + ".blocks");

        // Makes sure there is a Regions section in the config.
        if(section == null) {
            return;
        }

        // Loops through each block and registers it.
        for(String block : section.getKeys(false)) {
            this.blocks.add(new RegionBlock(plugin, this, block));
        }
    }

    /**
     * Get a RegionBlock based off a given block.
     * Returns null if none found.
     * @param block BLock to get region block of.
     * @return Associated RegionBlock.
     */
    public RegionBlock getBlock(Block block) {
        for(RegionBlock regionBlock : getBlocks()) {
            if(regionBlock.getMaterial() == block.getType()) {
                return regionBlock;
            }
        }

        return null;
    }

    /**
     * Get the RegionBlocks the region is made of.
     * @return All RegionBlocks.
     */
    public Collection<RegionBlock> getBlocks() {
        return blocks;
    }

    /**
     * Gets the id of the region as configured in config.yml
     * Should be the same as the WorldGuard region.
     * @return Region's id.
     */
    public String getId() {
        return id;
    }

    /**
     * Get if the plugin should disable vanilla block drops.
     * @return If the plugin should cancel block drops.
     */
    public boolean shouldDisableVanillaDrops() {
        return disableVanillaDrops;
    }

    /**
     * Get if the plugin should stop players from building in a region.
     * @return If the plugin should cancel the build event.
     */
    public boolean shouldPreventBuilding() {
        return preventBuilding;
    }
}