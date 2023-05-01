package dev.maanraj514.regionsplugin.commands;

import dev.maanraj514.regionsplugin.RegionsPlugin;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static me.kodysimpson.simpapi.colors.ColorTranslator.translateColorCodes;
import static me.kodysimpson.simpapi.colors.MessageUtils.msgPlayer;

public class RegionsReloadCommand extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("rl");
    }

    @Override
    public String getDescription() {
        return "&cReloads the regions! (&edo this at your own risk)";
    }

    @Override
    public String getSyntax() {
        return "/regions reload";
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

        RegionsPlugin.getInstance().getRegionManager().getRegions().clear();
        RegionsPlugin.getInstance().loadRegions();

        msgPlayer(player, "&eReloaded regions!");
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        if (args.length == 1) {
            return Collections.singletonList("reload");
        }
        return null;
    }
}
