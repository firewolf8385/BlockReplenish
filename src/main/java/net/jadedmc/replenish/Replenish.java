package net.jadedmc.replenish;

import net.jadedmc.replenish.commands.ReplenishCMD;
import net.jadedmc.replenish.listeners.BlockBreakListener;
import net.jadedmc.replenish.regions.RegionManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Replenish extends JavaPlugin {
    private RegionManager regionManager;
    private SettingsManager settingsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        settingsManager = new SettingsManager(this);
        regionManager = new RegionManager(this);

        getCommand("replenish").setExecutor(new ReplenishCMD(this));

        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
    }

    /**
     * Gets the current region manager instance.
     * @return Region manager.
     */
    public RegionManager getRegionManager() {
        return regionManager;
    }

    /**
     * Gets the current settings manager instance.
     * @return Settings Manager.
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}
