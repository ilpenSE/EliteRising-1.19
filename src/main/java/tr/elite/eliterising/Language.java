package tr.elite.eliterising;

import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;

import static tr.elite.eliterising.EliteRising.*;

/**
 * <h1>Language</h1>
 * <p>
 *     This class contains everything about languages. Don't change this file. <br></br>
 *     Methods: {@link #getLanguage()}, {@link #getValue(String)}, {@link #existLanguage(String)}, {@link #getAllLanguages()}, {@link #setLanguage(String)}
 * </p>
 * @author EliteDev, SadeceEmir
 * @since EL-1.3
 */
public class Language {
    String lan;
    MemorySection ms;
    public Language(String lan) {
        this.lan = lan;
        this.ms = (MemorySection) instance.configuration.get("lang." + lan);
    }

    public String getLanguage() {
        return lan;
    }

    public String getValue(String key) {
        return ms.getString(key);
    }

    public void setLanguage(String lan) {
        LANGUAGE = new Language(lan);
        COLORS = new Colors(lan);
        instance.configuration.set("language",lan);
    }

    public static boolean existLanguage(String lan) {
        return instance.configuration.get("lang." + lan) == null;
    }
    
    public static ArrayList<String> getAllLanguages() {
        MemorySection m = (MemorySection) instance.configuration.get("lang");
        assert m != null;
        return new ArrayList<>(m.getValues(false).keySet());
    }
}
