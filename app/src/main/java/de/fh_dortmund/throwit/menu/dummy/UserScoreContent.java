package de.fh_dortmund.throwit.menu.dummy;

import android.os.Build;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class UserScoreContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<UserScoreItem> ITEMS = new ArrayList<UserScoreItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, UserScoreItem> ITEM_MAP = new HashMap<String, UserScoreItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        addItem(createUserScoreItem("tom", 1,2));
        addItem(createUserScoreItem("frank", 5,8));
        addItem(createUserScoreItem("stefan", 19,12));
        addItem(createUserScoreItem("meier", 2,17));
        addItem(createUserScoreItem("slayer", 19,15));
        addItem(createUserScoreItem("würger", 3,10));
        addItem(createUserScoreItem("xxxAwesomexxx", 19,10));
        addItem(createUserScoreItem("der Alte", 7,20));
        addItem(createUserScoreItem("Sick but normal", 19,23));
        addItem(createUserScoreItem("NotaHero", 2,21));
        addItem(createUserScoreItem("MasterxMaster", 19,20));
        addItem(createUserScoreItem("Kaboom", 8,12));
        addItem(createUserScoreItem("Susen", 1,22));
        addItem(createUserScoreItem("Klara", 2,2));
        addItem(createUserScoreItem("Anna", 1,5));
        addItem(createUserScoreItem("Frank", 15,13));
        addItem(createUserScoreItem("Wo bin ich", 1,1));
        addItem(createUserScoreItem("Der Neue", 2,11));
    }

    public static List<UserScoreItem> returnHighScore(){

        class ScoreItemComperator implements Comparator<UserScoreItem> {
            public int compare(UserScoreItem item1, UserScoreItem item2) {
                return ((int) item1.score - (int) item2.score);
            }
        }
        Collections.sort(ITEMS, new ScoreItemComperator());
        Collections.reverse(ITEMS);

        return ITEMS;
    }

    public static List<UserScoreItem> returnHighHeight(){

        class HeightItemComperator implements Comparator<UserScoreItem> {
            public int compare(UserScoreItem item1, UserScoreItem item2) {
                return ((int) item1.height - (int) item2.height);
            }
        }
        Collections.sort(ITEMS, new HeightItemComperator());
        Collections.reverse(ITEMS);

        return ITEMS;
    }

    public static void newScore(String name, double multi, double height){
        addItem(createUserScoreItem(name, multi, height));
    }


    private static void addItem(UserScoreItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.id), item);
    }

    private static UserScoreItem createUserScoreItem(String name, double multi, double height) {
        return new UserScoreItem(name, (multi*height), height);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class UserScoreItem {
        private static int classID;
        public final String id;
        public final String name;
        public final double score;
        public final double height;

        private UserScoreItem(String name, double score, double height) {
            this.id = String.valueOf(classID);
            this.name = name;
            this.score = score;
            this.height = height;
            UserScoreItem.classID++;
        }

        @Override
        public String toString() {
            return String.valueOf(score);
        }
    }

}
