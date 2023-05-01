package dev.maanraj514.regionsplugin.listeners;

import dev.maanraj514.regionsplugin.RegionsPlugin;
import dev.maanraj514.regionsplugin.object.Bound;
import dev.maanraj514.regionsplugin.object.Region;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.kodysimpson.simpapi.colors.MessageUtils.*;

public class RegionListener implements Listener {
    private final Map<UUID, Bound> regionSetup = RegionsPlugin.getInstance().getRegionSetup();
    private final Map<UUID, Region> playerRegion = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!regionSetup.containsKey(player.getUniqueId())) return;
        Bound bound = regionSetup.get(player.getUniqueId());
        Block block = event.getClickedBlock();
        if (block == null) return;
        switch (event.getAction()){
            case LEFT_CLICK_BLOCK:
                bound.setMin(block.getLocation());
                msgPlayer(player, "&aLocation #1 set!");
                break;
            case RIGHT_CLICK_BLOCK:
                if (event.getHand() == EquipmentSlot.HAND) {
                    bound.setMax(block.getLocation());
                    msgPlayer(player, "&bLocation #2 set!");
                }
                break;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Region current = playerRegion.get(player.getUniqueId());
        Region active = null;

        for (Region region : RegionsPlugin.getInstance().getRegionManager().getRegions().values()) {
            if (region != null){
                if (!region.getBound().isInBounds(player.getLocation())) continue;
                active = region;
                if (current == region) continue;
                titlePlayer(player, region.getName(), region.getDescription(), 15, 50, 15);
                soundPlayer(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.5f);
            }
        }
        playerRegion.put(player.getUniqueId(), active);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Region region = playerRegion.get(player.getUniqueId());
        if (region != null && region.isSafeZone()){
            event.setCancelled(true);
        }
    }
}