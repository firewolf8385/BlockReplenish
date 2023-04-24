package net.jadedmc.replenish.regions;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.jadedmc.replenish.Replenish;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class manages all configured regions.
 */
public class RegionManager {
    private final Replenish plugin;
    private final Map<String, Region> regions = new HashMap<>();
    private final Collection<Block> replacements = new ArrayList<>();

    /**
     * Creates the RegionManager.
     * @param plugin Instance of the plugin.
     */
    public RegionManager(Replenish plugin) {
        this.plugin = plugin;
        loadRegions();
    }

    /**
     * Loads all configured regions.
     */
    public void loadRegions() {
        // Clears previous regions.
        regions.clear();

        ConfigurationSection section = plugin.getSettingsManager().getConfig().getConfigurationSection("Regions");

        // Makes sure there is a Regions section in the config.
        if(section == null) {
            return;
        }

        // Loops through each region and registers it.
        for(String id : section.getKeys(false)) {
            regions.put(id.toLowerCase(), new Region(plugin, id.toLowerCase()));
        }
    }

    /**
     * Mark that a block has a pending replacement.
     * @param block Block to set as having a replacement.
     */
    public void addReplacement(Block block) {
        replacements.add(block);
    }

    /**
     * Get a region based off its id.
     * @param id Region id.
     * @return Configured region.
     */
    public Region getFromID(String id) {
        if(regions.containsKey(id)) {
            return regions.get(id);
        }

        return null;
    }

    /**
     * Get a region based on a location.
     * Null if no configured region.
     * @param location Location to check region of.
     * @return Configured region.
     */
    public Region getFromLocation(Location location) {

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        ApplicableRegionSet wgRegions = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(location));

        String id = "";
        for(ProtectedRegion wgRegion : wgRegions.getRegions()) {
            if(regions.containsKey(wgRegion.getId().toLowerCase())) {
                id = wgRegion.getId().toLowerCase();
            }
        }


        if(id.isEmpty()) {
            return null;
        }

        return getFromID(id);
    }

    /**
     * Check if a block has a pending replacement.
     * @param block Block to check.
     * @return Whether the block has a pending replacement.
     */
    public boolean hasReplacement(Block block) {
        return replacements.contains(block);
    }

    /**
     * Remove a pending replacement from a block.
     * @param block Block to remove replacement of.
     */
    public void removeReplacement(Block block) {
        replacements.remove(block);
    }
}