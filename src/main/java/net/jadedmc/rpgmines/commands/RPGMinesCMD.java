package net.jadedmc.rpgmines.commands;

import net.jadedmc.rpgmines.RPGMines;
import net.jadedmc.rpgmines.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RPGMinesCMD implements CommandExecutor, TabCompleter {
    private final RPGMines plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public RPGMinesCMD(RPGMines plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the command is executed.
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return If the command was successful.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Makes sure the command has an argument.
        if(args.length == 0) {
            args = new String[]{"help"};
        }

        // Get the sub command used.
        String subCommand = args[0].toLowerCase();

        switch (subCommand) {

            // Reloads all plugin configuration files.
            case "reload":
                plugin.getSettingsManager().reload();
                plugin.getRegionManager().loadRegions();
                ChatUtils.chat(sender, "&a&lRPGMines &8» &aConfiguration file reloaded successfully!");
                return true;

            // Displays the plugin version.
            case "version":
                ChatUtils.chat(sender, "&a&lRPGMines &8» &aCurrent version: &f" + plugin.getDescription().getVersion());
                return true;

            // Displays the help menu.
            default:
                ChatUtils.chat(sender, "&a&lRPGMines Commands");
                ChatUtils.chat(sender, "&a/mines reload &8» &fReloads the configuration file.");
                ChatUtils.chat(sender, "&a/mines version &8» &fDisplays the plugin version.");
                return true;
        }
    }

    /**
     * Processes command tab completion.
     * @param sender Command sender.
     * @param command Command.
     * @param label Command label.
     * @param args Arguments of the command.
     * @return Tab completion.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // Makes sure the sender is a player.
        // Required to process regions.
        if(!(sender instanceof Player)) {
            return Collections.emptyList();
        }

        // Suggest sub commands if one hasn't been selected yet.
        if(args.length < 2) {
            return Arrays.asList("help", "reload", "version");
        }

        return Collections.emptyList();
    }
}