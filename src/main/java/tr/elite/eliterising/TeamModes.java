package tr.elite.eliterising;

import java.util.*;

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

    public static TeamModes getModeByName(String name) {
        return TeamModes.valueOf(name.toUpperCase(Locale.ENGLISH));
    }
}
