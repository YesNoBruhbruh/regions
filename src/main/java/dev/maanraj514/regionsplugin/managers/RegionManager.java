package dev.maanraj514.regionsplugin.managers;

import dev.maanraj514.regionsplugin.object.Bound;
import dev.maanraj514.regionsplugin.object.Region;
import dev.maanraj514.regionsplugin.utils.RegionFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static me.kodysimpson.simpapi.colors.ColorTranslator.decolor;
import static me.kodysimpson.simpapi.colors.ColorTranslator.translateColorCodes;

public class RegionManager {
    private final Map<String, Region> regions = new HashMap<>();

    public void createNewRegion(String name, String description, boolean isSafeZone, Bound bound){
        Region region = new Region(name, description, bound, isSafeZone);

        try{

            RegionFile regionFile = new RegionFile(decolor(translateColorCodes(name)));

            regionFile.getConfiguration().set("name", name);
            regionFile.getConfiguration().set("description", description);
            regionFile.getConfiguration().set("safezone", isSafeZone);

            ConfigurationSection boundSection = regionFile.getConfiguration().createSection("bound");
            ConfigurationSection maxSection = boundSection.createSection("max");
            ConfigurationSection minSection = boundSection.createSection("min");

            maxSection.set("world", bound.getMax().getWorld().getName());
            maxSection.set("x", bound.getMax().getX());
            maxSection.set("y", bound.getMax().getY());
            maxSection.set("z", bound.getMax().getZ());

            minSection.set("world", bound.getMin().getWorld().getName());
            minSection.set("x", bound.getMin().getX());
            minSection.set("y", bound.getMin().getY());
            minSection.set("z", bound.getMin().getZ());

            regionFile.saveConfig();
            regionFile.reloadConfig();

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        regions.put(decolor(name).toLowerCase(), region);
    }
    public Map<String, Region> getRegions() {
        return regions;
    }
}
