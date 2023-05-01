package dev.maanraj514.regionsplugin.commands;

import dev.maanraj514.regionsplugin.RegionsPlugin;
import dev.maanraj514.regionsplugin.menus.RegionsMainMenu;
import me.kodysimpson.simpapi.command.SubCommand;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static me.kodysimpson.simpapi.colors.ColorTranslator.translateColorCodes;
import static me.kodysimpson.simpapi.colors.MessageUtils.msgPlayer;

public class RegionMenuCommand extends SubCommand {
    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("m");
    }

    @Override
    public String getDescription() {
        return "&aOpens the main menu of this plugin!";
    }

    @Override
    public String getSyntax() {
        return "/regions menu";
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

        try {
            MenuManager.openMenu(RegionsMainMenu.class, player);
        } catch (MenuManagerException | MenuManagerNotSetupException e) {
            msgPlayer(player, "&cError has occurred!", "&cContact devs immediately!");
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        if (args.length == 1) {
            return Collections.singletonList("menu");
        }
        return null;
    }
}
