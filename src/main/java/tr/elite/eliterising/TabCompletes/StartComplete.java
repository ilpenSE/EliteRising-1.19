package tr.elite.eliterising.TabCompletes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import tr.elite.eliterising.GameModes;
import tr.elite.eliterising.TeamModes;

import static tr.elite.eliterising.EliteRising.*;

import java.util.*;

public class StartComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        ArrayList<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("standart");
            completions.add("elytra");
            completions.add("overpowered");
            completions.add("archery");
            completions.add("build");
            if (CUSTOM_GAME_MODES.size() > 0) {
                completions.addAll(CUSTOM_GAME_MODES.keySet());
            }
            if (GameModes.getConfigGameModes().size() > 0) {
                completions.addAll(GameModes.getConfigGameModes());
            }
        } else if (args.length == 2) {
            completions.add("normal");
            completions.add("solo");
            completions.add("duo");
            completions.add("triple");
            completions.add("squad");
            completions.add("penta");
            completions.add("half");
            completions.addAll(TeamModes.getConfigTeamModes());
        } else if (args.length > 2) {
            completions.add("");
        }

        return completions;
    }
}
