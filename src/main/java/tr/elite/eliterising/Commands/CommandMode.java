package tr.elite.eliterising.Commands;

import org.bukkit.ChatColor;
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

                if (equalsOneMore(modeName,"normal","elytra","overpowered","archery","build")) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonHandle"),player);
                    return true;
                }

                ArrayList<String> items = removeFirstValues(2, args);

                if (items.isEmpty()) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonItems"), player);
                    return true;
                }

                HashMap<String, ArrayList<String>> h = new HashMap<>();
                h.put(modeName, items);

                CUSTOM_START_MODES.add(h);
                sendMessage(LANGUAGE.getValue("eliterising.cmd.mode.createSuc"), player);
                break;
            case "edit":
                if (args.length < 2) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonModeName"),player);
                    return true;
                }

                String modeName1 = args[1];

                if (equalsOneMore(modeName1,"normal","elytra","overpowered","archery","build")) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonHandle"),player);
                    return true;
                }

                ArrayList<String> items1 = removeFirstValues(2, args);

                if (items1.isEmpty()) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonItems"), player);
                    return true;
                }

                int i = 0;
                for (int j = 0; j < CUSTOM_START_MODES.size(); j++) {
                    HashMap<String, ArrayList<String>> h1 = CUSTOM_START_MODES.get(j);
                    if (h1.containsKey(modeName1)) {
                        i = j;
                    }
                }
                CUSTOM_START_MODES.remove(i);

                HashMap<String, ArrayList<String>> h3 = new HashMap<>();
                h3.put(modeName1, items1);

                CUSTOM_START_MODES.add(h3);
                sendMessage(LANGUAGE.getValue("eliterising.cmd.mode.editSuc"), player);
                break;
            case "delete":
                if (args.length < 2) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonModeName"),player);
                    return true;
                }

                String modeName2 = args[1];

                if (equalsOneMore(modeName2,"normal","elytra","overpowered","archery","build")) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.mode.nonHandle"),player);
                    return true;
                }

                int i1 = 0;
                for (int j1 = 0; j1 < CUSTOM_START_MODES.size(); j1++) {
                    HashMap<String, ArrayList<String>> h4 = CUSTOM_START_MODES.get(j1);
                    if (h4.containsKey(modeName2)) {
                        i1 = j1;
                    }
                }
                CUSTOM_START_MODES.remove(i1);
                sendMessage(LANGUAGE.getValue("eliterising.cmd.mode.removeSuc"), player);
                break;
            case "list":
                String s = "";
                for (int i2 = 0;i2 < CUSTOM_START_MODES.size();i2++) {
                    HashMap<String,ArrayList<String>> hash = CUSTOM_START_MODES.get(i2);
                    s += "" + ChatColor.GOLD + removeFirstLastValues(String.valueOf(Collections.singletonList(hash.keySet()).get(0))) + (i2 == CUSTOM_START_MODES.size() - 1 ? "" : (ChatColor.AQUA + ", "));
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
