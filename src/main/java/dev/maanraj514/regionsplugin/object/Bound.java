package dev.maanraj514.regionsplugin.object;

import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bound {

    private Location min;
    private Location max;

    private int minX;
    private int maxX;
    private int minY, maxY;
    private int minZ;
    private int maxZ;
    private boolean isSafeZone;

    public Bound(){
    }

    public Bound(Location location1, Location location2, boolean isSafeZone) {
        this.min = location1;
        this.max = location2;

        this.isSafeZone = isSafeZone;
    }

    public void assignCorrectBounds() {
        maxX = Math.max(max.getBlockX(), min.getBlockX());
        minX = Math.min(max.getBlockX(), min.getBlockX());

        maxY = Math.max(max.getBlockY(), min.getBlockY());
        minY = Math.min(max.getBlockY(), min.getBlockY());

        maxZ = Math.max(max.getBlockZ(), min.getBlockZ());
        minZ = Math.min(max.getBlockZ(), min.getBlockZ());

        min = new Location(min.getWorld(), minX, minY, minZ);
        max = new Location(max.getWorld(), maxX, maxY, maxZ);
    }

    public boolean isInBounds(Location location) {
        return (location.getBlockX() <= maxX && location.getBlockX() >= minX) && (location.getY() <= maxY && location.getY() >= minY) && (location.getBlockZ() <= maxZ && location.getBlockZ() >= minZ);
    }
    //                                                     max                 min
    public List<Location> locationsFromTwoPoints(Location loc1, Location loc2) {
        List<Location> locations = new ArrayList<>();

        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));

        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));

        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));

        for (int x = bottomBlockX; x <= topBlockX + 1; x++)
        {
            for (int z = bottomBlockZ; z <= topBlockZ + 1; z++)
            {
                for (int y = bottomBlockY; y <= topBlockY + 1; y++)
                {

                    locations.add(loc1.getWorld().getBlockAt(x,y,z).getLocation());
                }
            }
        }

        return locations;
    }

    public boolean isComplete() {
        return min != null && max != null;
    }

    public boolean isSafeZone(){
        return this.isSafeZone;
    }

    public Location getMin() {
        return min;
    }

    public void setMin(Location location){
        this.min = location;
        playParticles();
    }

    public void setMax(Location location){
        this.max = location;
        playParticles();
    }

    public Location getMax() {
        return max;
    }

    public void playParticles(){
        if (getMax() != null && getMin() != null){
            for (Location location : locationsFromTwoPoints(getMax(), getMin())){
                Objects.requireNonNull(location.getWorld()).spawnParticle(Particle.VILLAGER_HAPPY, location, 1);
            }
        }
    }
}
