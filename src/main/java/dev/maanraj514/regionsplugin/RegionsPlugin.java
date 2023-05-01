package dev.maanraj514.regionsplugin;

import dev.maanraj514.regionsplugin.commands.RegionMenuCommand;
import dev.maanraj514.regionsplugin.commands.RegionsReloadCommand;
import dev.maanraj514.regionsplugin.commands.RegionsSetupCommand;
import dev.maanraj514.regionsplugin.listeners.RegionListener;
import dev.maanraj514.regionsplugin.managers.RegionManager;
import dev.maanraj514.regionsplugin.object.Bound;
import dev.maanraj514.regionsplugin.utils.RegionFile;
import me.kodysimpson.simpapi.command.CommandManager;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static me.kodysimpson.simpapi.colors.ColorTranslator.translateColorCodes;
import static me.kodysimpson.simpapi.colors.MessageUtils.msgPlayer;

public final class RegionsPlugin extends JavaPlugin {

    private static RegionsPlugin instance;

    private final RegionManager regionManager = new RegionManager();

    private final Map<UUID, Bound> regionSetup = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("The plugin has started!");

        saveDefaultConfig();

        MenuManager.setup(getServer(), this);

        try{
            CommandManager.createCoreCommand(this, "regions", "The main region command", "/regions", (sender, subCommandList) -> {
                if (!(sender instanceof Player)){
                    sender.sendMessage(translateColorCodes(getMessageNotAPlayerMessage()));
                    return;
                }
                Player player = (Player) sender;
                msgPlayer(player, "&e-------------------------------");
                subCommandList.forEach(subCommand -> {
                    msgPlayer(player, "&a" + subCommand.getSyntax() + " - &f" + subCommand.getDescription());
                });
                msgPlayer(player, "&e-------------------------------");
            }, Arrays.asList("regions", "r"), RegionsSetupCommand.class, RegionsReloadCommand.class, RegionMenuCommand.class);
        } catch (NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }

        getServer().getPluginManager().registerEvents(new RegionListener(), this);

        loadRegions();
    }

    @Override
    public void onDisable() {
        getLogger().info("The plugin has disabled!");
    }

    public String getMessageNotAPlayerMessage(){
        if (getConfig().getString("not-a-player-message") != null){
            return getConfig().getString("not-a-player-message");
        }
        return "&cDefault";
    }

    public void loadRegions() {
        File regionFolder = new File(getDataFolder(), "Regions");
        if (!regionFolder.exists()){
            regionFolder.mkdirs();
        }

        if (regionFolder.listFiles() != null){
            for (File file : regionFolder.listFiles()) {
                if (file != null){
                    try{
                        RegionFile regionFile = new RegionFile(file.getName().replace(".yml", ""));
                        regionFile.createRegion();
                    } catch (IOException | InvalidConfigurationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static RegionsPlugin getInstance() {
        return instance;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public Map<UUID, Bound> getRegionSetup() {
        return regionSetup;
    }
}