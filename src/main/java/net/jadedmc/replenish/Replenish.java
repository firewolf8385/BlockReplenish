package net.jadedmc.replenish;

import org.bukkit.plugin.java.JavaPlugin;

public final class Replenish extends JavaPlugin {
    private SettingsManager settingsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        settingsManager = new SettingsManager(this);
    }

    /**
     * Gets the current settings manager instance.
     * @return Settings Manager.
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}
