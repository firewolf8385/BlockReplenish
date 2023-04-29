package net.jadedmc.rpgmines.regions;

import net.jadedmc.rpgmines.RPGMines;
import net.jadedmc.rpgmines.utils.WeightedRandom;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a type of block that can be broken in a region.
 * It stores its replacement, as well as the regeneration options and delay.
 */
public class RegionBlock {
    private final Material material;
    private final Material replacement;
    private final Map<Material, Double> regeneration = new HashMap<>();
    private final int delay;

    /**
     * Creates the region block.
     * @param plugin Instance of the plugin.
     * @param region Region the RegionBlock belongs to.
     * @param material Material of the RegionBlock.
     */
    public RegionBlock(RPGMines plugin, Region region, String material) {
        System.out.println(material);
        this.material = Material.valueOf(material);
        this.delay = plugin.getSettingsManager().getConfig().getInt("Regions." + region.getId() + ".blocks." + material + ".delay");
        this.replacement = Material.valueOf(plugin.getSettingsManager().getConfig().getString("Regions." + region.getId() + ".blocks." + material + ".replacement"));

        ConfigurationSection regenerationSection = plugin.getSettingsManager().getConfig().getConfigurationSection("Regions." + region.getId() + ".blocks." + material + ".regeneration");

        if(regenerationSection == null) {
            return;
        }

        // Load the regeneration options.
        for(String regenerationMaterial : regenerationSection.getKeys(false)) {
            regeneration.put(Material.valueOf(regenerationMaterial), plugin.getSettingsManager().getConfig().getDouble("Regions." + region.getId() + ".blocks." + material + ".regeneration." + regenerationMaterial));
        }
    }

    /**
     * Get the delay between block break and regeneration.
     * @return Regeneration delay.
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Get the initial material of the block.
     * @return Initial material.
     */
    public Material getMaterial() {
        return  material;
    }

    /**
     * Get a random material from the regeneration list.
     * @return Random regeneration material.
     */
    public Material getRandomRegeneration() {
        WeightedRandom<Material> weightedRandom = new WeightedRandom<>();

        for(Material material : regeneration.keySet()) {
            weightedRandom.addEntry(material, regeneration.get(material));
        }

        return weightedRandom.getRandom();
    }

    /**
     * Get the replacement material of the block.
     * @return Replacement material.
     */
    public Material getReplacement() {
        return replacement;
    }
}