package dev.maanraj514.regionsplugin.object;

import dev.maanraj514.regionsplugin.RegionsPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import static me.kodysimpson.simpapi.colors.ColorTranslator.decolor;

public class Region {

    private final String name;
    private final String rawName;
    private final String description;
    private final Bound bound;
    private final boolean isSafeZone;

    public Region(String name, String description, Bound bound, boolean isSafeZone) {
        this.name = name;
        rawName = decolor(name);
        this.description = description;
        this.bound = bound;
        this.isSafeZone = isSafeZone;

        new BukkitRunnable() {
            int count = 10;
            @Override
            public void run() {
                if (count <= 0){
                    cancel();
                }else{
                    for (Location location : bound.locationsFromTwoPoints(bound.getMax(), bound.getMin())){
                        if (location.getWorld() != null){
                            location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 1);
                        }
                    }
                }
                count--;
            }
        }.runTaskTimerAsynchronously(RegionsPlugin.getInstance(), 0L, 5L);
    }

    public String getName() {
        return name;
    }

    public String getRawName() {
        return rawName;
    }

    public String getDescription() {
        return description;
    }

    public Bound getBound() {
        return bound;
    }

    public boolean isSafeZone() {
        return isSafeZone;
    }
}
