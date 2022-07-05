package tr.elite.eliterising.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static org.bukkit.Bukkit.getLogger;
import static tr.elite.eliterising.EliteRising.*;

public class CommandMode implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            log(LANGUAGE.getValue("eliterising.main.notReal"));
            return true;
        }
        Player player = (Player) sender;
        if (!(player.hasPermission("mode"))) {
            sendError(LANGUAGE.getValue("eliterising.main.notEnoughPerm"),player);
            return true;
        }

        if (args.length == 0) {
            sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonOp"), player);
            return true;
        }

        String operation = args[0];

        if (!equalsOneMore(operation,"create","edit","delete","list","info")) {
            sendError(LANGUAGE.getValue("eliterising.cmd.mode.invalidOp"), player);
            return true;
        }

        switch (operation) {
            case "create":
                if (args.length < 2) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonModeName"),player);
                    return true;
                }

                String modeName = args[1];

                if (equalsOneMore(modeName,"standart","elytra","overpowered","archery","build")) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonHandle"),player);
                    return true;
                }

                ArrayList<String> items = removeFirstValues(2, args);

                if (items.isEmpty()) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonItems"), player);
                    return true;
                }

                CUSTOM_GAME_MODES.put(modeName,items);
                sendMessage(LANGUAGE.getValue("eliterising.cmd.mode.createSuc"), player);
                break;
            case "edit":
                if (args.length < 2) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonModeName"),player);
                    return true;
                }

                String modeName1 = args[1];

                if (equalsOneMore(modeName1,"standart","elytra","overpowered","archery","build")) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonHandle"),player);
                    return true;
                }

                ArrayList<String> items1 = removeFirstValues(2, args);

                if (items1.isEmpty()) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonItems"), player);
                    return true;
                }

                CUSTOM_GAME_MODES.replace(modeName1,items1);
                sendMessage(LANGUAGE.getValue("eliterising.cmd.mode.editSuc"), player);
                break;
            case "delete":
                if (args.length < 2) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonModeName"),player);
                    return true;
                }

                String modeName2 = args[1];

                if (equalsOneMore(modeName2,"standart","elytra","overpowered","archery","build")) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonHandle"),player);
                    return true;
                }

                CUSTOM_GAME_MODES.remove(modeName2);
                sendMessage(LANGUAGE.getValue("eliterising.cmd.mode.removeSuc"), player);
                break;
            case "list":
                String s = "";
                for (String n : CUSTOM_GAME_MODES.keySet()) {
                    int i2 = new ArrayList<>(CUSTOM_GAME_MODES.keySet()).indexOf(n);
                    s += "" + ChatColor.GOLD + n + (i2 == CUSTOM_GAME_MODES.size() - 1 ? "" : (ChatColor.AQUA + ", "));
                }
                sendMessage(
                        ChatColor.AQUA + LANGUAGE.getValue("eliterising.cmd.mode.listAllTeams") + "\n" +
                                (s.equals("") ? LANGUAGE.getValue("eliterising.cmd.list.no") : s)
                        ,player
                );
                break;
            case "info":
                sendMessage(
                        ChatColor.AQUA + LANGUAGE.getValue("eliterising.cmd.mode.info.onlyEnch") + ": " + ChatColor.GOLD + "minecraft:ITEM_NAME{Enchantments:[{id:ENCH_NAME,level:LEVEL},{...},...]}" +
                        ChatColor.AQUA + LANGUAGE.getValue("eliterising.cmd.mode.info.onlyAmount") + ": " + ChatColor.GOLD + "minecraft:ITEM_NAME/AMOUNT" +
                        ChatColor.AQUA + LANGUAGE.getValue("eliterising.cmd.mode.info.both") + ": " + ChatColor.GOLD + "minecraft:ITEM_NAME{Enchantments:[{id:ENCH_NAME,level:LEVEL},{...},...]}/AMOUNT" +
                        ChatColor.AQUA + LANGUAGE.getValue("eliterising.cmd.mode.info.generic")
                        ,player
                );
                break;
        }
        return true;
    }
}
