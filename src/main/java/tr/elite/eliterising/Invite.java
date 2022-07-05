package tr.elite.eliterising;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import static org.bukkit.Bukkit.*;

/**
 * <h1>Invite</h1>
 * <p>
 *     This class contains everything about invites and their codes. Don't change this file.
 *     Methods: {@link #getInviter(String)}, {@link #getInvited(String)}, {@link #existInvite(String)}, {@link #registerInvite(String)} <br></br>
 *     Invite codes like: <br></br>
 *     if: INVITER = SadeceEmir, INVITED = memotex -> code = EL-inv(SadeceEmir,memotex)
 * </p>
 * <p>The Invites: {@link #invites}</p>
 * @author EliteDev, SadeceEmir
 * @since EL-1.4 <br></br>
 * @see tr.elite.eliterising.Commands.CommandDavet
 */

public class Invite {
    private static final ArrayList<String> invites = new ArrayList<>();

    public static Player getInviter(String inviteCode) {
        if ((!existInvite(inviteCode)) || (!isCode(inviteCode))) {
            return null;
        }
        String name = inviteCode.substring("EL-inv(".length(), inviteCode.length() - 1).split(",")[0];
        return getPlayer(name) == null ? null : getPlayer(name);
    }

    public static Player getInvited(String inviteCode) {
        if ((!existInvite(inviteCode)) || (!isCode(inviteCode))) {
            return null;
        }
        String name = inviteCode.substring("EL-inv(".length(), inviteCode.length() - 1).split(",")[1];
        return getPlayer(name) == null ? null : getPlayer(name);
    }

    public static void registerInvite(String code) {
        if (!isCode(code)) {
            return;
        }
        invites.add(code);
    }

    public static boolean existInvite(String code) {
        return invites.contains(code);
    }

    public static void expire(String code) {
        if ((!existInvite(code)) || (!isCode(code))) {
            return;
        }
        invites.remove(code);
    }

    public static ArrayList<String> getInvites() {
        return invites;
    }

    public static String createCode(String inviter, String invited) {
        return "EL-inv(" + inviter + "," + invited + ")";
    }

    public static ArrayList<String> getInvitesFromPlayer(String p) {
        ArrayList<String> r = new ArrayList<>();
        for (String code : invites) {
            if (getInviter(code).getName().equals(p)) {
                r.add(code);
            }
        }
        return r;
    }

    public static ArrayList<String> getInvitesToPlayer(String p) {
        ArrayList<String> r = new ArrayList<>();
        for (String code : invites) {
            if (getInvited(code).getName().equals(p)) {
                r.add(code);
            }
        }
        return r;
    }

    public static boolean isCode(String code) {
        return code.startsWith("EL-inv");
    }
}
