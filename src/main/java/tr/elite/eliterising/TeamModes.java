package tr.elite.eliterising;

import org.bukkit.configuration.MemorySection;
import tr.elite.eliterising.Commands.CommandTeam;

import java.util.*;

import static tr.elite.eliterising.EliteRising.instance;

/**
 * <h1>Team Modes</h1>
 * <p>
 *     This class contains everything about team modes. Don't change this file. <br></br>
 *     Methods: {@link #getModeByName(String)}, {@link #getName()}, {@link #getNumber()}
 * </p>
 * @author EliteDev, SadeceEmir
 * @since EL-1.1 <br></br>
 * @see CommandTeam
 */
public enum TeamModes {
    NORMAL (1,"normal"),
    SOLO (1,"solo"),
    DUO (2, "duo"),
    TRIPLE (3, "triple"),
    SQUAD (4, "squad"),
    PENTA (5, "penta"),
    HALF (EliteRising.getPlayers().size() / 2, "half");

    int number;
    String name;
    TeamModes(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getNumber() {
        return this.number;
    }

    public static int getCustomTeamModeNumber(String name) {
        return EliteRising.instance.getConfig().getInt("custom_modes.team_modes." + name);
    }

    public static ArrayList<String> getConfigTeamModes() {
        MemorySection ms = (MemorySection) instance.getConfig().get("custom_modes.teamModes");
        assert ms != null;
        return  new ArrayList<>(ms.getValues(false).keySet());
    }

    public static TeamModes getModeByName(String name) {
        return TeamModes.valueOf(name.toUpperCase(Locale.ENGLISH));
    }
}
