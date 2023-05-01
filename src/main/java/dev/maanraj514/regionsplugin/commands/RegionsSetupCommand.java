package dev.maanraj514.regionsplugin.commands;

import dev.maanraj514.regionsplugin.RegionsPlugin;
import dev.maanraj514.regionsplugin.managers.RegionManager;
import dev.maanraj514.regionsplugin.object.Bound;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

import static me.kodysimpson.simpapi.colors.ColorTranslator.*;
import static me.kodysimpson.simpapi.colors.MessageUtils.msgPlayer;

public class RegionsSetupCommand extends SubCommand {

    private final Map<UUID, Bound> regionSetup = RegionsPlugin.getInstance().getRegionSetup();

    @Override
    public String getName() {
        return "setup";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("stp");
    }

    @Override
    public String getDescription() {
        return "&aSetups a region!";
    }

    @Override
    public String getSyntax() {
        return "/regions setup";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(translateColorCodes(RegionsPlugin.getInstance().getMessageNotAPlayerMessage()));
            return;
        }
        Player player = (Player) sender;

        if (args.length == 0){
            msgPlayer(player, "&cInvalid command arguments!");
            return;
        }

        if (!regionSetup.containsKey(player.getUniqueId())) {
            // this means they want to go into setup mode
            regionSetup.put(player.getUniqueId(), new Bound());
            msgPlayer(player, "&eLeft-Click &fon a corner block to set &aPosition #1",
                    "&eRight-Click &fon the opposite corner block to set &bPosition #2",
                    "&fWhen done, type /region setup &d<Region name> <isSafeZone (true or false)> [Description (Optional)]");
        } else if (args.length == 1) {
            regionSetup.remove(player.getUniqueId());
            msgPlayer(player, "&cCancelled region setup!");
        } else if (args.length < 3) {
            msgPlayer(player, "&cInvalid usage, Correct usage: &f/region setup <Region name> <isSafeZone (true or false)> [Description (Optional)]");
        } else {
            Bound bound = regionSetup.get(player.getUniqueId());
            if (!bound.isComplete()) {
                msgPlayer(player, "&cPlease select the two opposite corners of the boundary!");
                return;
            }
            String name = args[1];
            String safeInput = args[2].toLowerCase();
            boolean safezone;

            if (safeInput.equalsIgnoreCase("true")) {
                safezone = true;
            } else if (safeInput.equalsIgnoreCase("false")) {
                safezone = false;
            } else {
                msgPlayer(player, "&fPlease specify &ctrue/false &ffor the region to be a safezone!");
                return;
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 4; i <= args.length; i++) {
                builder.append(args[i - 1]).append(" ");
            }
            String description = builder.toString().trim();
            bound.assignCorrectBounds();

            RegionManager regionManager = RegionsPlugin.getInstance().getRegionManager();
            if (regionManager.getRegions().containsKey(decolor(name).toLowerCase())) {
                msgPlayer(player, "&fRegion: " + name + " &falready exists!");
                return;
            }
            regionManager.createNewRegion(name, description, safezone, bound);

            regionSetup.remove(player.getUniqueId());
            msgPlayer(player, "&fCreated new region " + name + "");
            if (description.equalsIgnoreCase("")) {
                msgPlayer(player, "&fDescription: &7None");
            } else {
                msgPlayer(player, "&fDescription: " + description);
            }
            msgPlayer(player, "&fIs safezone: " + safezone);
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        switch (args.length){
            case 1:
                return Collections.singletonList("setup");
            case 2:
                return Collections.singletonList("<Region name>");
            case 3:
                return Arrays.asList("true", "false");
            case 4:
                return Collections.singletonList("[Description (Optional)]");
        }
        return null;
    }
}