package net.jadedmc.rpgmines;

import net.jadedmc.rpgmines.commands.RPGMinesCMD;
import net.jadedmc.rpgmines.listeners.BlockBreakListener;
import net.jadedmc.rpgmines.listeners.BlockPlaceListener;
import net.jadedmc.rpgmines.regions.RegionManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class RPGMines extends JavaPlugin {
    private RegionManager regionManager;
    private SettingsManager settingsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        settingsManager = new SettingsManager(this);
        regionManager = new RegionManager(this);

        getCommand("rpgmines").setExecutor(new RPGMinesCMD(this));

        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
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
