package tr.elite.eliterising.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tr.elite.eliterising.Language;

import static tr.elite.eliterising.EliteRising.*;

public class CommandLang implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                sendError(LANGUAGE.getValue("eliterising.cmd.lang.nonOp"),p);
                return true;
            }
            String operator = args[0];
            if (!equalsOneMore(operator,"get","set")) {
                sendError(LANGUAGE.getValue("eliterising.cmd.lang.invalidOp"),p);
                return true;
            }
            if (operator.equals("get")) {
                sendMessage(LANGUAGE.getValue("eliterising.cmd.lang.currentLang") + ": " + LANGUAGE.getLanguage());
            } else if (operator.equals("set")) {
                if (IS_STARTED) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.lang.gameStarted"),p);
                    return true;
                }
                if (args.length < 2) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.lang.nonLang"),p);
                    return true;
                }
                if (Language.existLanguage(args[1])) {
                    sendError(LANGUAGE.getValue("eliterising.cmd.lang.invalidLang"),p);
                    return true;
                }
                LANGUAGE.setLanguage(args[1]);
                sendMessage(LANGUAGE.getValue("eliterising.cmd.lang.setSuc1"),p);
            }
        } else {
            if (args.length == 0) {
                log(LANGUAGE.getValue("eliterising.cmd.lang.nonOp"));
                return true;
            }
            String operator = args[0];
            if (!equalsOneMore(operator,"get","set")) {
                log(LANGUAGE.getValue("eliterising.cmd.lang.invalidOp"));
                return true;
            }
            if (operator.equals("get")) {
                log(LANGUAGE.getValue("eliterising.cmd.lang.currentLang") + ": " + LANGUAGE.getLanguage());
            } else if (operator.equals("set")) {
                if (IS_STARTED) {
                    log(LANGUAGE.getValue("eliterising.cmd.lang.gameStarted"));
                    return true;
                }
                if (args.length < 2) {
                    log(LANGUAGE.getValue("eliterising.cmd.lang.nonLang"));
                    return true;
                }
                if (Language.existLanguage(args[1])) {
                    log(LANGUAGE.getValue("eliterising.cmd.lang.invalidLang"));
                    return true;
                }
                LANGUAGE.setLanguage(args[1]);
                log(LANGUAGE.getValue("eliterising.cmd.lang.setSuc1"));
            }
        }
        return true;
    }
}
