package tr.elite.eliterising;

import tr.elite.eliterising.Commands.CommandVersion;

import java.util.Objects;

/**
 * <h1>Version</h1>
 * <p>
 *     This class contains everything about versions. Don't change this file. <br></br>
 *     Methods: {@link #isBeta()}, {@link #getFull(boolean)}, {@link #getHotfix()},
 *     {@link #getMain()}, {@link #getSubmain()}
 * </p>
 * @author EliteDev, SadeceEmir
 * @since EL-1.3 <br></br>
 * @see CommandVersion
 */
public class Version {
    boolean isBeta;
    String main;
    String submain;
    String hotfix;
    Version(String ver) {
        this.isBeta = ver.startsWith("Beta");
        this.main = isBeta ? ver.substring(5).split("\\.")[0] : ver.split("\\.")[0];
        this.submain = isBeta ? ver.substring(5).split("\\.")[1] : ver.split("\\.")[1];
        this.hotfix = ver.split("\\.").length > 2 ? (isBeta ? ver.substring(5).split("\\.")[2] : ver.split("\\.")[2]) : "";
    }

    public boolean isBeta() {
        return isBeta;
    }

    public String getMain() {
        return main;
    }

    public String getSubmain() {
        return submain;
    }

    public String getHotfix() {
        return hotfix;
    }

    public String getFull(boolean prefix) {
        return (prefix ? "EL-" : "") + main + "." + submain + (Objects.equals(hotfix, "") ? "" : ("." + hotfix));
    }
}
