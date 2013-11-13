package dk.ls.hunter.maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class AnimalsContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<AnimalMap> ITEMS = new ArrayList<AnimalMap>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, AnimalMap> ITEM_MAP = new HashMap<String, AnimalMap>();

    static {
        // Add 3 sample items.
//        addItem(new AnimalMap("1", "R�v"));
//        addItem(new AnimalMap("2", "Gr� and"));
//        addItem(new AnimalMap("3", "Kronhjort"));
    }

    private static void addItem(AnimalMap item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
}
