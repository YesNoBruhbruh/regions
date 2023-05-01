package dev.maanraj514.regionsplugin.utils;

import dev.maanraj514.regionsplugin.RegionsPlugin;
import dev.maanraj514.regionsplugin.object.Bound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class RegionFile {

    private final File file;
    private final YamlConfiguration configuration;

    /**
     * @param fileName The file name (without file extension)
     */
    public RegionFile(String fileName) throws IOException, InvalidConfigurationException {
        if (!RegionsPlugin.getInstance().getDataFolder().exists()){
            RegionsPlugin.getInstance().getDataFolder().mkdirs();
        }
        File regionFolder = new File(RegionsPlugin.getInstance().getDataFolder(), "Regions");
        if (!regionFolder.exists()){
            regionFolder.mkdirs();
        }
        this.file = new File(regionFolder, fileName + ".yml");
        this.configuration = new YamlConfiguration();
        if (!file.exists()) {
            file.createNewFile();
        }
        this.configuration.load(file);
    }

    public YamlConfiguration getConfiguration(){
        return configuration;
    }

    public void saveConfig() throws IOException {
        this.configuration.save(file);
    }

    public void reloadConfig() throws IOException, InvalidConfigurationException {
        if (!file.exists()) {
            file.createNewFile();
        }
        this.configuration.load(file);
    }

    public void createRegion() {
        if (!file.exists()){
            Bukkit.getLogger().severe("STOP TRYING TO CREATE A REGION FROM A FILE THAT DOESN'T EXIST!");
            return;
        }
        if (configuration == null){
            Bukkit.getLogger().severe("YAMLCONFIGURATION DOESN'T EXIST?!?!?!?!?");
            return;
        }

        if (RegionsPlugin.getInstance().getRegionManager().getRegions().containsKey(file.getName())) {
            Bukkit.getLogger().severe("This region already exists, therefore cannot be made again!");
            return;
        }

        String regionName = configuration.getString("name");
        String description = configuration.getString("description");
        boolean isSafeZone = configuration.getBoolean("safezone");

        ConfigurationSection boundSection = configuration.getConfigurationSection("bound");
        if (boundSection == null){
            return;
        }

        ConfigurationSection maxSection = boundSection.getConfigurationSection("max");
        if (maxSection == null){
            return;
        }
        ConfigurationSection minSection = boundSection.getConfigurationSection("min");
        if (minSection == null){
            return;
        }

        Location max = new Location(Bukkit.getWorld(Objects.requireNonNull(maxSection.getString("world"))), maxSection.getDouble("x"), maxSection.getDouble("y"), maxSection.getDouble("z"));
        Location min = new Location(Bukkit.getWorld(Objects.requireNonNull(minSection.getString("world"))), minSection.getDouble("x"), minSection.getDouble("y"), minSection.getDouble("z"));

        Bound bound = new Bound(max, min, isSafeZone);
        bound.assignCorrectBounds();

        RegionsPlugin.getInstance().getRegionManager().createNewRegion(regionName, description, isSafeZone, bound);
    }
}