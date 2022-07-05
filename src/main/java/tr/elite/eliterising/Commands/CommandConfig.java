package tr.elite.eliterising.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tr.elite.eliterising.EliteRising;

import static tr.elite.eliterising.EliteRising.*;

public class CommandConfig implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                sendError(LANGUAGE.getValue("eliterising.cmd.config.nonSetting"),sender);
                return true;
            }
            String operator = args[0];
            if (!equalsOneMore(operator,"invite_expire","language","grace_period")) {
                sendError(LANGUAGE.getValue("eliterising.cmd.config.invalidSetting"),sender);
                return true;
            }
            if (args.length == 1) {
                sendError(LANGUAGE.getValue("eliterising.cmd.config.nonOp"),sender);
                return true;
            }
            String executor = args[1];
            if (!equalsOneMore(executor,"get","set")) {
                sendError(LANGUAGE.getValue("eliterising.cmd.config.invalidOp"),sender);
                return true;
            }
            switch (operator) {
                case "invite_expire":
                    if (executor.equals("get")) {
                        sendMessage(LANGUAGE.getValue("eliterising.cmd.config.cExpire") + ": " + getConfigration("inviteExpire", instance.getConfig()),sender);
                    } else if (executor.equals("set")) {
                        if (args.length == 2) {
                            sendError(LANGUAGE.getValue("eliterising.cmd.config.nonValue"),sender);
                            return true;
                        }
                        String val = args[2];
                        if (!isInteger(val)) {
                            sendError(LANGUAGE.getValue("eliterising.cmd.config.invalidValue"),sender);
                            return true;
                        }
                        setConfig("inviteExpire", val);
                        CommandTeam.expireTime = Integer.parseInt(val);
                        sendMessage(LANGUAGE.getValue("eliterising.cmd.config.sucExpire"),sender);
                    }
                    break;
                case "language":
                    if (executor.equals("get")) {
                        sendMessage(LANGUAGE.getValue("eliterising.cmd.config.cLang") + ": " + getConfigration("language", instance.getConfig()),sender);
                    } else if (executor.equals("set")) {
                        if (args.length == 2) {
                            sendError(LANGUAGE.getValue("eliterising.cmd.config.nonValue"),sender);
                            return true;
                        }
                        String val = args[2];
                        if (!equalsOneMore(val,"en", "tr")) {
                            sendError(LANGUAGE.getValue("eliterising.cmd.config.invalidLang"),sender);
                            return true;
                        }
                        setConfig("language", val);
                        LANGUAGE.setLanguage(val);
                        sendMessage(LANGUAGE.getValue("eliterising.cmd.config.sucLang"),sender);
                    }
                    break;
                case "grace_period":
                    if (executor.equals("get")) {
                        sendMessage(LANGUAGE.getValue("eliterising.cmd.config.cPeriod") + ": " + getConfigration("gracePeriod", instance.getConfig()),sender);
                    } else if (executor.equals("set")) {
                        if (args.length == 2) {
                            sendError(LANGUAGE.getValue("eliterising.cmd.config.nonValue"),sender);
                            return true;
                        }
                        String val = args[2];
                        if (!isInteger(val)) {
                            sendError(LANGUAGE.getValue("eliterising.cmd.config.invalidValue"),sender);
                            return true;
                        }
                        setConfig("gracePeriod", val);
                        CommandStart.gracePeriod = Integer.parseInt(val);
                        sendMessage(LANGUAGE.getValue("eliterising.cmd.config.sucPeriod"),sender);
                    }
                    break;
            }
        }
        return true;
    }

    public static void sendMessage(String s,CommandSender sender) {
        if (sender instanceof Player) {
            EliteRising.sendMessage(s,(Player) sender);
        } else {
            log(s);
        }
    }

    public static void sendError(String s,CommandSender sender) {
        if (sender instanceof Player) {
            EliteRising.sendError(s,(Player) sender);
        } else {
            log(s);
        }
    }
}
