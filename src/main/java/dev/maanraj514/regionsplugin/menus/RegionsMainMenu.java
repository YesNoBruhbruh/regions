package dev.maanraj514.regionsplugin.menus;

import dev.maanraj514.regionsplugin.RegionsPlugin;
import dev.maanraj514.regionsplugin.managers.RegionManager;
import dev.maanraj514.regionsplugin.object.Region;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.PaginatedMenu;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegionsMainMenu extends PaginatedMenu {
    public RegionsMainMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public List<?> getData() {

        return new ArrayList<>(RegionsPlugin.getInstance().getRegionManager().getRegions().values());
    }

    @Override
    public void loopCode(Object object) {
        RegionManager regionManager = RegionsPlugin.getInstance().getRegionManager();

        for (Region region : regionManager.getRegions().values()) {
            if (region != null){

                ItemStack regionItem = makeItem(Material.LIME_WOOL, ColorTranslator.translateColorCodes(region.getName()),
                        "&aDescription: " + region.getDescription(),
                        "&eIsSafeZone: " + (region.isSafeZone() ? "&atrue" : "&cfalse"));

                if (!inventory.contains(regionItem)){
                    inventory.addItem(regionItem);
                }
            }
        }

        ItemStack itemStack = makeItem(Material.DIRT, "DISADB");
        itemStack.setAmount(64);

        for (int i = 0; i < 100; i++){
            inventory.addItem(itemStack);
        }
    }

    @Override
    public @Nullable HashMap<Integer, ItemStack> getCustomMenuBorderItems() {
        // we can leave this as null because we don't want to override anything
        return null;
    }

    @Override
    public String getMenuName() {
        return "Main Menu";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null) {
            String itemName = ColorTranslator.decolor(e.getCurrentItem().getItemMeta().getDisplayName());

            switch (e.getCurrentItem().getType()) {
                case DARK_OAK_BUTTON:
                    if (itemName.equalsIgnoreCase("Left")) {
                        if (prevPage()){
                            System.out.println("test1");
                        }else{
                            System.out.println("test3");
                        }
                    }else if (itemName.equalsIgnoreCase("Right")){
                        if (nextPage()){
                            System.out.println("test2");
                        }else{
                            System.out.println("test4");
                        }
                    }
                    break;
                case BARRIER:
                    if (itemName.equalsIgnoreCase("Close")) {
                        p.closeInventory();
                    }
                    break;
            }
        }
    }

//    @Override
//    public void setMenuItems() {
//
//        setFillerGlass();
//
//        RegionManager regionManager = RegionsPlugin.getInstance().getRegionManager();
//
//        for (Map.Entry<String, Region> entry : regionManager.getRegions().entrySet()) {
//            String key = entry.getKey();
//
//            if (regionManager.getRegions().containsKey(key)) {
//                Region region = regionManager.getRegions().get(key);
//
//                ItemStack regionItem = makeItem(Material.LIME_WOOL, ColorTranslator.translateColorCodes(region.getName()),
//                        "&aDescription: " + region.getDescription(),
//                        "&eIsSafeZone: " + (region.isSafeZone() ? "&atrue" : "&cfalse"));
//
//                inventory.addItem(regionItem);
//            }
//        }
//    }
}